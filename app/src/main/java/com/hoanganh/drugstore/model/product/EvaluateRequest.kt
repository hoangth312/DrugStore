package com.hoanganh.drugstore.model.product

import com.hoanganh.drugstore.model.UserX1

data class EvaluateRequest(
        val comment: String,
        val updatedDate: String,
        val id: Int,
        val user: UserX1
)