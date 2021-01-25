package com.hoanganh.drugstore.model.drugstore

data class Evaluate(
    val comment: String,
    val commentDate: Any,
    val id: Int,
    val userComment: UserComment,
    val vote: Double
)