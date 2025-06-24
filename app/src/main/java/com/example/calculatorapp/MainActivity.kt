package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculatorapp.viewmodel.CalculatorViewModel
import com.example.calculatorapp.userInterface.CalculatorScreen
import com.example.calculatorapp.ui.theme.CalculatorAppTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorAppTheme {
                val vm: CalculatorViewModel = viewModel()
                CalculatorScreen(viewModel = vm)
            }
        }
    }
}

