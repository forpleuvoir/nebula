# Nebula

forpleuvoir 的基础代码库。Kotlin 多模块库，发布至 `maven.forpleuvoir.moe`。

## 技术栈

- **语言**: Kotlin 2.3.21, JVM 21
- **构建**: Gradle (ShadowJar, Maven Publishing)
- **序列化**: kotlinx-serialization 1.11.0 (编译器插件)
- **依赖**: kotlinx-coroutines 1.11.0

## 模块结构

### `:nebula-common` — 通用工具
- **颜色** (`moe.forpleuvoir.nebula.common.color`): `Color` (ARGB inline value class), `Colors` 颜色常量, HSV/RGB 转换, 插值 `lerp`/`hsvLerp`, 反色
- **网络** (`moe.forpleuvoir.nebula.common.net`): `HttpHelper` — 基于 `java.net.http.HttpClient` 的链式 HTTP 请求封装, 支持同步/异步, BodyPublisher, 超时/Header/Params
- **安全** (`moe.forpleuvoir.nebula.common.util.security`): `AESUtil`, `RSAUtil` 加解密工具
- **反射** (`moe.forpleuvoir.nebula.common.util.reflect`): `ClassScanner` — 类路径扫描
- **IO** (`moe.forpleuvoir.nebula.common.util.io`): `FileUtil`
- **协程** (`moe.forpleuvoir.nebula.common.util`): `CoroutineUtil`
- **数学** (`moe.forpleuvoir.nebula.common.util.math`): `Interpolation` (lerp)
- **类型工具**: `StringUtil`, `FloatUtil`, `BooleanUtil`, `EnumUtil`, `DateAndTime`, `DataStructureUtil`
- **通用接口**: `Matchable`, `Initializable`, `Notifiable`, `Resettable`, `ExperimentalApi`

### `:nebula-event` — 事件 API (Array-backed Invoker)
- **核心模式**: `Event<T>` 抽象类, 通过 `EventFactory.create` 创建, 所有 listener 以 `Array<T>` 形式缓存, 由 `invokerFactory` 合成最终 `invoker`
- **注册/注销**: `Event.register(listener)` / `Event.register(phase, listener)` → 返回 `Registration` (fun interface), 支持单个 listener 按 phase 精确注销
- **阶段排序**: 基于 Kahn 拓扑排序, 区分 declared phases (`createWithPhases`) 与 dynamic phases (运行时 `register(phase, ...)`), `addPhaseOrdering(first, second)` 动态声明偏序; declared phases 始终优先于同层级的 dynamic phases
- **线程安全**: 写操作 (`register`/`unregister`/`addPhaseOrdering`) 由 `synchronized` 保护, `invoker` 以 `@Volatile` 保证读端可见性; 重建 listener 数组后原子发布
- **默认阶段**: `Event.DEFAULT_PHASE` (`"nebula:default"`), 任何 Event 自动拥有该阶段
- **实现类**: `ArrayBackedEvent<T>` (internal), 维护 `linkedMapOf<phase, listeners>`, 每次变更触发 `rebuild()` → topological sort → 新数组 + 新 invoker
- **工厂**: `EventFactory.create` / `create(emptyInvoker, invokerFactory)` / `createWithPhases(vararg defaultPhases, invokerFactory)`, 后者要求显式包含 `Event.DEFAULT_PHASE`

### `:nebula-serialization` — 序列化框架

#### 文本格式框架 (`ast/`)
- **`SyntaxDialect`**: 格式入口接口, `decode(input) -> Result<SerializeElement>`, `encode(element) -> String`
- **`SyntaxEncoder`**: 内部编码器 fun interface, `SerializeElement -> String`
- **`SyntaxDecoder`**: 内部解码器 fun interface, `List<Token> -> Result<SerializeElement>`
- **`Lexer`**: 内部词法分析器 fun interface, `String -> List<Token>`
- **`Token`**: 词法标记 (sealed interface), 含 `Symbol`/`Literal`/`Identifier`/`EOF` + 格式特定 `Special`(TableHeader, ArrayOfTablesHeader)
- **`TokenPos`**: 行列偏移三维定位
- **`Primitive`**: inline value class 包装原始值, 提供 `asSerialize`/`asNull` 转换
- **`SyntaxReadException`**: 位置感知解析异常

#### JSON (`json/`)
- **`JsonDialect`**: 标准 JSON 编解码
- **`JsonEncoder`**: `{ }` 对象 / `[ ]` 数组, 2-space 缩进
- **`JsonDecoder`**: tailrec 递归下降解析
- **`JsonLexer`**: 字符流词法分析, 提供 `parseNumber()` 共享数字解析 (BigDecimal/BigInteger 精度保留)

