package moe.forpleuvoir.nebula.config

interface ExceptionHandler {

    fun onSerializationException(config: ConfigNode, e: SerializationException)

    fun onDeserializationException(config: ConfigNode, e: DeserializationException)

    companion object {

        val Terminal: ExceptionHandler = object : ExceptionHandler {
            override fun onSerializationException(config: ConfigNode, e: SerializationException) {
                e.printStackTrace()
            }

            override fun onDeserializationException(config: ConfigNode, e: DeserializationException) {
                e.printStackTrace()
            }
        }

        val Throw: ExceptionHandler = object : ExceptionHandler {
            override fun onSerializationException(config: ConfigNode, e: SerializationException) = throw e
            override fun onDeserializationException(config: ConfigNode, e: DeserializationException) = throw e
        }
    }
}

open class SerializationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

open class DeserializationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
