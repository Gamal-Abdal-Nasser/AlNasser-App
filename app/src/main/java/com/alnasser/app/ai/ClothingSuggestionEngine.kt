package com.alnasser.app.ai

import com.alnasser.app.domain.BodyMeasurements
import com.alnasser.app.domain.ClothingCategory
import com.alnasser.app.domain.ClothingItem
import com.alnasser.app.domain.Outfit

/**
 * AI-powered clothing suggestion engine
 * Recommends clothing items and outfits based on body measurements
 */
class ClothingSuggestionEngine {
    
    /**
     * Suggest clothing items that fit the body measurements
     */
    fun suggestItems(
        measurements: BodyMeasurements,
        allItems: List<ClothingItem>,
        category: ClothingCategory? = null
    ): List<ClothingItem> {
        val filteredItems = if (category != null) {
            allItems.filter { it.category == category }
        } else {
            allItems
        }
        
        return filteredItems
            .filter { it.fitsBody(measurements) }
            .sortedByDescending { calculateFitScore(it, measurements) }
    }
    
    /**
     * Calculate how well an item fits the measurements (0-100)
     */
    private fun calculateFitScore(item: ClothingItem, measurements: BodyMeasurements): Float {
        return when (item.category) {
            ClothingCategory.TOPS, ClothingCategory.FULL_OUTFIT -> {
                val chestMid = (item.chestMin + item.chestMax) / 2f
                val chestRange = item.chestMax - item.chestMin
                val deviation = kotlin.math.abs(measurements.chest - chestMid)
                val score = 100f - (deviation / chestRange * 100f)
                score.coerceIn(0f, 100f)
            }
            ClothingCategory.BOTTOMS -> {
                val waistMid = (item.waistMin + item.waistMax) / 2f
                val waistRange = item.waistMax - item.waistMin
                val deviation = kotlin.math.abs(measurements.waist - waistMid)
                val score = 100f - (deviation / waistRange * 100f)
                score.coerceIn(0f, 100f)
            }
            ClothingCategory.SHOES, ClothingCategory.ACCESSORIES -> 100f
        }
    }
    
    /**
     * Suggest complete outfits
     */
    fun suggestOutfits(
        measurements: BodyMeasurements,
        allItems: List<ClothingItem>,
        count: Int = 3
    ): List<Outfit> {
        val tops = suggestItems(measurements, allItems, ClothingCategory.TOPS).take(5)
        val bottoms = suggestItems(measurements, allItems, ClothingCategory.BOTTOMS).take(5)
        val shoes = suggestItems(measurements, allItems, ClothingCategory.SHOES).take(3)
        
        val outfits = mutableListOf<Outfit>()
        
        // Generate outfit combinations
        for (top in tops.take(count)) {
            for (bottom in bottoms.take(count)) {
                if (outfits.size >= count) break
                
                val outfit = Outfit(
                    top = top,
                    bottom = bottom,
                    shoes = shoes.firstOrNull()
                )
                
                if (isGoodMatch(outfit)) {
                    outfits.add(outfit)
                }
            }
            if (outfits.size >= count) break
        }
        
        return outfits.take(count)
    }
    
    /**
     * Check if outfit items match well together
     */
    private fun isGoodMatch(outfit: Outfit): Boolean {
        val top = outfit.top ?: return false
        val bottom = outfit.bottom ?: return false
        
        // Simple color matching rules
        val matchingColors = mapOf(
            "White" to listOf("Blue", "Black", "Khaki", "Gray"),
            "Black" to listOf("White", "Blue", "Gray", "Khaki"),
            "Blue" to listOf("White", "Black", "Khaki"),
            "Red" to listOf("Black", "Blue"),
            "Gray" to listOf("Black", "Blue", "Khaki")
        )
        
        val topColor = top.color
        val bottomColor = bottom.color
        
        return matchingColors[topColor]?.contains(bottomColor) == true ||
               matchingColors[bottomColor]?.contains(topColor) == true
    }
    
    /**
     * Suggest alternative items if desired item is not available
     */
    fun suggestAlternatives(
        desiredItem: ClothingItem,
        allItems: List<ClothingItem>,
        count: Int = 3
    ): List<ClothingItem> {
        return allItems
            .filter { it.category == desiredItem.category && it.id != desiredItem.id }
            .sortedBy { calculateSimilarity(it, desiredItem) }
            .take(count)
    }
    
    /**
     * Calculate similarity between two items (lower is more similar)
     */
    private fun calculateSimilarity(item1: ClothingItem, item2: ClothingItem): Float {
        var similarity = 0f
        
        // Color similarity
        if (item1.color != item2.color) {
            similarity += 1f
        }
        
        // Price similarity
        val priceDiff = kotlin.math.abs(item1.price - item2.price)
        similarity += priceDiff / 100f
        
        return similarity
    }
    
    /**
     * Estimate custom-made price for unavailable items
     */
    fun estimateCustomPrice(basePrice: Float): Float {
        // Custom-made items typically cost 30-50% more
        return basePrice * 1.4f
    }
    
    /**
     * Get size recommendation for an item
     */
    fun recommendSize(item: ClothingItem, measurements: BodyMeasurements): String {
        return when (item.category) {
            ClothingCategory.TOPS, ClothingCategory.FULL_OUTFIT -> {
                val chest = measurements.chest
                when {
                    chest < 86 -> "XS"
                    chest < 94 -> "S"
                    chest < 102 -> "M"
                    chest < 110 -> "L"
                    chest < 118 -> "XL"
                    else -> "XXL"
                }
            }
            ClothingCategory.BOTTOMS -> {
                val waist = measurements.waist
                when {
                    waist < 71 -> "28"
                    waist < 76 -> "30"
                    waist < 81 -> "32"
                    waist < 86 -> "34"
                    waist < 91 -> "36"
                    else -> "38"
                }
            }
            ClothingCategory.SHOES -> {
                // Simplified shoe size based on height
                val height = measurements.height
                when {
                    height < 160 -> "7"
                    height < 170 -> "8"
                    height < 175 -> "9"
                    height < 180 -> "10"
                    height < 185 -> "11"
                    else -> "12"
                }
            }
            ClothingCategory.ACCESSORIES -> "One Size"
        }
    }
}
