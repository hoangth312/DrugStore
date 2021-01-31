package com.hoanganh.drugstore.model.clinic

import com.hoanganh.drugstore.model.Comment1

data class ClinicModel(
    val apartmentNumber: String,
    val city: String,
    val clinicWorkTimes: List<ClinicWorkTime>,
    val commentTotal: Int,
    val country: String,
    val district: String,
    val evaluates: List<Comment1>,
    val faculties: List<Faculty>,
    val workTimeMorning: String,
    val workTimeAfternoon: String,
    val id: Int,
    val imageClinics: List<String>,
    val information: String,
    val languages: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val phoneNumber: String,
    val province: String,
    val street: String,
    val vote: Double,
    val websiteUrl: String
)