package com.alnasser.app.rendering.engine

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.alnasser.app.domain.BodyMeasurements
import com.alnasser.app.rendering.animation.AnimationController
import com.alnasser.app.rendering.mannequin.MannequinMesh
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * OpenGL ES 3.0 renderer for 3D mannequin visualization
 */
class MannequinRenderer : GLSurfaceView.Renderer {
    
    private val mannequins = mutableListOf<MannequinData>()
    private val animationControllers = mutableListOf<AnimationController>()
    
    // Matrices
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private val tempMatrix = FloatArray(16)
    
    // Camera - made public for gesture control
    var cameraDistance = 3.0f
    var cameraRotationX = 0f
    var cameraRotationY = 15f
    
    // Shader program
    private var shaderProgram = 0
    private var positionHandle = 0
    private var colorHandle = 0
    private var mvpMatrixHandle = 0
    
    // Vertex shader code
    private val vertexShaderCode = """
        #version 300 es
        uniform mat4 uMVPMatrix;
        in vec3 aPosition;
        in vec4 aColor;
        out vec4 vColor;
        
        void main() {
            gl_Position = uMVPMatrix * vec4(aPosition, 1.0);
            vColor = aColor;
        }
    """.trimIndent()
    
    // Fragment shader code
    private val fragmentShaderCode = """
        #version 300 es
        precision mediump float;
        in vec4 vColor;
        out vec4 fragColor;
        
        void main() {
            fragColor = vColor;
        }
    """.trimIndent()
    
    data class MannequinData(
        val measurements: BodyMeasurements,
        var vertexBuffer: FloatBuffer,
        var colorBuffer: FloatBuffer,
        var indexBuffer: ShortBuffer,
        var indexCount: Int,
        var position: FloatArray = floatArrayOf(0f, 0f, 0f),
        var rotation: Float = 0f
    )
    
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Set background color
        GLES30.glClearColor(0.95f, 0.95f, 0.97f, 1.0f)
        
        // Enable depth testing
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glDepthFunc(GLES30.GL_LEQUAL)
        
        // Enable face culling
        GLES30.glEnable(GLES30.GL_CULL_FACE)
        GLES30.glCullFace(GLES30.GL_BACK)
        
        // Create shader program
        shaderProgram = createShaderProgram(vertexShaderCode, fragmentShaderCode)
        
        // Get attribute locations
        positionHandle = GLES30.glGetAttribLocation(shaderProgram, "aPosition")
        colorHandle = GLES30.glGetAttribLocation(shaderProgram, "aColor")
        mvpMatrixHandle = GLES30.glGetUniformLocation(shaderProgram, "uMVPMatrix")
    }
    
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        
        val ratio = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 10f)
    }
    
    override fun onDrawFrame(gl: GL10?) {
        // Clear buffers
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        
        // Update animations
        val deltaTime = 0.016f // ~60 FPS
        animationControllers.forEach { it.update(deltaTime) }
        
        // Set up view matrix
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 1.5f, cameraDistance,  // Camera position
            0f, 1.5f, 0f,              // Look at point
            0f, 1f, 0f                 // Up vector
        )
        
        // Apply camera rotation
        Matrix.rotateM(viewMatrix, 0, cameraRotationY, 1f, 0f, 0f)
        Matrix.rotateM(viewMatrix, 0, cameraRotationX, 0f, 1f, 0f)
        
        // Use shader program
        GLES30.glUseProgram(shaderProgram)
        
        // Draw each mannequin
        mannequins.forEachIndexed { index, mannequin ->
            drawMannequin(mannequin, if (index < animationControllers.size) animationControllers[index] else null)
        }
    }
    
    private fun drawMannequin(mannequin: MannequinData, animator: AnimationController?) {
        // Set up model matrix
        Matrix.setIdentityM(modelMatrix, 0)
        
        // Apply position
        Matrix.translateM(modelMatrix, 0, mannequin.position[0], mannequin.position[1], mannequin.position[2])
        
        // Apply animation rotation
        val animRotation = animator?.getBodyRotation() ?: 0f
        Matrix.rotateM(modelMatrix, 0, mannequin.rotation + animRotation, 0f, 1f, 0f)
        
        // Apply animation offset
        val animOffset = animator?.getBodyOffset() ?: 0f
        Matrix.translateM(modelMatrix, 0, 0f, animOffset, 0f)
        
        // Calculate MVP matrix
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0)
        
        // Set MVP matrix
        GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)
        
        // Set vertex positions
        mannequin.vertexBuffer.position(0)
        GLES30.glEnableVertexAttribArray(positionHandle)
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 0, mannequin.vertexBuffer)
        
        // Set colors
        mannequin.colorBuffer.position(0)
        GLES30.glEnableVertexAttribArray(colorHandle)
        GLES30.glVertexAttribPointer(colorHandle, 4, GLES30.GL_FLOAT, false, 0, mannequin.colorBuffer)
        
        // Draw
        mannequin.indexBuffer.position(0)
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, mannequin.indexCount, GLES30.GL_UNSIGNED_SHORT, mannequin.indexBuffer)
        
        // Disable vertex arrays
        GLES30.glDisableVertexAttribArray(positionHandle)
        GLES30.glDisableVertexAttribArray(colorHandle)
    }
    
    fun addMannequin(measurements: BodyMeasurements, position: FloatArray = floatArrayOf(0f, 0f, 0f)) {
        val mesh = MannequinMesh(measurements)
        
        val vertexBuffer = createFloatBuffer(mesh.getVertices())
        val colorBuffer = createFloatBuffer(mesh.getColors())
        val indexBuffer = createShortBuffer(mesh.getIndices())
        
        val mannequinData = MannequinData(
            measurements = measurements,
            vertexBuffer = vertexBuffer,
            colorBuffer = colorBuffer,
            indexBuffer = indexBuffer,
            indexCount = mesh.getIndices().size,
            position = position
        )
        
        mannequins.add(mannequinData)
        animationControllers.add(AnimationController())
    }
    
    fun removeMannequin(index: Int) {
        if (index in mannequins.indices) {
            mannequins.removeAt(index)
            if (index < animationControllers.size) {
                animationControllers.removeAt(index)
            }
        }
    }
    
    fun getMannequinCount() = mannequins.size
    
    fun setAnimation(index: Int, animation: AnimationController.Animation) {
        if (index in animationControllers.indices) {
            animationControllers[index].setAnimation(animation)
        }
    }
    
    fun setCameraRotation(rotationX: Float, rotationY: Float) {
        cameraRotationX = rotationX
        cameraRotationY = rotationY.coerceIn(-89f, 89f)
    }
    
    fun setCameraDistance(distance: Float) {
        cameraDistance = distance.coerceIn(1.5f, 6f)
    }
    
    private fun createShaderProgram(vertexCode: String, fragmentCode: String): Int {
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexCode)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentCode)
        
        return GLES30.glCreateProgram().also { program ->
            GLES30.glAttachShader(program, vertexShader)
            GLES30.glAttachShader(program, fragmentShader)
            GLES30.glLinkProgram(program)
        }
    }
    
    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES30.glCreateShader(type).also { shader ->
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
        }
    }
    
    private fun createFloatBuffer(data: FloatArray): FloatBuffer {
        return ByteBuffer.allocateDirect(data.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(data)
                position(0)
            }
        }
    }
    
    private fun createShortBuffer(data: ShortArray): ShortBuffer {
        return ByteBuffer.allocateDirect(data.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(data)
                position(0)
            }
        }
    }
}
