package com.hoanganh.drugstore.model.clinic

data class Evaluate(
    val comment: String,
    val createdDate: Any,
    val id: Int,
    val likes: Boolean,
    val updatedDate: String,
    val user: User,
    val vote: Double
)