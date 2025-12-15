package com.alnasser.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alnasser.app.domain.Gender
import com.alnasser.app.viewmodel.InputViewModel

@Composable
fun InputScreen(onNavigateToMannequin: () -> Unit) {
    val viewModel = remember { InputViewModel.getInstance() }
    
    val height by viewModel.height.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "الناصر",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A3A52)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Enter Your Measurements",
                fontSize = 20.sp,
                color = Color(0xFF424242)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Height input
            OutlinedTextField(
                value = height,
                onValueChange = { viewModel.setHeight(it) },
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1A3A52),
                    focusedLabelColor = Color(0xFF1A3A52)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Weight input
            OutlinedTextField(
                value = weight,
                onValueChange = { viewModel.setWeight(it) },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1A3A52),
                    focusedLabelColor = Color(0xFF1A3A52)
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Gender selection
            Text(
                text = "Gender",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenderButton(
                    text = "Male",
                    selected = gender == Gender.MALE,
                    onClick = { viewModel.setGender(Gender.MALE) },
                    modifier = Modifier.weight(1f)
                )
                
                GenderButton(
                    text = "Female",
                    selected = gender == Gender.FEMALE,
                    onClick = { viewModel.setGender(Gender.FEMALE) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Error message
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Continue button
            Button(
                onClick = {
                    viewModel.predictMeasurements { measurements ->
                        onNavigateToMannequin()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1A3A52)
                ),
                enabled = !isProcessing,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Continue",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isProcessing) {
                Text(
                    text = "AI is predicting your body measurements...",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun GenderButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF1A3A52) else Color.White,
            contentColor = if (selected) Color.White else Color(0xFF1A3A52)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
