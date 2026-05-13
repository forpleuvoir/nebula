package moe.forpleuvoir.nebula.serialization.ast

internal class SyntaxReadException(msg: String, pos: TokenPos) :
    RuntimeException("Error at [Line ${pos.line}, Col ${pos.column}]: $msg")