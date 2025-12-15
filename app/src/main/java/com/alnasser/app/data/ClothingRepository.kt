package com.alnasser.app.data

import android.content.Context
import com.alnasser.app.domain.ClothingCategory
import com.alnasser.app.domain.ClothingItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

/**
 * Repository for managing clothing items
 * Loads clothing data from JSON assets
 */
class ClothingRepository(private val context: Context) {
    
    private val clothingItems = mutableListOf<ClothingItem>()
    private val gson = Gson()
    
    init {
        loadClothingData()
    }
    
    /**
     * Load clothing data from assets
     */
    private fun loadClothingData() {
        try {
            val inputStream = context.assets.open("clothing/clothing_catalog.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<ClothingItem>>() {}.type
            val items: List<ClothingItem> = gson.fromJson(reader, type)
            clothingItems.addAll(items)
            reader.close()
        } catch (e: Exception) {
            // If file doesn't exist, use default items
            loadDefaultClothing()
        }
    }
    
    /**
     * Load default clothing items
     */
    private fun loadDefaultClothing() {
        clothingItems.addAll(listOf(
            // Tops
            ClothingItem(
                id = "top_001",
                name = "White T-Shirt",
                category = ClothingCategory.TOPS,
                sizes = listOf("XS", "S", "M", "L", "XL", "XXL"),
                price = 19.99f,
                texturePath = "textures/white_tshirt.png",
                color = "White",
                chestMin = 80f,
                chestMax = 120f,
                isAvailable = true,
                description = "Classic white cotton t-shirt"
            ),
            ClothingItem(
                id = "top_002",
                name = "Black T-Shirt",
                category = ClothingCategory.TOPS,
                sizes = listOf("XS", "S", "M", "L", "XL", "XXL"),
                price = 19.99f,
                texturePath = "textures/black_tshirt.png",
                color = "Black",
                chestMin = 80f,
                chestMax = 120f,
                isAvailable = true,
                description = "Classic black cotton t-shirt"
            ),
            ClothingItem(
                id = "top_003",
                name = "Blue Shirt",
                category = ClothingCategory.TOPS,
                sizes = listOf("S", "M", "L", "XL", "XXL"),
                price = 39.99f,
                texturePath = "textures/blue_shirt.png",
                color = "Blue",
                chestMin = 85f,
                chestMax = 120f,
                isAvailable = true,
                description = "Formal blue dress shirt"
            ),
            ClothingItem(
                id = "top_004",
                name = "Red Hoodie",
                category = ClothingCategory.TOPS,
                sizes = listOf("S", "M", "L", "XL"),
                price = 49.99f,
                texturePath = "textures/red_hoodie.png",
                color = "Red",
                chestMin = 85f,
                chestMax = 115f,
                isAvailable = true,
                description = "Comfortable red hoodie"
            ),
            ClothingItem(
                id = "top_005",
                name = "Gray Sweater",
                category = ClothingCategory.TOPS,
                sizes = listOf("S", "M", "L", "XL"),
                price = 44.99f,
                texturePath = "textures/gray_sweater.png",
                color = "Gray",
                chestMin = 85f,
                chestMax = 115f,
                isAvailable = true,
                description = "Warm gray sweater"
            ),
            
            // Bottoms
            ClothingItem(
                id = "bottom_001",
                name = "Blue Jeans",
                category = ClothingCategory.BOTTOMS,
                sizes = listOf("28", "30", "32", "34", "36", "38"),
                price = 59.99f,
                texturePath = "textures/blue_jeans.png",
                color = "Blue",
                chestMin = 0f,
                chestMax = 0f,
                waistMin = 70f,
                waistMax = 110f,
                isAvailable = true,
                description = "Classic blue denim jeans"
            ),
            ClothingItem(
                id = "bottom_002",
                name = "Black Jeans",
                category = ClothingCategory.BOTTOMS,
                sizes = listOf("28", "30", "32", "34", "36", "38"),
                price = 59.99f,
                texturePath = "textures/black_jeans.png",
                color = "Black",
                chestMin = 0f,
                chestMax = 0f,
                waistMin = 70f,
                waistMax = 110f,
                isAvailable = true,
                description = "Stylish black jeans"
            ),
            ClothingItem(
                id = "bottom_003",
                name = "Khaki Pants",
                category = ClothingCategory.BOTTOMS,
                sizes = listOf("30", "32", "34", "36", "38"),
                price = 49.99f,
                texturePath = "textures/khaki_pants.png",
                color = "Khaki",
                chestMin = 0f,
                chestMax = 0f,
                waistMin = 75f,
                waistMax = 110f,
                isAvailable = true,
                description = "Casual khaki pants"
            ),
            ClothingItem(
                id = "bottom_004",
                name = "Gray Shorts",
                category = ClothingCategory.BOTTOMS,
                sizes = listOf("S", "M", "L", "XL"),
                price = 29.99f,
                texturePath = "textures/gray_shorts.png",
                color = "Gray",
                chestMin = 0f,
                chestMax = 0f,
                waistMin = 70f,
                waistMax = 105f,
                isAvailable = true,
                description = "Comfortable gray shorts"
            ),
            
            // Shoes
            ClothingItem(
                id = "shoes_001",
                name = "White Sneakers",
                category = ClothingCategory.SHOES,
                sizes = listOf("7", "8", "9", "10", "11", "12"),
                price = 79.99f,
                texturePath = "textures/white_sneakers.png",
                color = "White",
                chestMin = 0f,
                chestMax = 0f,
                isAvailable = true,
                description = "Classic white sneakers"
            ),
            ClothingItem(
                id = "shoes_002",
                name = "Black Sneakers",
                category = ClothingCategory.SHOES,
                sizes = listOf("7", "8", "9", "10", "11", "12"),
                price = 79.99f,
                texturePath = "textures/black_sneakers.png",
                color = "Black",
                chestMin = 0f,
                chestMax = 0f,
                isAvailable = true,
                description = "Stylish black sneakers"
            ),
            ClothingItem(
                id = "shoes_003",
                name = "Brown Dress Shoes",
                category = ClothingCategory.SHOES,
                sizes = listOf("7", "8", "9", "10", "11", "12"),
                price = 99.99f,
                texturePath = "textures/brown_shoes.png",
                color = "Brown",
                chestMin = 0f,
                chestMax = 0f,
                isAvailable = true,
                description = "Formal brown dress shoes"
            )
        ))
    }
    
    /**
     * Get all clothing items
     */
    fun getAllItems(): List<ClothingItem> {
        return clothingItems.toList()
    }
    
    /**
     * Get items by category
     */
    fun getItemsByCategory(category: ClothingCategory): List<ClothingItem> {
        return clothingItems.filter { it.category == category }
    }
    
    /**
     * Get item by ID
     */
    fun getItemById(id: String): ClothingItem? {
        return clothingItems.find { it.id == id }
    }
    
    /**
     * Search items by name
     */
    fun searchItems(query: String): List<ClothingItem> {
        return clothingItems.filter { 
            it.name.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }
}
