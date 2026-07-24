package moe.forpleuvoir.nebula.serialization.ast

class SyntaxReadException(val msg: String, val pos: TokenPos) :
    RuntimeException("Error at [Line ${pos.line}, Col ${pos.column}]: $msg")