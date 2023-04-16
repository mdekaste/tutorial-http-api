package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class CustomerRequest(
    val firstName: String,
    val lastName: String,
    val email: String
)