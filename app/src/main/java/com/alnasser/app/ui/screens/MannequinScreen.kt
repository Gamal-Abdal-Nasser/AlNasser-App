package com.alnasser.app.ui.screens

import android.opengl.GLSurfaceView
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.alnasser.app.domain.ClothingCategory
import com.alnasser.app.domain.ClothingItem
import com.alnasser.app.rendering.animation.AnimationController
import com.alnasser.app.rendering.engine.MannequinRenderer
import com.alnasser.app.viewmodel.InputViewModel
import com.alnasser.app.viewmodel.MannequinViewModel
import kotlin.math.abs

@Composable
fun MannequinScreen() {
    val context = LocalContext.current
    val viewModel = remember { MannequinViewModel.getInstance(context) }
    val inputViewModel = remember { InputViewModel.getInstance() }
    
    val measurements by inputViewModel.measurements.collectAsState()
    val mannequins by viewModel.mannequins.collectAsState()
    val canAddMore by viewModel.canAddMoreMannequins.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    
    var renderer: MannequinRenderer? by remember { mutableStateOf(null) }
    var showClothingPanel by remember { mutableStateOf(false) }
    
    // Initialize mannequin with measurements
    LaunchedEffect(measurements) {
        measurements?.let { m ->
            if (mannequins.isEmpty()) {
                viewModel.initializeWithMeasurements(m)
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // 3D OpenGL View
        AndroidView(
            factory = { ctx ->
                GLSurfaceView(ctx).apply {
                    setEGLContextClientVersion(3)
                    val glRenderer = MannequinRenderer()
                    renderer = glRenderer
                    setRenderer(glRenderer)
                    renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                    
                    // Add initial mannequin
                    measurements?.let { m ->
                        glRenderer.addMannequin(m)
                    }
                    
                    // Touch gesture handling
                    var lastX = 0f
                    var lastY = 0f
                    var initialDistance = 0f
                    
                    setOnTouchListener { _, event ->
                        when (event.actionMasked) {
                            MotionEvent.ACTION_DOWN -> {
                                lastX = event.x
                                lastY = event.y
                            }
                            MotionEvent.ACTION_MOVE -> {
                                if (event.pointerCount == 1) {
                                    // Rotation
                                    val dx = event.x - lastX
                                    val dy = event.y - lastY
                                    glRenderer.setCameraRotation(
                                        glRenderer.cameraRotationX + dx * 0.5f,
                                        glRenderer.cameraRotationY + dy * 0.5f
                                    )
                                    lastX = event.x
                                    lastY = event.y
                                } else if (event.pointerCount == 2) {
                                    // Zoom
                                    val dx = event.getX(1) - event.getX(0)
                                    val dy = event.getY(1) - event.getY(0)
                                    val distance = kotlin.math.sqrt(dx * dx + dy * dy)
                                    
                                    if (initialDistance == 0f) {
                                        initialDistance = distance
                                    } else {
                                        val scale = distance / initialDistance
                                        glRenderer.setCameraDistance(
                                            glRenderer.cameraDistance / scale
                                        )
                                        initialDistance = distance
                                    }
                                }
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                                initialDistance = 0f
                            }
                        }
                        true
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Top bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0xCC1A3A52))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الناصر",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37)
                )
                
                // Add mannequin button
                if (canAddMore) {
                    FloatingActionButton(
                        onClick = {
                            viewModel.addMannequin()
                            measurements?.let { m ->
                                renderer?.addMannequin(m, floatArrayOf(mannequins.size * 0.8f, 0f, 0f))
                            }
                        },
                        containerColor = Color(0xFFD4AF37),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Mannequin",
                            tint = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${mannequins.size} Mannequin(s)",
                fontSize = 14.sp,
                color = Color.White
            )
        }
        
        // Bottom control panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Animation controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AnimationButton("Idle", AnimationController.Animation.IDLE) { anim ->
                    viewModel.setAnimation(anim)
                    renderer?.setAnimation(viewModel.selectedMannequinIndex.value, anim)
                }
                AnimationButton("Walk", AnimationController.Animation.WALK) { anim ->
                    viewModel.setAnimation(anim)
                    renderer?.setAnimation(viewModel.selectedMannequinIndex.value, anim)
                }
                AnimationButton("Turn", AnimationController.Animation.TURN) { anim ->
                    viewModel.setAnimation(anim)
                    renderer?.setAnimation(viewModel.selectedMannequinIndex.value, anim)
                }
                AnimationButton("Wave", AnimationController.Animation.WAVE) { anim ->
                    viewModel.setAnimation(anim)
                    renderer?.setAnimation(viewModel.selectedMannequinIndex.value, anim)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Try clothes button
            Button(
                onClick = { showClothingPanel = !showClothingPanel },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1A3A52)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (showClothingPanel) "Hide Clothing" else "Try Clothes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Clothing panel
        if (showClothingPanel) {
            ClothingPanel(
                viewModel = viewModel,
                onClose = { showClothingPanel = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun AnimationButton(
    text: String,
    animation: AnimationController.Animation,
    onClick: (AnimationController.Animation) -> Unit
) {
    Button(
        onClick = { onClick(animation) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFD4AF37)
        ),
        modifier = Modifier
            .height(40.dp)
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ClothingPanel(
    viewModel: MannequinViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val clothingItems = viewModel.getFilteredClothingItems()
    
    Surface(
        modifier = modifier,
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Clothing",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A3A52)
                )
                
                TextButton(onClick = onClose) {
                    Text("Close", color = Color(0xFF1A3A52))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category filters
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    CategoryChip(
                        text = "All",
                        selected = selectedCategory == null,
                        onClick = { viewModel.selectCategory(null) }
                    )
                }
                item {
                    CategoryChip(
                        text = "Tops",
                        selected = selectedCategory == ClothingCategory.TOPS,
                        onClick = { viewModel.selectCategory(ClothingCategory.TOPS) }
                    )
                }
                item {
                    CategoryChip(
                        text = "Bottoms",
                        selected = selectedCategory == ClothingCategory.BOTTOMS,
                        onClick = { viewModel.selectCategory(ClothingCategory.BOTTOMS) }
                    )
                }
                item {
                    CategoryChip(
                        text = "Shoes",
                        selected = selectedCategory == ClothingCategory.SHOES,
                        onClick = { viewModel.selectCategory(ClothingCategory.SHOES) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Clothing items list
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(clothingItems) { item ->
                    ClothingItemCard(
                        item = item,
                        recommendedSize = viewModel.getRecommendedSize(item),
                        onClick = { viewModel.applyClothingItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (selected) Color(0xFF1A3A52) else Color(0xFFE0E0E0),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ClothingItemCard(
    item: ClothingItem,
    recommendedSize: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Placeholder for item image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "$${item.price}",
                fontSize = 12.sp,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Size: $recommendedSize",
                fontSize = 11.sp,
                color = Color(0xFF666666)
            )
            
            if (!item.isAvailable) {
                Text(
                    text = "Custom-made",
                    fontSize = 10.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
