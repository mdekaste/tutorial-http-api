package com.example.routes

import arrow.core.*
import arrow.core.raise.*
import com.example.models.*
import com.example.storage.CustomerStorage
import com.example.storage.CustomerStorage.getCustomers
import com.example.validation.CustomerFactory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val customerRepository = CustomerStorage
val customerFactory = CustomerFactory()

fun Route.getAllCustomers() {
    get("/customer") {
        effect { getCustomers() }
            .fold(
                { error -> call.respondText(error) },
                { map ->
                    call.respond(map.values.map { customerFactory.toRequest(it) })
                }
            )
    }
}

fun Route.findCustomerById(){
    get("/customer/{id?}") {
        call.parameters["id"]?.let {
            effect {
                val customer = customerRepository.getCustomerById(it)
                customerFactory.toRequest(customer)
            }.fold(
                { error -> call.respondText(error) },
                { request -> call.respond(request) }
            )
        } ?: call.respondText("geen id")
    }
}

fun Route.postCustomer() {
    post("/customer") {
        effect {
            val customerRequest = call.receive<CustomerRequest>()
            val customer = customerFactory.toCustomer(customerRequest) // Nel<String>
            raiseNel { customerRepository.storeCustomer(customer) }
        }.fold(
            { errors: NonEmptyList<String> -> call.respondText(errors.joinToString("\n")) },
            { customerId: String -> call.respond(customerId) }
        )
    }
}

fun Route.deleteCustomerById() {
    delete("/customer/{id?}"){
        effect {
            val customer = customerRepository.deleteCustomerById(call.parameters["id"]!!)
            customerFactory.toRequest(customer)
        }.fold(
            { error: String -> call.respondText(error) },
            { customer: CustomerRequest -> call.respond(customer) }
        )
    }
}

context(Raise<Nel<E>>)
inline fun <A, E> raiseNel(block: Raise<E>.() -> A): A = recover(block) { raise(nonEmptyListOf(it)) }