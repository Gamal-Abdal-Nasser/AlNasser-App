package com.alnasser.app.domain

data class ClothingItem(
    val id: String,
    val name: String,
    val category: ClothingCategory,
    val sizes: List<String>,
    val price: Float,
    val texturePath: String,
    val color: String,
    val chestMin: Float,
    val chestMax: Float,
    val waistMin: Float = 0f,
    val waistMax: Float = 0f,
    val isAvailable: Boolean = true,
    val description: String = ""
) {
    /**
     * Check if this item fits the given measurements
     */
    fun fitsBody(measurements: BodyMeasurements): Boolean {
        return when (category) {
            ClothingCategory.TOPS, ClothingCategory.FULL_OUTFIT -> {
                measurements.chest in chestMin..chestMax
            }
            ClothingCategory.BOTTOMS -> {
                measurements.waist in waistMin..waistMax
            }
            ClothingCategory.SHOES, ClothingCategory.ACCESSORIES -> true
        }
    }
    
    /**
     * Get recommended size for measurements
     */
    fun getRecommendedSize(measurements: BodyMeasurements): String {
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
}
