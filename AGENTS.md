# Nebula

forpleuvoir 的基础代码库。Kotlin 多模块库，发布至 `maven.forpleuvoir.moe`。

## 技术栈

- **语言**: Kotlin 2.3.21, JVM 21
- **构建**: Gradle (ShadowJar, Maven Publishing)
- **序列化**: kotlinx-serialization 1.8.1 (编译器插件)
- **依赖**: kotlinx-coroutines 1.11.0, Gson 2.10

## 模块结构

### `:nebula-common` — 通用工具
- **颜色** (`moe.forpleuvoir.nebula.common.color`): `Color` (ARGB inline value class), `Colors` 颜色常量, HSV/RGB 转换, 插值 `lerp`/`hsvLerp`, 反色
- **网络** (`moe.forpleuvoir.nebula.common.net`): `HttpHelper` — 基于 `java.net.http.HttpClient` 的链式 HTTP 请求封装, 支持同步/异步, BodyPublisher, 超时/Header/Params
- **数据结构** (`moe.forpleuvoir.nebula.common.util.collection`): `NotifiableArrayList`, `NotifiableLinkedHashMap` — 变更通知型集合
- **安全** (`moe.forpleuvoir.nebula.common.util.security`): `AESUtil`, `RSAUtil` 加解密工具
- **反射** (`moe.forpleuvoir.nebula.common.util.reflect`): `ClassScanner` — 类路径扫描
- **IO** (`moe.forpleuvoir.nebula.common.util.io`): `FileUtil`
- **协程** (`moe.forpleuvoir.nebula.common.util`): `CoroutineUtil`
- **数学** (`moe.forpleuvoir.nebula.common.util.math`): `Interpolation` (lerp)
- **类型工具**: `StringUtil`, `FloatUtil`, `BooleanUtil`, `EnumUtil`, `DateAndTime`, `DataStructureUtil`
- **通用接口**: `Matchable`, `Initializable`, `Notifiable`, `Resettable`, `ExperimentalApi`

### `:nebula-event` — 事件总线
- **核心**: `EventBus` 接口 + `EventBusImpl` (基于 `ConcurrentHashMap` + `ConcurrentLinkedQueue`)
- **事件**: `Event` 接口, 支持 `cancellable`/`canceled`, `CancellableEvent`
- **订阅**: 支持 `priority` 排序 + `greedy` 模式 (向父类传播)
- **静态入口**: `EventBus.DEFAULT_EVENT_BUS` + `EventBus.registerEventBus(name, bus)` 多总线注册
- **异常**: `EventException`

### `:nebula-serialization` — 序列化框架

#### 核心 AST (`base/`)
- **`SerializeElement`** (sealed interface) → `SerializePrimitive`, `SerializeObject`, `SerializeArray`, `SerializeNull`
- **`SerializeObject`**: `LinkedHashMap` 实现, 有序键值对, 委托 `SequencedMap<String, SerializeElement>`
- **`SerializeArray`**: `MutableList<SerializeElement>` 代理
- **`SerializePrimitive`**: 支持 `Char/String/Boolean/Number/BigInteger/BigDecimal` 等类型, `asXxx` 全部返回 nullable (`T?`), **延迟解析数字**(`LazilyParsedNumber` 避免精度丢失)
- **`SerializeNull`**: 单例对象, 表示 null

#### Builder DSL (`base/builder/`)
- **`SerializeObjectBuilder`**: `SerializeObject.build { "key" to value; "nested" { ... } }` — 支持 `context(codec)`/`context(serializer)` 的 `set`/`to`, 原始类型 `to`/`set`, 嵌套 `obj`/`arr`
- **`SerializeArrayBuilder`**: `SerializeArray.build { add(value); obj { ... } }` — 支持 `context(codec)`/`context(serializer)` 的 `add`, 嵌套 `obj`/`arr`

#### Codec 体系 (`codec/`)
- **`Codec<T>`**: 继承 `Serializer<T>` + `Deserializer<T>`, 提供 `companion` 静态方法委托到 `PrimitiveCodec` (`.int` / `.string` / `.nullable()` 等)
- **`CodecBuilder<T>`**: 类型安全的 Builder (支持 1-22 字段), `Codec.create<T>().field(name).getter(prop).codec(codec).build(::Ctor)`
- **`FieldBuilder`**: 字段配置, 支持 `getter`/`default`/`skipDefault`/`skipNull`
- **`PrimitiveCodec`**: 原始类型编解码器 (Int, String, Boolean, BigInteger 等), 支持范围校验
- **`ColorCodec`**: `Color` ↔ hex string `#RRGGBB` 或 RGB/HSV 对象
- **`TimeCodec`**: `Duration` ↔ ISO string, `Date` ↔ Long timestamp
- **`RangeCodec`**: `IntRange`/`LongRange`/`CharRange` 等 → `"start..end"` 字符串

