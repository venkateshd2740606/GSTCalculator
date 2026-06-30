package com.gstcalculator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.gstcalculator.domain.engine.GstEngine
import com.gstcalculator.domain.engine.GstMode
import com.gstcalculator.domain.engine.GstResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class GstUiState(
    val mode: GstMode = GstMode.ADD_GST,
    val amount: String = "1000",
    val ratePercent: Double = 18.0,
    val quantity: String = "",
    val showQuantity: Boolean = false,
    val result: GstResult? = null,
    val history: List<GstResult> = emptyList()
)

@HiltViewModel
class GstViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(GstUiState())
    val state = _state.asStateFlow()

    init { recalculate() }

    fun setMode(mode: GstMode) {
        _state.update { it.copy(mode = mode) }
        recalculate()
    }

    fun setAmount(value: String) {
        _state.update { it.copy(amount = value) }
        recalculate()
    }

    fun setRate(rate: Double) {
        _state.update { it.copy(ratePercent = rate) }
        recalculate()
    }

    fun setQuantity(value: String) {
        _state.update { it.copy(quantity = value) }
        recalculate()
    }

    fun setShowQuantity(show: Boolean) {
        _state.update {
            it.copy(showQuantity = show, quantity = if (show) it.quantity else "")
        }
        recalculate()
    }

    private var lastRecordedKey: String? = null

    private fun recalculate() {
        val s = _state.value
        val amount = s.amount.toDoubleOrNull()
        val qty = if (s.showQuantity) (s.quantity.toIntOrNull() ?: 1).coerceAtLeast(1) else 1
        val result = amount?.let { GstEngine.calculate(s.mode, it, s.ratePercent, qty) }
        val key = result?.let { "${it.mode}-${it.unitAmount}-${it.ratePercent}-${it.quantity}" }
        val history = _state.value.history
        val newHistory = if (result != null && key != lastRecordedKey) {
            lastRecordedKey = key
            (listOf(result) + history).take(10)
        } else history
        _state.update { it.copy(result = result, history = newHistory) }
    }

    fun clearHistory() {
        _state.update { it.copy(history = emptyList()) }
    }
}
