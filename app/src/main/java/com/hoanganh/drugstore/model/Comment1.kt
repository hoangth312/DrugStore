package com.hoanganh.drugstore.model

data class Comment1(
        val comment: String,
        val updatedDate: String,
        val id: Int,
        val user: UserX1,
        val vote: Double
)