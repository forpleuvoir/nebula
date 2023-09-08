package moe.forpleuvoir.nebula.common.api

/**
 * 这是一个测试中的API,非常不稳定.如果要使用
 * OptIn 注释注释该用法来接受，例如@OptIn(BetaApi::class)，或使用编译器参数 -opt-in=moe.forpleuvoir.nebula.common.api.BetaApi。
 */
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.TYPEALIAS
)
@MustBeDocumented
annotation class BetaApi
