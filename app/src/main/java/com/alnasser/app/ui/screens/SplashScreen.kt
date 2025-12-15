package com.alnasser.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alnasser.app.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToInput: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2500) // Show splash for 2.5 seconds
        onNavigateToInput()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A3A52)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App logo
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "Al-Nasser Logo",
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App name in Arabic
            Text(
                text = "الناصر",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // App name in English
            Text(
                text = "Al-Nasser",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Loading text
            Text(
                text = "Virtual Fashion Experience",
                fontSize = 16.sp,
                color = Color(0xFFF4E5C2),
                textAlign = TextAlign.Center
            )
        }
    }
}
