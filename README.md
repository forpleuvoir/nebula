# Nebula

Kotlin 多模块基础代码库，发布至 `maven.forpleuvoir.moe`。

## 模块

- [nebula-common](#nebula-common) — 通用工具套件
- [nebula-event](#nebula-event) — 高性能事件 API
- [nebula-serialization](#nebula-serialization) — 序列化框架
- [nebula-config](#nebula-config) — 配置管理

## 技术栈

| 组件 | 版本 |
| --- | --- |
| Kotlin | 2.3.21 |
| JVM | 21 |
| kotlinx-serialization | 1.11.0 |
| kotlinx-coroutines | 1.11.0 |

---

## nebula-common

通用工具模块，提供颜色、网络、安全、反射、IO 等基础功能。

### Color — 颜色工具

基于 `Int` 的 ARGB 内联值类，支持 RGB/HSV 转换、插值、反色、算术运算。

```kotlin
val color = Color.fromARGB(255, 128, 64)       // 0xFFFF8040
val hex   = Color.fromHexString("#FF8040")      // 同上
val hsv   = Color.fromHSV(0.5f, 0.8f, 0.6f)    // HSV → Color

color.red       // 255
color.hexStr    // "#FF8040"
color.alpha(128) // 透明度
color.lerp(Color.WHITE, 0.5f)       // RGB 插值
color.hsvLerp(Color.WHITE, 0.5f)    // HSV 插值
color.reverse()                      // 反色
color + Color.WHITE                  // 加色混合
```

预定义颜色常量见 `Colors` 对象（500+ 种命名颜色）：

```kotlin
Colors.RED      // 0xFFFF0000
Colors.BLUE     // 0xFF0000FF
Colors.GOLD     // 0xFFFFD700
Colors.PLUM     // 0xFFDDA0DD
```

### HttpHelper — HTTP 客户端

基于 `java.net.http.HttpClient` 的链式请求封装，支持同步/异步。

```kotlin
// GET 请求
httpGet("https://api.example.com/data")
    .timeout(Duration.ofSeconds(15))
    .headers("Authorization" to "Bearer xxx")
    .params("page" to "1", "size" to "20")
    .sendGetBody()          // 同步 → String

// POST 请求
httpPost("https://api.example.com/submit", body = """{"key":"value"}""")
    .send()                 // 同步 → HttpResponse<String>

// 异步请求
httpGet("https://api.example.com/data")
    .sendAsyncGetBody({ body -> println(body) }, { e -> e.printStackTrace() })
```

### 安全工具

```kotlin
// AES 加密/解密
AESUtil.encrypt("plaintext", "my-secret-key-16b")
AESUtil.decrypt("ciphertext", "my-secret-key-16b")

// RSA 加密/解密
val pair = RSAUtil.getKeyPair()
RSAUtil.encrypt("data", pair.publicKey)
RSAUtil.decrypt("encrypted", pair.privateKey)
```

### 其它工具

| 工具 | 说明 |
| --- | --- |
| `ClassScanner` | 包路径类扫描 |
| `FileUtil` | 文件读写 |
| `CoroutineUtil` | `defaultLaunch{}` / `ioLaunch{}` / `ioAsync{}` 协程快捷函数 |
| `Interpolation` | `lerp` 插值函数 |
| `StringUtil` / `FloatUtil` / `BooleanUtil` | 类型工具 |
| `Observable<T>` | 观察者模式接口 |

---

## nebula-event

高性能事件 API，基于数组缓存 + 拓扑排序的实现。

### 基本使用

```kotlin
// 1. 定义事件
val event = EventFactory.create<(String) -> Unit> { handlers ->
    { value -> handlers.forEach { it(value) } }
}

// 2. 注册监听器
val reg = event.register { println("收到: $it") }

// 3. 触发
event.invoker()("hello")

// 4. 注销
reg.unregister()
```

### 阶段排序

```kotlin
val event = EventFactory.create<(Int) -> Unit> { handlers ->
    { value -> handlers.forEach { it(value) } }
}

event.addPhaseOrdering("early", Event.DEFAULT_PHASE)
event.addPhaseOrdering(Event.DEFAULT_PHASE, "late")

event.register("early") { println("first") }
event.register { println("middle") }
event.register("late") { println("last") }

event.invoker()(1)
// 输出: first → middle → last
```

### 声明有序阶段

```kotlin
val event = EventFactory.createWithPhases("before", Event.DEFAULT_PHASE, "after") { handlers ->
    { -> handlers.forEach { it() } }
}
// 等价于手动 addPhaseOrdering

event.register("before") { println(1) }
event.register { println(2) }
event.register("after") { println(3) }
```

---

## nebula-serialization

统一的序列化框架，支持 JSON / TOML / HJSON / YAML 格式。

### 核心 AST

```kotlin
// SerializeElement 层次结构
sealed interface SerializeElement
  ├─ SerializePrimitive   // 原始值（String/Number/Boolean/Char）
  ├─ SerializeObject      // 有序键值对
  ├─ SerializeArray       // 元素列表
  └─ SerializeNull        // null 单例
```

### 格式编解码

```kotlin
val json = JsonDialect()
val toml = TomlDialect()
val hjson = HJsonDialect()
val yaml = YamlDialect()

// 编码
val obj = SerializeObject.build {
    "name" to "nebula"
    "version" to 1
    "nested" {
        "enabled" to true
    }
    "tags" arr {
        add("kotlin")
        add("serialization")
    }
}
println(json.encode(obj))

// 解码
val result = json.decode("""{"name":"nebula","version":1}""")
result.getOrNull()?.asObject?.getString("name")  // "nebula"
```

### Codec 系统

类型安全的编解码器，支持范围校验、默认值、nullable。

```kotlin
// 原始类型 codec
Codec.int                       // Codec<Int>
Codec.string("default")         // 带默认值的 String
Codec.int(0, 1..100)            // 带范围校验
Codec.int.nullable()            // Codec<Int?>

// 复杂对象 codec
val codec = Codec.create<Person>()
    .field("name")  .getter(Person::name)  .codec(Codec.string)
    .field("age")   .getter(Person::age)   .codec(Codec.int)
    .build(::Person)

val person = codec.deserialization(element).getOrThrow()
```

### NebulaFormat — kotlinx.serialization 集成

```kotlin
@Serializable
data class User(val name: String, val age: Int)

val element = NebulaFormat.encodeToElement(User("Alice", 30))
val user    = NebulaFormat.decodeFromElement<User>(element)
```

### Builder DSL

```kotlin
val obj = SerializeObject.build {
    "key" to "value"
    "count" to 42
    "child" {
        "flag" to true
    }
    "items" arr {
        add("a")
        add("b")
        obj { "x" to 1 }
    }
}

val arr = SerializeArray.build {
    add(1); add(2); add(3)
    obj { "id" to "extra" }
}
```

---

## nebula-config

层级化配置管理系统，支持持久化、类型安全、变更通知。

### 定义配置管理器

```kotlin
object AppConfig : ConfigManager("app") {

    init {
        localConfig(Path.of("./config"), yaml())
    }

    val debug   = configBoolean("debug", false)
    val color   = configColor("color", Color.fromARGB(0xFFFF0000))
    val timeout = configDuration("timeout", 5.minutes)

    val strings = Strings()

    class Strings : ConfigGroup("strings_group", this) {
        val list = configList("list", listOf("a", "b", "c"), Codec.string)
    }

    val map by configMap(
        "map", mapOf("k1" to "v1", "k2" to "v2"), Codec.string
    )

    val numbers = Numbers

    object Numbers : ConfigGroup("numbers", this) {
        val port = configInt("port", 8080).comment("服务端口")
        val rate = configDouble("rate", 0.5).apply {
            observe { println("值变更: ${it.getValue()}") }
        }
    }

    enum class Mode { AUTO, MANUAL }
    val mode = configEnum("mode", Mode.AUTO)
    val unit by configEnum("unit", TimeUnit.SECONDS)
}
```

### 持久化与生命周期

```kotlin
// 声明持久化组件 (在 ConfigManager init 中)
localConfig(Path.of("./config"), yaml())       // YAML
localConfig(Path.of("./config"), json())       // JSON
localConfig(Path.of("./config"), toml())       // TOML
localConfig(Path.of("./config"), hjson())      // HJSON

// 启动
suspend fun main() {
    AppConfig.onSaved { println("保存耗时 $it") }
    AppConfig.onLoaded { println("加载耗时 $it") }
    AppConfig.init()

    runCatching {
        AppConfig.load()      // 从文件加载
    }.onFailure {
        AppConfig.markSavable()
    }
    AppConfig.forceSave()     // 强制保存

    // 启动（init + load 快捷方式）
    AppConfig.startup()
}

// 序列化到字符串
val json = JsonConfigPersistence.encode(AppConfig.serialization())
```

### 自动保存

```kotlin
object AppConfig : ConfigManager("app") {

    init {
        localConfig(Path.of("./config"), yaml())
        autoSave(initialDelay = 5.seconds, period = 30.seconds)
    }
}
```

### 变更通知

```kotlin
val port = configInt("port", 8080).apply {
    observe { println("$this, 数值有变! (${it.getValue()})") }
}
```

### 注释

```kotlin
val host = configString("host", "localhost").comment("主机地址")
```

---

## 构建

```bash
# 完整构建
./gradlew build

# 发布
./gradlew publishNebulaToReleases
./gradlew publishNebulaToSnapshots
./gradlew publishNebulaToLocal

# 合并产物
# build/libs/*-nebula.jar (ShadowJar)
```

## 许可证

[MIT](LICENSE)
