package com.gstcalculator.domain.engine

enum class GstMode {
    ADD_GST,
    REMOVE_GST
}

data class GstResult(
    val mode: GstMode,
    val ratePercent: Double,
    val quantity: Int,
    val unitAmount: Double,
    val baseAmount: Double,
    val gstAmount: Double,
    val totalAmount: Double,
    val cgst: Double,
    val sgst: Double
) {
    val summary: String
        get() = buildString {
            append(
                when (mode) {
                    GstMode.ADD_GST -> "Add GST @ ${ratePercent.toInt()}%"
                    GstMode.REMOVE_GST -> "Remove GST @ ${ratePercent.toInt()}%"
                }
            )
            if (quantity > 1) append(" × $quantity")
        }
}

object GstEngine {
    val RATES = listOf(0.0, 5.0, 12.0, 18.0, 28.0)

    val RATE_GUIDE = listOf(
        0.0 to "Essential goods — grains, fresh milk, books, healthcare",
        5.0 to "Household necessities — sugar, tea, edible oil, medicines",
        12.0 to "Processed foods — butter, cheese, mobile phones",
        18.0 to "Standard rate — most goods & services, IT, restaurants",
        28.0 to "Luxury & sin goods — cars, AC, tobacco, aerated drinks"
    )

    fun calculate(mode: GstMode, unitAmount: Double, ratePercent: Double, quantity: Int): GstResult? {
        if (unitAmount < 0 || quantity <= 0) return null
        return when (mode) {
            GstMode.ADD_GST -> addGst(unitAmount, ratePercent, quantity)
            GstMode.REMOVE_GST -> removeGst(unitAmount, ratePercent, quantity)
        }
    }

    fun addGst(unitBase: Double, ratePercent: Double, quantity: Int): GstResult {
        val base = unitBase * quantity
        val gst = base * ratePercent / 100.0
        val total = base + gst
        val halfGst = gst / 2.0
        return GstResult(
            mode = GstMode.ADD_GST,
            ratePercent = ratePercent,
            quantity = quantity,
            unitAmount = unitBase,
            baseAmount = base,
            gstAmount = gst,
            totalAmount = total,
            cgst = halfGst,
            sgst = halfGst
        )
    }

    fun removeGst(unitInclusive: Double, ratePercent: Double, quantity: Int): GstResult {
        val total = unitInclusive * quantity
        val base = total / (1.0 + ratePercent / 100.0)
        val gst = total - base
        val halfGst = gst / 2.0
        return GstResult(
            mode = GstMode.REMOVE_GST,
            ratePercent = ratePercent,
            quantity = quantity,
            unitAmount = unitInclusive,
            baseAmount = base,
            gstAmount = gst,
            totalAmount = total,
            cgst = halfGst,
            sgst = halfGst
        )
    }
}
