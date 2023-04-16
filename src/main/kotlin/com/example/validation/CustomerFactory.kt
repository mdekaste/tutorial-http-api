package com.example.validation

import arrow.core.Nel

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.fx.coroutines.parZipOrAccumulate
import com.example.models.*
import kotlinx.coroutines.Dispatchers

class CustomerFactory {

    context(Raise<Nel<String>>)
    suspend fun toCustomer(customerRequest: CustomerRequest): Customer {
        return parZipOrAccumulate(
            Dispatchers.Default,
            { toVoornaam(customerRequest.firstName) },
            { toAchternaam(customerRequest.lastName) } ,
            { Email(customerRequest.email) }
        ){
            voornaam: Voornaam,
            achternaam: Achternaam,
            email: Email
            ->
            Customer(voornaam, achternaam, email)
        }
    }

    fun toRequest(customer: Customer): CustomerRequest =
        CustomerRequest(
            firstName = customer.firstName.firstName,
            lastName = customer.lastName.lastName,
            email = customer.email.email
        )

    context(Raise<String>)
    private fun toVoornaam(firstName: String): Voornaam {
        ensure(firstName.isNotBlank()){ "voornaam is niet goed" }
        return Voornaam(firstName)
    }

    context(Raise<String>)
    private fun toAchternaam(lastName: String): Achternaam {
        ensure(!lastName.contains("O")){ "wij accepteren geen achternamen met een O"}
        return Achternaam(lastName)
    }
}