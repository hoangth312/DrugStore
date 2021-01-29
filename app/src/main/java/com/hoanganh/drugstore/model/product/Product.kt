package com.hoanganh.drugstore.model.product

import com.hoanganh.drugstore.model.Comment1

data class Product(
        val attention: String,
        val capacity: String,
        val categoryName: String,
        val commentTotal: Int,
        val companyName: String,
        val description: String,
        val englishName: String,
        val evaluates: List<EvaluateRequest>,
        val id: Int,
        val imageProducts: List<String>,
        val indentifyCode: String,
        val likeTotal: Int,
        val manual: String,
        val price: Int,
        val priceTax: Int,
        val publicationNumber: String,
        val qrCode: String,
        val status: Int,
        val unit: String,
        val vietnameseName: String,
        val vote: String
)