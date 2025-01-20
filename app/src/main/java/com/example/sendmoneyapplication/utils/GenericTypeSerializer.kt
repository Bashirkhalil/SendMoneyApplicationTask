package com.example.sendmoneyapplication.utils

import com.example.sendmoneyapplication.SendMonyScreen.domain.model.FieldLabel
import kotlinx.serialization.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*


object GenericTypeSerializer : KSerializer<Any?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GenericType")

    override fun serialize(encoder: Encoder, value: Any?) {
        val jsonEncoder = encoder as? JsonEncoder ?: throw SerializationException("This class can only be serialized by Json.")
        when (value) {
            is String -> jsonEncoder.encodeString(value)
            is Int -> jsonEncoder.encodeInt(value)
            is Float -> jsonEncoder.encodeFloat(value)
            is Double -> jsonEncoder.encodeDouble(value)
            is Boolean -> jsonEncoder.encodeBoolean(value)
            is FieldLabel -> jsonEncoder.encodeJsonElement(JsonObject(
                buildMap {
                    value.en?.let { put("en", JsonPrimitive(it)) }
                    value.ar?.let { put("ar", JsonPrimitive(it)) }
                }
            ))
            else -> throw SerializationException("Unsupported type for value: ${value}")
        }
    }

    override fun deserialize(decoder: Decoder): Any? {
        val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("This class can only be deserialized by Json.")
        val jsonElement = jsonDecoder.decodeJsonElement()
        return when {
            jsonElement is JsonPrimitive && jsonElement.isString -> jsonElement.content
            jsonElement is JsonPrimitive && isInteger(jsonElement.toString()) -> jsonElement.int
            jsonElement is JsonPrimitive && isFloat(jsonElement.toString()) -> jsonElement.float
            jsonElement is JsonPrimitive && isDouble(jsonElement.toString()) -> jsonElement.double
            jsonElement is JsonPrimitive && isBoolean(jsonElement.toString()) -> jsonElement.boolean
            jsonElement is JsonObject -> {
                FieldLabel(
                    en = jsonElement["en"]?.jsonPrimitive?.contentOrNull,
                    ar = jsonElement["ar"]?.jsonPrimitive?.contentOrNull
                )
            }
            else -> throw SerializationException("Unsupported value type during deserialization")
        }
    }

    private fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false
    private fun isFloat(str: String?): Boolean = str?.toFloatOrNull()?.let { true } ?: false
    private fun isDouble(str: String?): Boolean = str?.toDoubleOrNull()?.let { true } ?: false
    private fun isBoolean(str: String?): Boolean = str?.toBooleanStrictOrNull()?.let { true } ?: false

}
