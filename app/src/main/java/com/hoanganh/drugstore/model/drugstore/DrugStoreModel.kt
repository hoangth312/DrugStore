package com.hoanganh.drugstore.model.drugstore

data class DrugStoreModel(
    val apartmentNumber: String,
    val city: String,
    val commentTotal: Int,
    val country: String,
    val district: String,
    val evaluates: List<Evaluate>,
    val id: Int,
    val imageDrugStores: List<ImageDrugStore>,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val phoneNumber: String,
    val products: List<Product>,
    val province: String,
    val services: List<Service>,
    val status: Int,
    val street: String,
    val timeWorking: String,
    val vote: Double
)