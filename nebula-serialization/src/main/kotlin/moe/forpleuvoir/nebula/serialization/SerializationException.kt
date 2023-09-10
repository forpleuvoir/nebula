package moe.forpleuvoir.nebula.serialization

class SerializationException(message: String, cause: Throwable) : Exception(message, cause)

class DeserializationException(message: String, cause: Throwable) : Exception(message, cause)