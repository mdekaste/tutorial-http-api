package com.example.storage

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.*
import arrow.core.right
import com.example.models.*
import java.util.UUID

object CustomerStorage {
    val _customers = mutableMapOf(
        UUID.randomUUID().toString() to
        Customer(
            firstName = Voornaam("michael"),
            lastName = Achternaam("de Kaste"),
            email = either { Email.of("m.dekaste@hotmail.com") }.getOrNull()!!
        )
    )

    context(Raise<String>)
    fun storeCustomer(customer: Customer): String {
        val bestaat = getCustomers().containsValue(customer)
        ensure(!bestaat){ "customer bestaat al" }
        return UUID.randomUUID().toString().also {
            _customers[it] = customer
        }
    }

    fun Raise<String>.getCustomers() : Map<String, Customer>{
        ensure(_customers.isNotEmpty()){ "customers is leeg" }
        return _customers
    }

    context(Raise<String>)
    fun getCustomerById(id: String): Customer {
        val customer = getCustomers()[id]
        ensure(customer != null){ "customer bestaat niet" }
        return customer
    }







































































    fun deleteById(id: String){
        TODO()
    }
}

