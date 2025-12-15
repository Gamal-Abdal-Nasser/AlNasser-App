package com.alnasser.app.util

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Quaternion(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 1f
) {
    operator fun times(other: Quaternion) = Quaternion(
        w * other.x + x * other.w + y * other.z - z * other.y,
        w * other.y - x * other.z + y * other.w + z * other.x,
        w * other.z + x * other.y - y * other.x + z * other.w,
        w * other.w - x * other.x - y * other.y - z * other.z
    )
    
    fun normalize(): Quaternion {
        val len = sqrt(x * x + y * y + z * z + w * w)
        return if (len > 0f) Quaternion(x / len, y / len, z / len, w / len) else this
    }
    
    companion object {
        val IDENTITY = Quaternion(0f, 0f, 0f, 1f)
        
        fun fromAxisAngle(axis: Vector3, angle: Float): Quaternion {
            val halfAngle = angle * 0.5f
            val s = sin(halfAngle)
            val normalized = axis.normalize()
            return Quaternion(
                normalized.x * s,
                normalized.y * s,
                normalized.z * s,
                cos(halfAngle)
            )
        }
        
        fun fromEuler(pitch: Float, yaw: Float, roll: Float): Quaternion {
            val cy = cos(yaw * 0.5f)
            val sy = sin(yaw * 0.5f)
            val cp = cos(pitch * 0.5f)
            val sp = sin(pitch * 0.5f)
            val cr = cos(roll * 0.5f)
            val sr = sin(roll * 0.5f)
            
            return Quaternion(
                sr * cp * cy - cr * sp * sy,
                cr * sp * cy + sr * cp * sy,
                cr * cp * sy - sr * sp * cy,
                cr * cp * cy + sr * sp * sy
            )
        }
    }
}
