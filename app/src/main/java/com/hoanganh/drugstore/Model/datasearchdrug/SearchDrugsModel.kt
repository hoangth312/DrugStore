package com.hoanganh.drugstore.Model.datasearchdrug

data class SearchDrugsModel(
    val attention: String,
    val capacity: Any,
    val categoryName: String,
    val commentTotal: Int,
    val companyName: String,
    val description: String,
    val englishName: String,
    val evaluates: List<Any>,
    val id: Int,
    val imageProducts: List<String>,
    val indentifyCode: Any,
    val likeTotal: Int,
    val manual: String,
    val price: Int,
    val priceTax: Int,
    val publicationNumber: Any,
    val qrCode: String,
    val status: Int,
    val unit: Any,
    val vietnameseName: String,
    val vote: String
)