#### TOML (`toml/`)
- **`TomlDialect`**: TOML v1.0.0 编解码
- **`TomlEncoder`**: `open class`, `key = value` / `[table]` / `[[array]]` 输出, hook 方法支持注释
- **`TomlCommentedEncoder`**: `abstract class`, `getComment(path)` 注入 `# comment`
- **`TomlDecoder`**: 线性 token 遍历, 支持点分隔键、内联表/数组
- **`TomlLexer`**: 完整 TOML 词法 (基本/字面量/多行字符串, hex/oct/bin 数字, 日期时间)

#### HJSON (`hjson/`)
- **`HJsonDialect`**: HJSON (Human JSON) 编解码
- **`HJsonEncoder`**: `abstract class`, 无引号键, 可选根对象 `{}`, `'''` 多行字符串
- **`HJsonCommentedEncoder`**: `abstract class`, 路径追踪 + `getComment(path)` 注入 `# comment`
- **`HJsonDecoder`**: tailrec `parseMembers`/`parseElements`, 无根对象自动检测
- **`HJsonLexer`**: 支持 `#`/`//`/`/* */` 注释, 无引号字符串, 三引号多行

#### YAML (`yml/`)
- **`YamlDialect`**: YAML 1.1 子集编解码
- **`YamlEncoder`**: `open class`, `key: value` + 缩进嵌套, `- ` 列表, `|` 多行字符串, indent-aware hook
- **`YamlCommentedEncoder`**: `abstract class`, 缩进对齐 `# comment`, `getComment(path)` 注入
- **`YamlDecoder`**: 基于行的递归下降解析器, 缩进感知嵌套, 支持 `{}`/`[]` 内联空容器

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

### `:nebula-config` — 配置管理
- **核心接口** (`config/`):
  - `ConfigNode` — 节点基接口 (name, parent, root, metadata, path, comment), 继承 `Initializable` + `Matchable` + `Serde`
  - `ConfigValued<C>` — 值语义 (defaultValue, getValue/setValue, asString, 委托运算符)
  - `ConfigSerde<C>` — 序列化策略 (sealed: `ViaCodec<C>` / `ViaKSerializer<C>`)
- **抽象基类**:
  - `ConfigItem<C>` — 组合 Node + Valued + Resettable + Notifiable, `_value` 存储, 变更通知
  - `Config<C>` — Codec/KSerializer 驱动的通用配置项, 一行构造
- **容器**:
  - `ConfigGroup` — 显式 `addConfig()` 注册, children LinkedHashMap, ser/des 迭代
  - `ConfigManager` — 继承 Group, 添加 `components` 和 save/load 生命周期
- **配置项类型** (`item/`):
  - `ConfigString`, `ConfigBoolean`, `ConfigNumber<T>`(clamp), `ConfigByte/Short/Int/Long/Float/Double`
  - `ConfigList<T>` — 实现 `MutableList<T>`, 变更自动通知
  - `ConfigMap<V>` — 实现 `MutableMap<String,V>`, 变更自动通知
  - `ConfigJavaEnum<E>`, `ConfigEnum<T>`
  - `ConfigColor` (via ColorCodec/KSerializer), `ConfigDuration` (via DurationSerializer)
- **持久化** (`persistence/`): `ConfigPersistence` → `JsonConfigPersistence` (内嵌 JSON 编码器)
- **组件** (`component/`): `ConfigManagerComponent`(含 `manager`) → `LocalConfig`, `AutoSave`
- **异常处理**: `ExceptionHandler` (Terminal/Throw 策略), `SerializationException`, `DeserializationException`
- **Builder DSL**: `SerializeObject.build { }` / `SerializeArray()` — 构建 SerializeElement
- **运算符**: `getValue`/`setValue` 委托属性, `invoke()` 取值
- **扩展**: `comment`/`path`/`pathWithRoot`/`isRoot`/`flat`/`items`/`groups`/`startup()`

## 开发流程

### 提交工作流

1. **查看变更**: 并行执行 `git status`、`git diff`、`git log --oneline -10`
2. **分析变更范围**: 确定涉及的文件和模块
3. **检查版本变化**: 执行 `git diff gradle/libs.versions.toml` 确认 `nebulaVersion` 是否有变更
4. **起草提交信息**: 使用中文，1-2 句话，聚焦 "why" 而非 "what"
5. **展示给用户确认**: 必须将草稿提交信息展示给用户，用户同意后才能执行提交
6. **提交**: `git add -A` → `git commit -m "<message>"`
7. **打 tag**: 若版本有变更，执行 `git tag -a v<nebulaVersion> -m "v<nebulaVersion>"`
8. **提交后验证**: `git status`

### 其他

- **测试**: JUnit 5 (useJUnitPlatform)
- **构建**: `./gradlew build`
- **发布**: `./gradlew publishNebulaToReleases` / `publishNebulaToSnapshots` / `publishNebulaToLocal`
- **ShadowJar**: 产物合并为 `nebula-<version>.jar`（发布时 `classifier = ""`）
