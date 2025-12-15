package com.alnasser.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alnasser.app.ai.BodyPredictionModel
import com.alnasser.app.domain.BodyMeasurements
import com.alnasser.app.domain.Gender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InputViewModel : ViewModel() {
    
    private val predictionModel = BodyPredictionModel()
    
    private val _height = MutableStateFlow("")
    val height: StateFlow<String> = _height.asStateFlow()
    
    private val _weight = MutableStateFlow("")
    val weight: StateFlow<String> = _weight.asStateFlow()
    
    private val _gender = MutableStateFlow(Gender.MALE)
    val gender: StateFlow<Gender> = _gender.asStateFlow()
    
    private val _measurements = MutableStateFlow<BodyMeasurements?>(null)
    val measurements: StateFlow<BodyMeasurements?> = _measurements.asStateFlow()
    
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    fun setHeight(value: String) {
        _height.value = value
        _errorMessage.value = null
    }
    
    fun setWeight(value: String) {
        _weight.value = value
        _errorMessage.value = null
    }
    
    fun setGender(value: Gender) {
        _gender.value = value
    }
    
    fun predictMeasurements(onSuccess: (BodyMeasurements) -> Unit) {
        viewModelScope.launch {
            _isProcessing.value = true
            _errorMessage.value = null
            
            try {
                val heightValue = _height.value.toFloatOrNull()
                val weightValue = _weight.value.toFloatOrNull()
                
                if (heightValue == null || weightValue == null) {
                    _errorMessage.value = "Please enter valid height and weight"
                    _isProcessing.value = false
                    return@launch
                }
                
                if (!predictionModel.validateInput(heightValue, weightValue)) {
                    _errorMessage.value = "Please enter realistic measurements (Height: 140-220cm, Weight: 40-200kg)"
                    _isProcessing.value = false
                    return@launch
                }
                
                // Simulate AI processing delay
                kotlinx.coroutines.delay(1500)
                
                val predicted = predictionModel.predictMeasurements(
                    heightValue,
                    weightValue,
                    _gender.value
                )
                
                _measurements.value = predicted
                _isProcessing.value = false
                onSuccess(predicted)
                
            } catch (e: Exception) {
                _errorMessage.value = "Error processing measurements: ${e.message}"
                _isProcessing.value = false
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    companion object {
        private var instance: InputViewModel? = null
        
        fun getInstance(): InputViewModel {
            if (instance == null) {
                instance = InputViewModel()
            }
            return instance!!
        }
    }
}
