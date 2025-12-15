package com.alnasser.app.domain

data class BodyMeasurements(
    val height: Float,           // cm
    val weight: Float,           // kg
    val gender: Gender,
    val chest: Float,            // cm (predicted)
    val waist: Float,            // cm (predicted)
    val hips: Float,             // cm (predicted)
    val armLength: Float,        // cm (predicted)
    val legLength: Float,        // cm (predicted)
    val shoulderWidth: Float,    // cm (predicted)
    val inseam: Float            // cm (predicted)
) {
    companion object {
        fun empty() = BodyMeasurements(
            height = 0f,
            weight = 0f,
            gender = Gender.MALE,
            chest = 0f,
            waist = 0f,
            hips = 0f,
            armLength = 0f,
            legLength = 0f,
            shoulderWidth = 0f,
            inseam = 0f
        )
    }
}
