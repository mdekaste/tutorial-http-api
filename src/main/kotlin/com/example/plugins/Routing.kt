package com.example.plugins

import com.example.routes.deleteCustomerById
import com.example.routes.findCustomerById
import com.example.routes.getAllCustomers
import com.example.routes.postCustomer
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        getAllCustomers()
        postCustomer()
        findCustomerById()
        deleteCustomerById()
    }
}
