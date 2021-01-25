package com.hoanganh.drugstore.model

data class RegisterAccount(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val role: List<String>,
    val username: String
)