package com.alnasser.app.ai

import com.alnasser.app.domain.BodyMeasurements
import com.alnasser.app.domain.Gender
import kotlin.math.pow

/**
 * AI-powered body measurement prediction model
 * Uses anthropometric regression equations based on height, weight, and gender
 * 
 * Equations derived from research on body proportions and anthropometry
 */
class BodyPredictionModel {
    
    /**
     * Predict all body measurements from height, weight, and gender
     */
    fun predictMeasurements(height: Float, weight: Float, gender: Gender): BodyMeasurements {
        val heightM = height / 100f // Convert to meters
        val bmi = weight / (heightM * heightM)
        
        return when (gender) {
            Gender.MALE -> predictMaleMeasurements(height, weight, bmi)
            Gender.FEMALE -> predictFemaleMeasurements(height, weight, bmi)
        }
    }
    
    private fun predictMaleMeasurements(height: Float, weight: Float, bmi: Float): BodyMeasurements {
        // Male-specific anthropometric equations
        
        // Chest circumference (cm)
        // Based on height and weight correlation
        val chest = 0.45f * height + 0.35f * weight + 15f
        
        // Waist circumference (cm)
        // More influenced by weight and BMI
        val waist = 0.25f * height + 0.55f * weight + (bmi - 22f) * 2f
        
        // Hip circumference (cm)
        // Males have narrower hips relative to shoulders
        val hips = 0.42f * height + 0.28f * weight + 20f
        
        // Arm length (cm) - shoulder to wrist
        // Proportional to height
        val armLength = height * 0.38f + 2f
        
        // Leg length (cm) - inseam
        // Approximately 45-48% of height for males
        val legLength = height * 0.47f
        
        // Shoulder width (cm)
        // Males have broader shoulders
        val shoulderWidth = 0.24f * height + 0.08f * weight + 5f
        
        // Inseam (cm) - similar to leg length
        val inseam = height * 0.46f
        
        return BodyMeasurements(
            height = height,
            weight = weight,
            gender = Gender.MALE,
            chest = chest.coerceIn(70f, 140f),
            waist = waist.coerceIn(60f, 130f),
            hips = hips.coerceIn(70f, 130f),
            armLength = armLength.coerceIn(50f, 90f),
            legLength = legLength.coerceIn(60f, 110f),
            shoulderWidth = shoulderWidth.coerceIn(35f, 60f),
            inseam = inseam.coerceIn(60f, 105f)
        )
    }
    
    private fun predictFemaleMeasurements(height: Float, weight: Float, bmi: Float): BodyMeasurements {
        // Female-specific anthropometric equations
        
        // Chest/Bust circumference (cm)
        val chest = 0.42f * height + 0.38f * weight + 18f
        
        // Waist circumference (cm)
        // Females typically have smaller waist-to-hip ratio
        val waist = 0.22f * height + 0.48f * weight + (bmi - 21f) * 1.8f
        
        // Hip circumference (cm)
        // Females have wider hips relative to waist
        val hips = 0.48f * height + 0.32f * weight + 15f
        
        // Arm length (cm)
        // Slightly shorter proportionally than males
        val armLength = height * 0.37f + 1.5f
        
        // Leg length (cm)
        // Approximately 44-47% of height for females
        val legLength = height * 0.46f
        
        // Shoulder width (cm)
        // Narrower than males
        val shoulderWidth = 0.22f * height + 0.06f * weight + 3f
        
        // Inseam (cm)
        val inseam = height * 0.45f
        
        return BodyMeasurements(
            height = height,
            weight = weight,
            gender = Gender.FEMALE,
            chest = chest.coerceIn(70f, 130f),
            waist = waist.coerceIn(55f, 120f),
            hips = hips.coerceIn(75f, 140f),
            armLength = armLength.coerceIn(48f, 85f),
            legLength = legLength.coerceIn(58f, 105f),
            shoulderWidth = shoulderWidth.coerceIn(32f, 55f),
            inseam = inseam.coerceIn(58f, 100f)
        )
    }
    
    /**
     * Calculate recommended clothing size based on measurements
     */
    fun recommendSize(measurements: BodyMeasurements): String {
        return when (measurements.gender) {
            Gender.MALE -> recommendMaleSize(measurements)
            Gender.FEMALE -> recommendFemaleSize(measurements)
        }
    }
    
    private fun recommendMaleSize(measurements: BodyMeasurements): String {
        val chest = measurements.chest
        return when {
            chest < 86 -> "XS"
            chest < 94 -> "S"
            chest < 102 -> "M"
            chest < 110 -> "L"
            chest < 118 -> "XL"
            else -> "XXL"
        }
    }
    
    private fun recommendFemaleSize(measurements: BodyMeasurements): String {
        val chest = measurements.chest
        return when {
            chest < 82 -> "XS"
            chest < 88 -> "S"
            chest < 96 -> "M"
            chest < 104 -> "L"
            chest < 112 -> "XL"
            else -> "XXL"
        }
    }
    
    /**
     * Validate input measurements
     */
    fun validateInput(height: Float, weight: Float): Boolean {
        return height in 140f..220f && weight in 40f..200f
    }
}
