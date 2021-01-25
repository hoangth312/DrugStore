package com.hoanganh.drugstore.model.clinic

data class ClinicModel(
    val apartmentNumber: String,
    val city: String,
    val clinicWorkTimes: List<Any>,
    val country: String,
    val district: String,
    val evaluates: List<Evaluate>,
    val faculties: List<Faculty>,
    val id: Int,
    val imageClinics: List<ImageClinic>,
    val information: String,
    val languages: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val province: String,
    val street: String,
    val websiteUrl: String
)