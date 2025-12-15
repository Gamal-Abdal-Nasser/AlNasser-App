package com.alnasser.app.rendering.mannequin

import com.alnasser.app.domain.BodyMeasurements
import com.alnasser.app.domain.Gender
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Generates parametric 3D mesh for human mannequin based on body measurements
 * Uses simplified body segments: head, torso, arms, legs
 */
class MannequinMesh(private val measurements: BodyMeasurements) {
    
    private val vertices = mutableListOf<Float>()
    private val normals = mutableListOf<Float>()
    private val indices = mutableListOf<Short>()
    private val colors = mutableListOf<Float>()
    
    // Normalized proportions (total height = 1.0)
    private val headHeight = 0.12f
    private val torsoHeight = 0.35f
    private val armLength = 0.38f
    private val legLength = 0.47f
    
    // Scale factor to convert to actual size
    private val scale = measurements.height / 100f
    
    init {
        generateMesh()
    }
    
    private fun generateMesh() {
        // Generate body parts
        generateHead()
        generateTorso()
        generateArms()
        generateLegs()
    }
    
    private fun generateHead() {
        val headRadius = 0.08f * scale
        val headY = (legLength + torsoHeight + headHeight * 0.5f) * scale
        
        // Generate sphere for head
        generateSphere(0f, headY, 0f, headRadius, 16, 16, floatArrayOf(0.95f, 0.85f, 0.75f, 1f))
    }
    
    private fun generateTorso() {
        val torsoBottom = legLength * scale
        val torsoTop = (legLength + torsoHeight) * scale
        
        // Chest width from measurements
        val chestWidth = (measurements.chest / (2f * PI.toFloat())) * scale * 0.8f
        val waistWidth = (measurements.waist / (2f * PI.toFloat())) * scale * 0.8f
        val shoulderWidth = measurements.shoulderWidth / 100f * scale
        
        // Generate tapered cylinder for torso
        generateTaperedCylinder(
            0f, torsoBottom, 0f,
            waistWidth, chestWidth,
            torsoHeight * scale,
            16,
            floatArrayOf(0.9f, 0.8f, 0.7f, 1f)
        )
    }
    
    private fun generateArms() {
        val shoulderY = (legLength + torsoHeight * 0.9f) * scale
        val shoulderOffset = measurements.shoulderWidth / 200f * scale
        val armRadius = 0.03f * scale
        val armLen = measurements.armLength / 100f * scale
        
        // Left arm
        generateCylinder(
            -shoulderOffset, shoulderY, 0f,
            armRadius,
            armLen,
            12,
            floatArrayOf(0.95f, 0.85f, 0.75f, 1f),
            isVertical = false,
            angleX = -30f
        )
        
        // Right arm
        generateCylinder(
            shoulderOffset, shoulderY, 0f,
            armRadius,
            armLen,
            12,
            floatArrayOf(0.95f, 0.85f, 0.75f, 1f),
            isVertical = false,
            angleX = 30f
        )
    }
    
    private fun generateLegs() {
        val hipWidth = (measurements.hips / (2f * PI.toFloat())) * scale * 0.4f
        val legRadius = 0.04f * scale
        val legLen = measurements.legLength / 100f * scale
        
        // Left leg
        generateCylinder(
            -hipWidth, legLen, 0f,
            legRadius,
            legLen,
            12,
            floatArrayOf(0.9f, 0.8f, 0.7f, 1f),
            isVertical = true
        )
        
        // Right leg
        generateCylinder(
            hipWidth, legLen, 0f,
            legRadius,
            legLen,
            12,
            floatArrayOf(0.9f, 0.8f, 0.7f, 1f),
            isVertical = true
        )
    }
    
