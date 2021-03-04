package com.hoanganh.drugstore.model.clinic

data class ClinicWorkTime(
        val weekDay: String,
        var workingAfternoon: Boolean,
        var workingMorning: Boolean
)