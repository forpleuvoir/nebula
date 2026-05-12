# Nebula

forpleuvoir 的基础代码库。Kotlin 多模块库，发布至 `maven.forpleuvoir.moe`。

## 技术栈

- **语言**: Kotlin 2.2.20, JVM 21
- **构建**: Gradle (ShadowJar, Maven Publishing)
- **依赖**: kotlinx-coroutines 1.10.2, Gson 2.10

## 模块结构

### `:nebula-common` — 通用工具
- **颜色** (`moe.forpleuvoir.nebula.common.color`): `Color` (ARGB inline value class), HSV/RGB 转换, 插值 `lerp`/`hsvLerp`, 反色
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
- **类型体系**: `SerializeElement` (sealed interface) → `SerializePrimitive`, `SerializeObject`, `SerializeArray`, `SerializeNull`
- **`SerializeObject`**: `LinkedTreeMap` 实现, 有序键值对, `add(key, element)` / `get(key)` / `remove(key)` / `has(key)`
- **`SerializeArray`**: `ArrayList<SerializeElement>` 包装
- **`SerializePrimitive`**: 支持 String/Boolean/Number 原始类型, **延迟解析数字**(`LazilyParsedNumber` 避免精度丢失)
- **序列化接口**: `Serializer<T>` (fun interface), `Deserializer<T>`, `Serializable`, `Deserializable`
- **扩展**: 内置 Color/Duration/Date 序列化; Range/Timer/HttpHelper 扩展
- **JSON**: `JsonParser` (递归下降), `JsonSerializer`, `JsonParseException`
- **注解**: `@SerializerName`, `@Deserializable`

### `:nebula-serialization-gson` — Gson 桥接
- **互转**: `JsonElement ↔ SerializeElement`, `JsonObject ↔ SerializeObject`, `JsonArray ↔ SerializeArray`
- **DSL**: `jsonObject { }`, `jsonArray(...)`, `Any.toJsonObject()`, `Any.toJsonStr()`
- **工具**: `JsonObject.getOr(key, default)` 类型安全获取, `Json` 接口(反射转JsonObject)
- **解析扩展**: `String.parseToJsonArray/parseToJsonObject/parseToJsonElement`

### `:nebula-config` — 配置管理
- **架构**: `Config<V, C>` → `ConfigBase<V, C>` → 各类 ConfigItem
- **配置项类型**:
  - `ConfigString`, `ConfigBoolean`, `ConfigNumber`, `ConfigEnum`, `ConfigDate`, `ConfigDuration`, `ConfigRGBColor`
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
