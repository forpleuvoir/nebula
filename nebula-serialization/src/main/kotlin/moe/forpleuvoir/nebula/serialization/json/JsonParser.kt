package moe.forpleuvoir.nebula.serialization.json

import moe.forpleuvoir.nebula.common.api.ExperimentalApi
import moe.forpleuvoir.nebula.common.util.primitive.subSequence
import moe.forpleuvoir.nebula.serialization.base.*
import moe.forpleuvoir.nebula.serialization.extensions.serializeArray
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject

@ExperimentalApi
class JsonParser private constructor(private val charArray: CharArray) {
    companion object {

        private const val ESCAPES = '\\'

        private val SKIP_CHAR = arrayOf(' ', '\t', '\n')

        private val Char.isSkipChar: Boolean get() = this in SKIP_CHAR || this <= ' '

        private const val PAIR_CONNECTION = ':'

        private const val ELEMENT_SEPARATOR = ','

        private val STRING_BEGIN = arrayOf(
//            '\'',
            '\"'
        )

        private val NUMBER_BEGIN = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-')

        private val NUMBER_END = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

        private val NUMBER_CHAR = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+', '.', 'e', 'E')

        private val BOOLEAN_BEGIN = arrayOf('f', 't')

        private const val OBJECT_BEGIN = '{'

        private const val OBJECT_END = '}'

        private const val ARRAY_BEGIN = '['

        private const val ARRAY_END = ']'

        private const val NULL_BEGIN = 'n'

        val ESCAPE
            get() = mapOf(
                "\\\\" to '\\',
                "\\\n" to '\n',
                "\\\t" to '\t',
                "\\\r" to '\r',
                "\\\'" to '\'',
                "\\\"" to '\"',
                "\\\b" to '\b',
                "\\\u000C" to '\u000C',
            )

        val ESCAPE_REVERSE
            get() = mapOf(
                "\\" to "\\\\",
                "\n" to "\\\n",
                "\t" to "\\\t",
                "\r" to "\\\r",
                "\'" to "\\\'",
                "\"" to "\\\"",
                "\b" to "\\\b",
                "\u000C" to "\\\u000C",
            )


        fun parse(json: String): SerializeElement {
            return JsonParser(json.toCharArray()).parseElement()
        }

        fun parse(json: CharArray): SerializeElement {
            return JsonParser(json).parseElement()
        }

        fun parse(json: Array<Char>): SerializeElement {
            return JsonParser(json.toCharArray()).parseElement()
        }

    }


    private var curr = 0

    private val currChar: Char get() = charArray[curr]

    private fun skipChar() {
        while (currChar.isSkipChar) curr++
    }

    private fun isEnd(vararg endChars: Char): Boolean {
        if (currChar in endChars) {
            var cur = curr - 1
            var count = 0
            while (charArray[cur] == ESCAPES) {
                cur--
                count++
            }
            if (count == 0) return true
            return count % 2 == 0
        }
        return false
    }

