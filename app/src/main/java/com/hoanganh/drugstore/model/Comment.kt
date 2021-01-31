package com.hoanganh.drugstore.model

data class Comment(
        val comment: String,
        val drugstoreId: Int?,
        val clinicId:Int?,
        val userId: Int,
        val vote: Double,
)