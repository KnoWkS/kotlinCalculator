package com.example.calculatorapp.userInterface

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calculatorapp.viewmodel.CalculatorViewModel

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    var input1 by remember { mutableStateOf("") }
    var input2 by remember { mutableStateOf("") }

    val result by viewModel.result.collectAsState()
    val history by viewModel.history.collectAsState()
    val selectedOperator by viewModel.selectedOperator.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Simple Calculator", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = input1,
            onValueChange = { input1 = it },
            label = { Text("Nombre 1") },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground)
        )
        OperatorDropdown(viewModel)

        OutlinedTextField(
            value = input2,
            onValueChange = { input2 = it },
            label = { Text("Nombre 2") },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground)
        )


        Button(
            onClick = {
                val temp = input1
                input1 = input2
                input2 = temp
            }
        ) {
            Text("Inverser les nombres")
        }

        Button(onClick = {
            viewModel.calculate(input1, input2, selectedOperator)
        }) {
            Text("Calculer")
        }

        Text(
            text = "Résultat : $result",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.clearHistory() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Effacer l'historique", color = MaterialTheme.colorScheme.onError)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Historique des calculs",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {
                    items(history.reversed()) { entry ->
                        Text(
                            text = entry,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }


        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmation") },
                text = { Text("Es-tu sûr de vouloir supprimer l'historique ?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearHistory()
                            showDialog = false
                        }
                    ) {
                        Text("Oui")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}

@Composable
fun OperatorDropdown(viewModel: CalculatorViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOperator by viewModel.selectedOperator.collectAsState()
    val operators = listOf("+", "-", "*", "/")

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedOperator)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            operators.forEach { op ->
                DropdownMenuItem(
                    text = { Text(op) },
                    onClick = {
                        viewModel.setOperator(op)
                        expanded = false
                    }
                )
            }
        }
    }
}

