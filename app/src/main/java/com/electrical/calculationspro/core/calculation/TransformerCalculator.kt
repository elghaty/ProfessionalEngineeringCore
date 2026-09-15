package com.electrical.calculationspro.core.calculation

import com.electrical.calculationspro.core.model.TransformerInput
import com.electrical.calculationspro.core.model.TransformerResult
import kotlin.math.sqrt

data class TransformerDetailedInput(
    val ratingKva: Double,
    val primaryVoltageV: Double,
    val secondaryVoltageV: Double,
    val impedancePercent: Double,
    val frequencyHz: Double = 50.0,
    val powerFactor: Double = 0.90,
    val noLoadLossKw: Double = 0.0,
    val loadLossKw: Double = 0.0,
    val loadPercent: Double = 100.0,
    val tapPercent: Double = 0.0
)

data class TransformerDetailedResult(
    val primaryCurrentA: Double,
    val secondaryCurrentA: Double,
    val shortCircuitCurrentKA: Double,
    val faultMva: Double,
    val inputPowerKw: Double,
    val outputPowerKw: Double,
    val totalLossKw: Double,
    val efficiencyPercent: Double,
    val approximateRegulationPercent: Double,
    val primaryImpedanceOhm: Double,
    val secondaryImpedanceOhm: Double,
    val turnsRatio: Double,
    val notes: List<String>
)

class TransformerCalculator {

    fun calculate(
        input: TransformerInput
    ): TransformerResult {

        validate(input)

        val primaryCurrent =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) * input.primaryVoltageV)

        val secondaryCurrent =
            input.ratingKva * 1000.0 /
                (sqrt(3.0) * input.secondaryVoltageV)

        val shortCircuitCurrent =
            secondaryCurrent /
                (input.impedancePercent / 100.0)

        val faultMva =
            sqrt(3.0) *
                input.secondaryVoltageV *
                shortCircuitCurrent /
                1_000_000.0

        return TransformerResult(
            primaryCurrentA = primaryCurrent,
            secondaryCurrentA = secondaryCurrent,
            shortCircuitCurrentKA =
                shortCircuitCurrent / 1000.0,
            faultMva = faultMva
        )
    }

    fun calculateDetailed(
        input: TransformerDetailedInput
    ): TransformerDetailedResult {

        validateDetailed(input)

        val base =
            calculate(
                TransformerInput(
                    ratingKva = input.ratingKva,
                    primaryVoltageV =
                        input.primaryVoltageV,
                    secondaryVoltageV =
                        input.secondaryVoltageV,
                    impedancePercent =
                        input.impedancePercent,
                    frequencyHz =
                        input.frequencyHz
                )
            )

        val loadRatio =
            input.loadPercent / 100.0

        val copperLoss =
            input.loadLossKw *
                loadRatio *
                loadRatio

        val totalLoss =
            input.noLoadLossKw +
                copperLoss

        val outputPower =
            input.ratingKva *
                loadRatio *
                input.powerFactor

        val inputPower =
            outputPower +
                totalLoss

        val efficiency =
            if (inputPower > 0.0) {
                outputPower /
                    inputPower *
                    100.0
            } else {
                100.0
            }

        val regulation =
            input.impedancePercent *
                loadRatio *
                input.powerFactor

        val secondaryBaseImpedance =
            input.secondaryVoltageV *
                input.secondaryVoltageV /
                (
                    input.ratingKva *
                        1000.0
                    )

        val primaryBaseImpedance =
            input.primaryVoltageV *
                input.primaryVoltageV /
                (
                    input.ratingKva *
                        1000.0
                    )

        val secondaryImpedance =
            secondaryBaseImpedance *
                input.impedancePercent /
                100.0

        val primaryImpedance =
            primaryBaseImpedance *
                input.impedancePercent /
                100.0

        val notes = mutableListOf<String>()

        if (input.tapPercent != 0.0) {
            notes +=
                "Tap setting = %.2f %%"
                    .format(input.tapPercent)
        }

        notes +=
            "Transformer impedance = %.2f %%"
                .format(input.impedancePercent)

        notes +=
            "Estimated regulation = %.2f %%"
                .format(regulation)

        return TransformerDetailedResult(
            primaryCurrentA =
                base.primaryCurrentA,

            secondaryCurrentA =
                base.secondaryCurrentA,

            shortCircuitCurrentKA =
                base.shortCircuitCurrentKA,

            faultMva =
                base.faultMva,

            inputPowerKw =
                inputPower,

            outputPowerKw =
                outputPower,

            totalLossKw =
                totalLoss,

            efficiencyPercent =
                efficiency,

            approximateRegulationPercent =
                regulation,

            primaryImpedanceOhm =
                primaryImpedance,

            secondaryImpedanceOhm =
                secondaryImpedance,

            turnsRatio =
                input.primaryVoltageV /
                    input.secondaryVoltageV,

            notes = notes
        )
    }

    private fun validate(
        input: TransformerInput
    ) {
        require(input.ratingKva > 0.0)
        require(input.primaryVoltageV > 0.0)
        require(input.secondaryVoltageV > 0.0)
        require(input.impedancePercent > 0.0)
        require(input.frequencyHz > 0.0)
    }

    private fun validateDetailed(
        input: TransformerDetailedInput
    ) {
        require(input.ratingKva > 0.0)
        require(input.primaryVoltageV > 0.0)
        require(input.secondaryVoltageV > 0.0)
        require(input.impedancePercent > 0.0)
        require(input.frequencyHz > 0.0)
        require(input.powerFactor in 0.01..1.0)
        require(input.noLoadLossKw >= 0.0)
        require(input.loadLossKw >= 0.0)
        require(input.loadPercent >= 0.0)
    }
}
