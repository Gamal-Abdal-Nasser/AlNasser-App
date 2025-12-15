package com.alnasser.app.domain

import java.util.UUID

data class Mannequin(
    val id: String = UUID.randomUUID().toString(),
    val measurements: BodyMeasurements,
    var currentOutfit: Outfit? = null,
    var position: FloatArray = floatArrayOf(0f, 0f, 0f),
    var rotation: Float = 0f
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as Mannequin
        return id == other.id
    }
    
    override fun hashCode(): Int {
        return id.hashCode()
    }
}
