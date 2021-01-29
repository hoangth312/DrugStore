package com.hoanganh.drugstore.model

import com.hoanganh.drugstore.model.drugstore.Service

data class DrugStoreItem(
    val apartmentNumber: String,
    val city: String,
    val commentTotal: Int,
    val country: String,
    val district: String,
    val evaluates: List<Any>,
    val id: Int,
    val imageDrugStores: List<Any>,
    val latitude: Double,
    val likeTotal: Int,
    val longitude: Double,
    val name: String,
    val phoneNumber: String,
    val products: List<Any>,
    val province: String,
    val services: List<Service>,
    val status: Int,
    val street: String,
    val timeWorking: Any,
    val vote: String
)