package com.hoanganh.drugstore.model

import com.hoanganh.drugstore.model.drugstore.UserComment


data class Evaluate(
        val comment: String,
        val commentDate: Any,
        val id: Int,
        val userComment: UserComment,
        val vote: Double
)