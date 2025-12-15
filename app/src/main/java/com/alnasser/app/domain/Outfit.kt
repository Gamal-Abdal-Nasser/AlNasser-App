package com.alnasser.app.domain

data class Outfit(
    var top: ClothingItem? = null,
    var bottom: ClothingItem? = null,
    var shoes: ClothingItem? = null,
    var accessories: List<ClothingItem> = emptyList()
) {
    fun isEmpty(): Boolean {
        return top == null && bottom == null && shoes == null && accessories.isEmpty()
    }
    
    fun getTotalPrice(): Float {
        var total = 0f
        top?.let { total += it.price }
        bottom?.let { total += it.price }
        shoes?.let { total += it.price }
        accessories.forEach { total += it.price }
        return total
    }
    
    fun copy(): Outfit {
        return Outfit(
            top = top,
            bottom = bottom,
            shoes = shoes,
            accessories = accessories.toList()
        )
    }
    
    fun clear() {
        top = null
        bottom = null
        shoes = null
        accessories = emptyList()
    }
}
