package com.example.calculatorapp.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculatorapp.data.HistoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history

    private val _selectedOperator = MutableStateFlow("+")
    val selectedOperator: StateFlow<String> = _selectedOperator

    fun setOperator(op: String) {
        _selectedOperator.value = op
    }

    init {
        viewModelScope.launch {
            HistoryManager.getHistory(context).collect { saved ->
                _history.value = saved
            }
        }
    }
    fun clearHistory() {
        _history.value = emptyList()
        viewModelScope.launch {
            HistoryManager.clearHistory(context)
        }
    }
    fun calculate(nb1: String, nb2: String, operator: String) {
        val number1 = nb1.toDoubleOrNull()
        val number2 = nb2.toDoubleOrNull()

        if (number1 == null || number2 == null) {
            _result.value = "Veuillez entrer des nombres valides"
            return
        }

        if (operator == "/" && number2 == 0.0) {
            _result.value = "Erreur : division par zéro"
            return
        }

        val res = when (operator) {
            "+" -> number1 + number2
            "-" -> number1 - number2
            "*" -> number1 * number2
            "/" -> number1 / number2
            else -> {
                _result.value = "Opérateur invalide"
                return
            }
        }

        val expression = "$number1 $operator $number2 = $res"
        _result.value = res.toString()

        val updatedHistory = _history.value + expression
        _history.value = updatedHistory

        viewModelScope.launch {
            HistoryManager.saveHistory(context, updatedHistory)
        }
    }
}
