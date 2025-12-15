package com.alnasser.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.alnasser.app.ai.ClothingSuggestionEngine
import com.alnasser.app.data.ClothingRepository
import com.alnasser.app.domain.*
import com.alnasser.app.rendering.animation.AnimationController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MannequinViewModel(context: Context) : ViewModel() {
    
    private val mannequinManager = MannequinManager()
    private val clothingRepository = ClothingRepository(context)
    private val suggestionEngine = ClothingSuggestionEngine()
    
    private val _mannequins = MutableStateFlow<List<Mannequin>>(emptyList())
    val mannequins: StateFlow<List<Mannequin>> = _mannequins.asStateFlow()
    
    private val _selectedMannequinIndex = MutableStateFlow(0)
    val selectedMannequinIndex: StateFlow<Int> = _selectedMannequinIndex.asStateFlow()
    
    private val _currentAnimation = MutableStateFlow(AnimationController.Animation.IDLE)
    val currentAnimation: StateFlow<AnimationController.Animation> = _currentAnimation.asStateFlow()
    
    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val clothingItems: StateFlow<List<ClothingItem>> = _clothingItems.asStateFlow()
    
    private val _suggestedOutfits = MutableStateFlow<List<Outfit>>(emptyList())
    val suggestedOutfits: StateFlow<List<Outfit>> = _suggestedOutfits.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<ClothingCategory?>(null)
    val selectedCategory: StateFlow<ClothingCategory?> = _selectedCategory.asStateFlow()
    
    private val _canAddMoreMannequins = MutableStateFlow(true)
    val canAddMoreMannequins: StateFlow<Boolean> = _canAddMoreMannequins.asStateFlow()
    
    init {
        // Load clothing items
        _clothingItems.value = clothingRepository.getAllItems()
    }
    
    fun initializeWithMeasurements(measurements: BodyMeasurements) {
        mannequinManager.addMannequin(measurements)
        updateMannequins()
        generateSuggestions()
    }
    
    fun addMannequin() {
        val currentMeasurements = mannequinManager.getSelectedMannequin()?.measurements
        if (currentMeasurements != null && mannequinManager.canAddMore()) {
            mannequinManager.addMannequin(currentMeasurements)
            updateMannequins()
        }
    }
    
    fun removeMannequin(index: Int) {
        mannequinManager.removeMannequin(index)
        updateMannequins()
    }
    
    fun selectMannequin(index: Int) {
        mannequinManager.selectMannequin(index)
        _selectedMannequinIndex.value = index
    }
    
    fun setAnimation(animation: AnimationController.Animation) {
        _currentAnimation.value = animation
    }
    
    fun selectCategory(category: ClothingCategory?) {
        _selectedCategory.value = category
    }
    
    fun applyClothingItem(item: ClothingItem) {
        val selectedMannequin = mannequinManager.getSelectedMannequin() ?: return
        val currentOutfit = selectedMannequin.currentOutfit ?: Outfit()
        
        when (item.category) {
            ClothingCategory.TOPS -> currentOutfit.top = item
            ClothingCategory.BOTTOMS -> currentOutfit.bottom = item
            ClothingCategory.SHOES -> currentOutfit.shoes = item
            ClothingCategory.ACCESSORIES -> {
                currentOutfit.accessories = currentOutfit.accessories + item
            }
            ClothingCategory.FULL_OUTFIT -> {
                currentOutfit.top = item
                currentOutfit.bottom = item
            }
        }
        
        mannequinManager.applyOutfitToSelected(currentOutfit)
        updateMannequins()
    }
    
    fun applyOutfit(outfit: Outfit) {
        mannequinManager.applyOutfitToSelected(outfit)
        updateMannequins()
    }
    
    fun applyMatchingOutfits(outfit: Outfit) {
        mannequinManager.applyMatchingOutfits(outfit)
        updateMannequins()
    }
    
    fun removeClothingItem(category: ClothingCategory) {
        val selectedMannequin = mannequinManager.getSelectedMannequin() ?: return
        val currentOutfit = selectedMannequin.currentOutfit ?: return
        
        when (category) {
            ClothingCategory.TOPS -> currentOutfit.top = null
            ClothingCategory.BOTTOMS -> currentOutfit.bottom = null
            ClothingCategory.SHOES -> currentOutfit.shoes = null
            ClothingCategory.ACCESSORIES -> currentOutfit.accessories = emptyList()
            ClothingCategory.FULL_OUTFIT -> currentOutfit.clear()
        }
        
        mannequinManager.applyOutfitToSelected(currentOutfit)
        updateMannequins()
    }
    
    fun getFilteredClothingItems(): List<ClothingItem> {
        val category = _selectedCategory.value
        val selectedMannequin = mannequinManager.getSelectedMannequin() ?: return emptyList()
        
        return if (category != null) {
            suggestionEngine.suggestItems(
                selectedMannequin.measurements,
                clothingRepository.getItemsByCategory(category)
            )
        } else {
            suggestionEngine.suggestItems(
                selectedMannequin.measurements,
                clothingRepository.getAllItems()
            )
        }
    }
    
    private fun generateSuggestions() {
        val selectedMannequin = mannequinManager.getSelectedMannequin() ?: return
        _suggestedOutfits.value = suggestionEngine.suggestOutfits(
            selectedMannequin.measurements,
            clothingRepository.getAllItems(),
            3
        )
    }
    
    private fun updateMannequins() {
        _mannequins.value = mannequinManager.getAllMannequins()
        _canAddMoreMannequins.value = mannequinManager.canAddMore()
        _selectedMannequinIndex.value = mannequinManager.getSelectedIndex()
    }
    
    fun getRecommendedSize(item: ClothingItem): String {
        val selectedMannequin = mannequinManager.getSelectedMannequin() ?: return "M"
        return suggestionEngine.recommendSize(item, selectedMannequin.measurements)
    }
    
    fun getSelectedMannequin(): Mannequin? {
        return mannequinManager.getSelectedMannequin()
    }
    
    companion object {
        private var instance: MannequinViewModel? = null
        
        fun getInstance(context: Context): MannequinViewModel {
            if (instance == null) {
                instance = MannequinViewModel(context.applicationContext)
            }
            return instance!!
        }
    }
}