    private fun generateSphere(
        cx: Float, cy: Float, cz: Float,
        radius: Float,
        stacks: Int, slices: Int,
        color: FloatArray
    ) {
        val startIndex = (vertices.size / 3).toShort()
        
        for (i in 0..stacks) {
            val phi = PI * i / stacks
            for (j in 0..slices) {
                val theta = 2 * PI * j / slices
                
                val x = (radius * sin(phi) * cos(theta)).toFloat()
                val y = (radius * cos(phi)).toFloat()
                val z = (radius * sin(phi) * sin(theta)).toFloat()
                
                vertices.addAll(listOf(cx + x, cy + y, cz + z))
                normals.addAll(listOf(x / radius, y / radius, z / radius))
                colors.addAll(color.toList())
            }
        }
        
        // Generate indices
        for (i in 0 until stacks) {
            for (j in 0 until slices) {
                val first = (startIndex + i * (slices + 1) + j).toShort()
                val second = (first + slices + 1).toShort()
                
                indices.addAll(listOf(first, second, (first + 1).toShort()))
                indices.addAll(listOf(second, (second + 1).toShort(), (first + 1).toShort()))
            }
        }
    }
    
    private fun generateCylinder(
        cx: Float, cy: Float, cz: Float,
        radius: Float,
        height: Float,
        segments: Int,
        color: FloatArray,
        isVertical: Boolean = true,
        angleX: Float = 0f
    ) {
        val startIndex = (vertices.size / 3).toShort()
        val angleRad = angleX * PI.toFloat() / 180f
        
        for (i in 0..segments) {
            val theta = 2 * PI * i / segments
            val x = (radius * cos(theta)).toFloat()
            val z = (radius * sin(theta)).toFloat()
            
            // Bottom circle
            var px = cx + x
            var py = cy
            var pz = cz + z
            
            if (!isVertical) {
                // Rotate for horizontal orientation
                val tempY = py
                py = tempY * cos(angleRad) - height * 0.5f * sin(angleRad)
                px += height * 0.5f * cos(angleRad)
            }
            
            vertices.addAll(listOf(px, py, pz))
            normals.addAll(listOf(x / radius, 0f, z / radius))
            colors.addAll(color.toList())
            
            // Top circle
            px = cx + x
            py = if (isVertical) cy - height else cy
            pz = cz + z
            
            if (!isVertical) {
                val tempY = cy
                py = tempY * cos(angleRad) + height * 0.5f * sin(angleRad)
                px += -height * 0.5f * cos(angleRad)
            }
            
            vertices.addAll(listOf(px, py, pz))
            normals.addAll(listOf(x / radius, 0f, z / radius))
            colors.addAll(color.toList())
        }
        
        // Generate indices
        for (i in 0 until segments) {
            val first = (startIndex + i * 2).toShort()
            val second = (first + 2).toShort()
            
            indices.addAll(listOf(first, (first + 1).toShort(), second))
            indices.addAll(listOf(second, (first + 1).toShort(), (second + 1).toShort()))
        }
    }
    
    private fun generateTaperedCylinder(
        cx: Float, cy: Float, cz: Float,
        bottomRadius: Float,
        topRadius: Float,
        height: Float,
        segments: Int,
        color: FloatArray
    ) {
        val startIndex = (vertices.size / 3).toShort()
        
        for (i in 0..segments) {
            val theta = 2 * PI * i / segments
            
            // Bottom circle
            val xBottom = (bottomRadius * cos(theta)).toFloat()
            val zBottom = (bottomRadius * sin(theta)).toFloat()
            vertices.addAll(listOf(cx + xBottom, cy, cz + zBottom))
            normals.addAll(listOf(xBottom / bottomRadius, 0f, zBottom / bottomRadius))
            colors.addAll(color.toList())
            
            // Top circle
            val xTop = (topRadius * cos(theta)).toFloat()
            val zTop = (topRadius * sin(theta)).toFloat()
            vertices.addAll(listOf(cx + xTop, cy + height, cz + zTop))
            normals.addAll(listOf(xTop / topRadius, 0f, zTop / topRadius))
            colors.addAll(color.toList())
        }
        
        // Generate indices
        for (i in 0 until segments) {
            val first = (startIndex + i * 2).toShort()
            val second = (first + 2).toShort()
            
            indices.addAll(listOf(first, (first + 1).toShort(), second))
            indices.addAll(listOf(second, (first + 1).toShort(), (second + 1).toShort()))
        }
    }
    
    fun getVertices() = vertices.toFloatArray()
    fun getNormals() = normals.toFloatArray()
    fun getIndices() = indices.toShortArray()
    fun getColors() = colors.toFloatArray()
}
