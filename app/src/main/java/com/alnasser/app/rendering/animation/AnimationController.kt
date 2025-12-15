package com.alnasser.app.rendering.animation

import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos

/**
 * Controls mannequin animations including walking, turning, idle, and gestures
 */
class AnimationController {
    
    private var currentAnimation: Animation = Animation.IDLE
    private var animationTime = 0f
    private var isPlaying = true
    
    // Animation speeds
    private val idleSpeed = 1.0f
    private val walkSpeed = 1.5f
    private val turnSpeed = 0.5f
    private val gestureSpeed = 1.2f
    
    enum class Animation {
        IDLE,
        WALK,
        TURN,
        WAVE,
        POSE
    }
    
    fun update(deltaTime: Float) {
        if (!isPlaying) return
        
        val speed = when (currentAnimation) {
            Animation.IDLE -> idleSpeed
            Animation.WALK -> walkSpeed
            Animation.TURN -> turnSpeed
            Animation.WAVE -> gestureSpeed
            Animation.POSE -> 0f
        }
        
        animationTime += deltaTime * speed
        
        // Loop animations
        when (currentAnimation) {
            Animation.IDLE -> if (animationTime > 3f) animationTime = 0f
            Animation.WALK -> if (animationTime > 2f) animationTime = 0f
            Animation.TURN -> if (animationTime > 4f) animationTime = 0f
            Animation.WAVE -> if (animationTime > 2f) {
                animationTime = 0f
                currentAnimation = Animation.IDLE
            }
            Animation.POSE -> { /* Static pose */ }
        }
    }
    
    fun setAnimation(animation: Animation) {
        if (currentAnimation != animation) {
            currentAnimation = animation
            animationTime = 0f
        }
    }
    
    fun getCurrentAnimation() = currentAnimation
    
    fun getAnimationTime() = animationTime
    
    /**
     * Get rotation angle for body based on current animation
     */
    fun getBodyRotation(): Float {
        return when (currentAnimation) {
            Animation.TURN -> (animationTime / 4f) * 360f
            Animation.IDLE -> sin(animationTime * 2f * PI.toFloat()) * 3f // Subtle sway
            else -> 0f
        }
    }
    
    /**
     * Get vertical offset for breathing/walking animation
     */
    fun getBodyOffset(): Float {
        return when (currentAnimation) {
            Animation.IDLE -> sin(animationTime * 3f * PI.toFloat()) * 0.01f // Breathing
            Animation.WALK -> sin(animationTime * 4f * PI.toFloat()) * 0.05f // Walking bounce
            else -> 0f
        }
    }
    
    /**
     * Get arm rotation for gestures
     */
    fun getLeftArmRotation(): Float {
        return when (currentAnimation) {
            Animation.WALK -> sin(animationTime * 2f * PI.toFloat()) * 30f
            Animation.WAVE -> {
                if (animationTime < 1f) {
                    sin(animationTime * 4f * PI.toFloat()) * 45f + 45f
                } else {
                    0f
                }
            }
            else -> 0f
        }
    }
    
    fun getRightArmRotation(): Float {
        return when (currentAnimation) {
            Animation.WALK -> -sin(animationTime * 2f * PI.toFloat()) * 30f
            else -> 0f
        }
    }
    
    /**
     * Get leg rotation for walking
     */
    fun getLeftLegRotation(): Float {
        return when (currentAnimation) {
            Animation.WALK -> sin(animationTime * 2f * PI.toFloat()) * 25f
            else -> 0f
        }
    }
    
    fun getRightLegRotation(): Float {
        return when (currentAnimation) {
            Animation.WALK -> -sin(animationTime * 2f * PI.toFloat()) * 25f
            else -> 0f
        }
    }
    
    fun play() {
        isPlaying = true
    }
    
    fun pause() {
        isPlaying = false
    }
    
    fun reset() {
        animationTime = 0f
        currentAnimation = Animation.IDLE
    }
}
