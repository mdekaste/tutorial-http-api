package com.example.models

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import kotlinx.serialization.Serializable
import java.util.*

data class Customer(
    val firstName: Voornaam,
    val lastName: Achternaam,
    val email: Email
)

@JvmInline value class Voornaam(val firstName: String)
@JvmInline value class Achternaam(val lastName: String)

@JvmInline value class Email private constructor(val email: String){
    companion object{
        private val REGEX = """(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])""".toRegex()

        context(Raise<String>)
        operator fun invoke(email: String) = Email(ensureNotNull(REGEX.matchEntire(email)){ "email niet goed" }.value)

        context(Raise<String>)
        fun of(email: String) = Email(ensureNotNull(REGEX.matchEntire(email)){ "email niet goed" }.value)
    }
}