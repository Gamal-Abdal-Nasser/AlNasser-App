package com.alnasser.app.domain

/**
 * Manages multiple mannequins in the scene
 * Handles positioning, selection, and outfit synchronization
 */
class MannequinManager {
    
    private val mannequins = mutableListOf<Mannequin>()
    private var selectedMannequinIndex = 0
    
    companion object {
        const val MAX_MANNEQUINS = 4
        private const val SPACING = 0.8f // Distance between mannequins
    }
    
    /**
     * Add a new mannequin to the scene
     */
    fun addMannequin(measurements: BodyMeasurements): Boolean {
        if (mannequins.size >= MAX_MANNEQUINS) {
            return false
        }
        
        val position = calculatePosition(mannequins.size)
        val mannequin = Mannequin(
            measurements = measurements,
            position = position
        )
        
        mannequins.add(mannequin)
        return true
    }
    
    /**
     * Remove mannequin at index
     */
    fun removeMannequin(index: Int): Boolean {
        if (index !in mannequins.indices) {
            return false
        }
        
        mannequins.removeAt(index)
        
        // Reposition remaining mannequins
        repositionMannequins()
        
        // Adjust selected index
        if (selectedMannequinIndex >= mannequins.size && mannequins.isNotEmpty()) {
            selectedMannequinIndex = mannequins.size - 1
        }
        
        return true
    }
    
    /**
     * Get mannequin at index
     */
    fun getMannequin(index: Int): Mannequin? {
        return mannequins.getOrNull(index)
    }
    
    /**
     * Get all mannequins
     */
    fun getAllMannequins(): List<Mannequin> {
        return mannequins.toList()
    }
    
    /**
     * Get number of mannequins
     */
    fun getCount(): Int = mannequins.size
    
    /**
     * Select mannequin by index
     */
    fun selectMannequin(index: Int) {
        if (index in mannequins.indices) {
            selectedMannequinIndex = index
        }
    }
    
    /**
     * Get currently selected mannequin
     */
    fun getSelectedMannequin(): Mannequin? {
        return mannequins.getOrNull(selectedMannequinIndex)
    }
    
    /**
     * Get selected mannequin index
     */
    fun getSelectedIndex(): Int = selectedMannequinIndex
    
    /**
     * Apply outfit to selected mannequin
     */
    fun applyOutfitToSelected(outfit: Outfit) {
        getSelectedMannequin()?.currentOutfit = outfit
    }
    
    /**
     * Apply matching outfits to all mannequins
     */
    fun applyMatchingOutfits(outfit: Outfit) {
        mannequins.forEach { mannequin ->
            mannequin.currentOutfit = outfit.copy()
        }
    }
    
    /**
     * Clear outfit from selected mannequin
     */
    fun clearOutfitFromSelected() {
        getSelectedMannequin()?.currentOutfit = null
    }
    
    /**
     * Clear all outfits
     */
    fun clearAllOutfits() {
        mannequins.forEach { it.currentOutfit = null }
    }
    
    /**
     * Check if can add more mannequins
     */
    fun canAddMore(): Boolean {
        return mannequins.size < MAX_MANNEQUINS
    }
    
    /**
     * Calculate position for mannequin based on index
     */
    private fun calculatePosition(index: Int): FloatArray {
        val count = index + 1
        val totalWidth = (count - 1) * SPACING
        val startX = -totalWidth / 2f
        
        return floatArrayOf(startX + index * SPACING, 0f, 0f)
    }
    
    /**
     * Reposition all mannequins after removal
     */
    private fun repositionMannequins() {
        mannequins.forEachIndexed { index, mannequin ->
            mannequin.position = calculatePosition(index)
        }
    }
    
    /**
     * Clear all mannequins
     */
    fun clear() {
        mannequins.clear()
        selectedMannequinIndex = 0
    }
}
