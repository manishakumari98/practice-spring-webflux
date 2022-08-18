package com.example.dummywebfluxproject.others

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@JsonSerialize(using = Base64Serializer::class)
@JsonDeserialize(using = Base64Deserializer::class)
@JacksonAnnotationsInside
annotation class Base64Encoded

@Component
class Base64Serializer<T> : JsonSerializer<T>(), ContextualSerializer {

    override fun createContextual(serializerProvider: SerializerProvider?, property: BeanProperty?): JsonSerializer<*> {
        return Base64Serializer<T>()
    }

    override fun serialize(value: T, jsonGenerator: JsonGenerator?, serializerProvider: SerializerProvider?) {
        checkNotNull(jsonGenerator)
        val textToEncode = jacksonObjectMapper().writeValueAsString(value)
        val encodedString = Base64.getEncoder().encodeToString(textToEncode.toByteArray())
        jsonGenerator.writeObject(encodedString)
    }
}

@Component
class Base64Deserializer<T> : JsonDeserializer<T>(), ContextualDeserializer {
    private lateinit var classType: Class<*>

    override fun deserialize(jsonParser: JsonParser?, deserializationContext: DeserializationContext?): T {
        val node = jsonParser!!.codec.readTree<JsonNode>(jsonParser)
        val textToDecode = node.asText()
        val decodedText = String(Base64.getDecoder().decode(textToDecode))
        val resultObject = jacksonObjectMapper().readValue(decodedText, classType)
        return resultObject as T
    }

    override fun createContextual(deserializationContext: DeserializationContext?, beanProperty: BeanProperty?): JsonDeserializer<*> {
        val deserializer = Base64Deserializer<Any>()
        deserializer.classType = beanProperty!!.type.rawClass
        return deserializer
    }
}