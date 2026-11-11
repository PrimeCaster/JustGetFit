package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.FitnessRepository
import com.example.ui.screens.MainAppScreen
import com.example.ui.theme.JustGetFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val repository = remember { FitnessRepository() }
            val isDarkTheme by repository.isDarkTheme.collectAsState()
            val currentColorTheme by repository.currentColorTheme.collectAsState()

            JustGetFitTheme(darkTheme = isDarkTheme, colorTheme = currentColorTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainAppScreen(repository = repository)
                }
            }
        }
    }
}
