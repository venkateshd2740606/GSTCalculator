package com.gstcalculator.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gstcalculator.domain.engine.GstEngine
import com.gstcalculator.domain.engine.GstMode
import com.gstcalculator.domain.engine.GstResult
import com.gstcalculator.presentation.viewmodel.GstViewModel
import com.gstcalculator.util.FormatUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(viewModel: GstViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("GST Calculator") }) }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Calculation Mode", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = state.mode == GstMode.ADD_GST,
                                onClick = { viewModel.setMode(GstMode.ADD_GST) },
                                label = { Text("Add GST") },
                                leadingIcon = if (state.mode == GstMode.ADD_GST) {
                                    { Icon(Icons.Default.Add, null, Modifier.size(18.dp)) }
                                } else null
                            )
                            FilterChip(
                                selected = state.mode == GstMode.REMOVE_GST,
                                onClick = { viewModel.setMode(GstMode.REMOVE_GST) },
                                label = { Text("Remove GST") },
                                leadingIcon = if (state.mode == GstMode.REMOVE_GST) {
                                    { Icon(Icons.Default.Remove, null, Modifier.size(18.dp)) }
                                } else null
                            )
                        }
                        Text(
                            if (state.mode == GstMode.ADD_GST) "Base amount → total with GST"
                            else "Inclusive amount → base (reverse calculation)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Inputs", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = state.amount,
                            onValueChange = viewModel::setAmount,
                            label = {
                                Text(
                                    if (state.mode == GstMode.ADD_GST) "Base Amount (₹)" else "Inclusive Amount (₹)"
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Text("GST Rate", style = MaterialTheme.typography.labelLarge)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GstEngine.RATES.forEach { rate ->
                                FilterChip(
                                    selected = state.ratePercent == rate,
                                    onClick = { viewModel.setRate(rate) },
                                    label = { Text("${rate.toInt()}%") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Quantity", modifier = Modifier.weight(1f))
                            Switch(checked = state.showQuantity, onCheckedChange = viewModel::setShowQuantity)
                        }
                        if (state.showQuantity) {
                            OutlinedTextField(
                                value = state.quantity,
                                onValueChange = viewModel::setQuantity,
                                label = { Text("Quantity") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                placeholder = { Text("1") }
                            )
                        }
                    }
                }
            }
            item {
                state.result?.let { GstResultCard(it) }
                    ?: Text("Enter a valid amount", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateGuideScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("GST Rate Guide") }) }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "India GST slabs — what each rate typically covers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(GstEngine.RATE_GUIDE) { (rate, description) ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                "${rate.toInt()}%",
                                Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            item {
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("CGST + SGST", fontWeight = FontWeight.Bold)
                        Text(
                            "For intra-state supply, GST is split equally into CGST (Central) and SGST (State). " +
                                "IGST applies for inter-state supply.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: GstViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                actions = {
                    if (state.history.isNotEmpty()) {
                        IconButton(onClick = viewModel::clearHistory) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear history")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (state.history.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.History, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(Modifier.height(8.dp))
                    Text("No calculations yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.history) { entry -> HistoryCard(entry) }
            }
        }
    }
}

@Composable
private fun GstResultCard(result: GstResult) {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Results", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (result.quantity > 1) {
                ResultLine("Quantity", "${result.quantity}")
                ResultLine("Per unit", FormatUtils.currency(result.unitAmount))
            }
            ResultLine("Base Amount", FormatUtils.currency(result.baseAmount))
            ResultLine("GST (${result.ratePercent.toInt()}%)", FormatUtils.currency(result.gstAmount))
            HorizontalDivider()
            ResultLine("CGST (${result.ratePercent / 2}%)", FormatUtils.currency(result.cgst))
            ResultLine("SGST (${result.ratePercent / 2}%)", FormatUtils.currency(result.sgst))
            HorizontalDivider()
            ResultLine(
                "Total Amount",
                FormatUtils.currency(result.totalAmount),
                bold = true
            )
        }
    }
}

@Composable
private fun HistoryCard(entry: GstResult) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(entry.summary, fontWeight = FontWeight.SemiBold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Base: ${FormatUtils.currency(entry.baseAmount)}", style = MaterialTheme.typography.bodySmall)
                Text("Total: ${FormatUtils.currency(entry.totalAmount)}", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                "CGST ${FormatUtils.currency(entry.cgst)} + SGST ${FormatUtils.currency(entry.sgst)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ResultLine(label: String, value: String, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text(value, fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold)
    }
}
