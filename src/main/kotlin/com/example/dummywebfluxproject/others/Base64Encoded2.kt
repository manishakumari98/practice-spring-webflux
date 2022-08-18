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
@JsonSerialize(using = Base64Serializer2::class)
@JsonDeserialize(using = Base64Deserializer2::class)
@JacksonAnnotationsInside
annotation class Base64Encoded2(val mapIn: String = "")

@Component
class Base64Serializer2<T> : JsonSerializer<T>(), ContextualSerializer {
    private lateinit var mapIn: String

    override fun createContextual(serializerProvider: SerializerProvider?, beanProperty: BeanProperty?): JsonSerializer<*> {
        val serializer = Base64Serializer2<T>()
        serializer.mapIn = beanProperty!!.getAnnotation(Base64Encoded2::class.java).mapIn
        return serializer
    }

    override fun serialize(value: T, jsonGenerator: JsonGenerator?, serializerProvider: SerializerProvider?) {
        checkNotNull(jsonGenerator)
        val textToEncode = jacksonObjectMapper().writeValueAsString(value)
        val encodedString = Base64.getEncoder().encodeToString(textToEncode.toByteArray())

        val objectString = if (mapIn.isNotBlank()) mapOf(mapIn to encodedString) else encodedString
        jsonGenerator.writeObject(objectString)
    }
}

@Component
class Base64Deserializer2<T> : JsonDeserializer<T>(), ContextualDeserializer {
    private lateinit var mapIn: String
    private lateinit var classType: Class<*>

    override fun createContextual(deserializationContext: DeserializationContext?, beanProperty: BeanProperty?): JsonDeserializer<*> {
        val deserializer = Base64Deserializer2<Any>()
        deserializer.mapIn = beanProperty!!.getAnnotation(Base64Encoded2::class.java).mapIn
        deserializer.classType = beanProperty.type.rawClass
        return deserializer
    }

    override fun deserialize(jsonParser: JsonParser?, deserializationContext: DeserializationContext?): T {
        val node = jsonParser!!.codec.readTree<JsonNode>(jsonParser)
        val nodeWithText = if (mapIn.isNotBlank()) node.get(mapIn) else node
        val textToDecode = nodeWithText.asText()
        val decodedString = String(Base64.getDecoder().decode(textToDecode))

        val resultObject = jacksonObjectMapper().readValue(decodedString, classType)
        return resultObject as T
    }
}