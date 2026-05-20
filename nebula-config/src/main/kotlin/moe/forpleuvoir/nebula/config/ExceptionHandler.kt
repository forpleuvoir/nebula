package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.SerializationException

interface ExceptionHandler {

    fun onSerializationException(config: ConfigNode, e: SerializationException)


    fun onDeserializationException(config: ConfigNode, e: DeserializationException)

    companion object {

        fun ExceptionHandler.onSerializationException(config: ConfigNode, t: Throwable) = onSerializationException(config, SerializationException.wrap(t))
        fun ExceptionHandler.onDeserializationException(config: ConfigNode, t: Throwable) = onDeserializationException(config, DeserializationException.wrap(t))

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

