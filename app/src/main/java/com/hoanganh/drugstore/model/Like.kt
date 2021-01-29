package com.hoanganh.drugstore.model

data class Like(
    val likes: Boolean,
    val productId: Int,
    val userId: Int
)