    private fun parseElement(): SerializeElement {
        while (curr < charArray.size) {
            if (currChar.isSkipChar) {
                curr++
                continue
            }
            return when (currChar) {
                OBJECT_BEGIN     -> parseObject()
                ARRAY_BEGIN      -> parseArray()
                NULL_BEGIN       -> parseNull()
                in STRING_BEGIN  -> parseString()
                in BOOLEAN_BEGIN -> parseBoolean()
                in NUMBER_BEGIN  -> parseNumber()
                else -> throw JsonParseException(
                    "unexpect char '$currChar' index:$curr,from '${
                        charArray.subSequence(
                            (curr - 30).coerceAtLeast(0),
                            curr
                        )
                    }'"
                )
            }
        }
        return SerializeNull
    }

    private fun parseObject(): SerializeObject {
        return serializeObject {
            curr++
            while (curr < charArray.size - 1) {
                skipChar()
                if (isEnd(OBJECT_END)) {
                    curr++
                    break
                }
                val key = parseString().asString
                skipChar()
                if (currChar == PAIR_CONNECTION) curr++
                else throw JsonParseException(
                    "expect '$PAIR_CONNECTION',current key '$key',found $currChar index:$curr,from '${
                        charArray.subSequence(
                            (curr - 30).coerceAtLeast(
                                0
                            ), curr
                        )
                    }'"
                )
                skipChar()
                key - parseElement()//put in object
                skipChar()
                if (isEnd(OBJECT_END)) {
                    curr++
                    break
                } else if (currChar == ELEMENT_SEPARATOR) curr++
                else throw JsonParseException(
                    "expect '$ELEMENT_SEPARATOR' or '$OBJECT_END',found '$currChar' index:$curr,from '${
                        charArray.subSequence(
                            (curr - 30).coerceAtLeast(
                                0
                            ), curr
                        )
                    }'"
                )
            }

        }
    }

    private fun parseArray(): SerializeArray {
        return serializeArray {
            curr++
            while (curr < charArray.size - 1) {
                skipChar()
                if (isEnd(ARRAY_END)) {
                    curr++
                    break
                }
                add(parseElement())
                skipChar()
                if (isEnd(ARRAY_END)) {
                    curr++
                    break
                } else if (currChar == ELEMENT_SEPARATOR) curr++
                else throw JsonParseException(
                    "expect '$ELEMENT_SEPARATOR' or '$ARRAY_END',found '$currChar' index:$curr,from '${
                        charArray.subSequence(
                            (curr - 30).coerceAtLeast(
                                0
                            ), curr
                        )
                    }'"
                )
            }

        }
    }

    private fun parseString(): SerializePrimitive {
        val builder = StringBuilder()
        when (currChar) {
            '\'' -> {
                curr++
                while (!isEnd('\'')) {
                    builder.append(currChar)
                    curr++
                }
            }

            '"'  -> {
                curr++
                while (curr < charArray.size && !isEnd('"')) {
                    builder.append(currChar)
                    curr++
                }
            }

            else -> {
                throw JsonParseException("unexpect char '$currChar' index:$curr,from '${charArray.subSequence((curr - 30).coerceAtLeast(0), curr)}'")
            }
        }
        curr++
        return SerializePrimitive(builder.toString())
    }

    private fun parseNumber(): SerializePrimitive {
        val builder = StringBuilder()
        builder.append(currChar)
        curr++
        while (currChar in NUMBER_CHAR) {
            builder.append(currChar)
            curr++
        }
        if (charArray[curr - 1] !in NUMBER_END) throw JsonParseException(
            "unexpect char '$currChar' index:$curr,from '${
                charArray.subSequence(
                    (curr - 30).coerceAtLeast(
                        0
                    ), curr
                )
            }'"
        )
        val str = builder.toString()
        return if ('e' in str || 'E' in str) {
            val value = str.toBigDecimal()
            if ('.' in str) SerializePrimitive(value)
            else SerializePrimitive(value.toBigInteger())
        } else {
            if ('.' in str) SerializePrimitive(str.toDouble())
            else SerializePrimitive(str.toLong())
        }
    }

    private fun parseBoolean(): SerializePrimitive {

        if (currChar == 'f') {
            curr++
            if (currChar == 'a') {
                curr++
                if (currChar == 'l') {
                    curr++
                    if (currChar == 's') {
                        curr++
                        if (currChar == 'e') {
                            curr++
                            return SerializePrimitive(false)
                        }
                    }
                }
            }
        } else if (currChar == 't') {
            curr++
            if (currChar == 'r') {
                curr++
                if (currChar == 'u') {
                    curr++
                    if (currChar == 'e') {
                        curr++
                        return SerializePrimitive(true)
                    }
                }
            }
        }
        throw JsonParseException("unexpect boolean char '$currChar' index:$curr,from '${charArray.subSequence((curr - 30).coerceAtLeast(0), curr)}'")

    }

    private fun parseNull(): SerializeNull {
        if (currChar == 'n') {
            curr++
            if (currChar == 'u') {
                curr++
                if (currChar == 'l') {
                    curr++
                    if (currChar == 'l') {
                        curr++
                        return SerializeNull
                    }
                }
            }
        }
        throw JsonParseException("unexpect null char $currChar index:$curr,from '${charArray.subSequence((curr - 30).coerceAtLeast(0), curr)}'")
    }
}