#### NebulaFormat — kotlinx.serialization 集成 (`nebula/`)
- **`NebulaFormat`**: 实现 `SerialFormat`, 将 `@Serializable` data class 编解码为 `SerializeElement`
  - `NebulaFormat.encodeToElement(value, serializer)` / `decodeFromElement(element, serializer)`
  - 扩展 `T.encode()` / `element.decode<T>()` (使用 `SerializerRegistry` 自动查找序列化器)
- **`NebulaEncoder`**: 实现 `Encoder` + `CompositeEncoder`, 栈式容器 (Object/Array/Map), 支持原始类型/结构体/集合
- **`NebulaDecoder`**: 实现 `Decoder`, 三个子解码器:
  - `NebulaObjectDecoder` — 按字段名匹配读取
  - `NebulaMapDecoder` — Map → `{"key": value, ...}`, 按 key-value 对迭代
  - `NebulaArrayDecoder` — 按顺序索引读取
- **`CodecKSerializer<T>`**: `Codec<T> → KSerializer<T>` 的桥接, 支持 `@UseSerializers` 使用
- **`SerializerRegistry`**: 全局序列化器注册表, `register(type, serializer)` → `nebulaSerializer()` 自动查找

#### 扩展 (`extensions/`)
- **`SerializeObjectExtensions`**: 安全读取 —
  - `getXxx` 严格类型检查 (仅当原始类型匹配时返回)
  - `getAsXxx` 宽松转换 (自动转换)
  - `getOrElse` 带默认值
- **`SerializeElementExtensions`**: `completeEquals` 深度比较, `contains` 运算符
- **`SerializeElementCheckTypeResult`**: 类型安全的多分支模式匹配, `check<T> { }`
- **`SerializePrimitiveCheckTypeResult`**: 原始值类型匹配
- **`JavaConversion`**: `SerializeElement.toJava()` → Java 原生类型 (List/Map/原生值)
- **`HttpHelperExtensions`**: `HttpHelper.params(SerializeObject)` / `.headers(SerializeObject)`

#### 注解 (`annotation/`)
已有 `@Serializable`/`@Deserializable` 注解 (引用具体 `Serializer<T>`/`Deserializer<T>` 类), 但属于过时设计, 保留兼容。

### `:nebula-serialization-gson` — Gson 桥接
- **互转**: `JsonElement ↔ SerializeElement`, `JsonObject ↔ SerializeObject`, `JsonArray ↔ SerializeArray`
- **DSL**: `jsonObject { }`, `jsonArray(...)`, `Any.toJsonObject()`, `Any.toJsonStr()`
- **工具**: `JsonObject.getOr(key, default)` 类型安全获取, `Json` 接口(反射转JsonObject)
- **解析扩展**: `String.parseToJsonArray/parseToJsonObject/parseToJsonElement`

### `:nebula-config` — 配置管理
- **架构**: `Config<V, C>` → `ConfigBase<V, C>` → 各类 ConfigItem
- **配置项类型**:
  - `ConfigString`, `ConfigBoolean`, `ConfigNumber`, `ConfigEnum`, `ConfigDate`, `ConfigDuration`, `ConfigColor`
  - `ConfigList`, `ConfigStringList`, `ConfigNumberList`
  - `ConfigStringKeyMap` (String→Any Map)
  - `ConfigCycle` / `ConfigCycleString` (循环值)
- **容器**: `ConfigContainer` → `ConfigContainerImpl`, `ConfigManager`
- **管理器**: `ConfigManagerImpl` — save/load 生命周期, `markSavable`/`markSaved`, `forceSave`
- **持久化**: `ConfigManagerPersistence` → `JsonConfigManagerPersistence` (JSON 文件存储)
- **组件**: `ConfigManagerComponent` → `LocalConfig`(文件路径), `AutoSave`(定时/防抖自动保存)
- **观察者**: `subscribe(callback)`, `onChange`, `onSaved`, `onLoaded`
- **运算符**: `getValue`/`setValue` 支持 Kotlin 委托属性

## 开发流程

- **测试**: JUnit 5 (useJUnitPlatform)
- **构建**: `./gradlew build`
- **发布**: `./gradlew publishNebulaToReleases`, `publishNebulaToSnapshots`, `publishNebulaToLocal`
- **ShadowJar**: 产物合并为 `*-nebula.jar`
