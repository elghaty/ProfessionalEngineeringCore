package com.electrical.calculationspro.core

import kotlin.math.sqrt

/**
 * SINGLE ENGINEERING CORE.
 *
 * All engineering calculations must eventually pass
 * through this class.
 *
 * UI must not contain engineering formulas.
 */
class ProfessionalEngineeringCore private constructor() {

    fun calculatePower(
        powerKw: Double,
        voltageV: Double,
        powerFactor: Double,
        phase: EngineeringPhase
    ): PowerCalculationResult {

        require(powerKw >= 0.0)
        require(voltageV > 0.0)
        require(powerFactor in 0.01..1.0)

        val kva =
            powerKw / powerFactor

        val kvar =
            if (kva >= powerKw) {
                sqrt(
                    (kva * kva) -
                        (powerKw * powerKw)
                )
            } else {
                0.0
            }

        val current =
            when (phase) {

                EngineeringPhase.DC ->
                    powerKw * 1000.0 /
                        voltageV

                EngineeringPhase.SINGLE ->
                    kva * 1000.0 /
                        voltageV

                EngineeringPhase.TWO_PHASE ->
                    kva * 1000.0 /
                        (2.0 * voltageV)

                EngineeringPhase.THREE ->
                    kva * 1000.0 /
                        (sqrt(3.0) * voltageV)
            }

        return PowerCalculationResult(
            activePowerKw = powerKw,
            apparentPowerKva = kva,
            reactivePowerKvar = kvar,
            currentA = current,
            powerFactor = powerFactor
        )
    }

    fun calculateLoad(
        load: EngineeringLoad
    ): LoadCalculationResult {

        require(load.powerKw >= 0.0)
        require(load.quantity > 0)
        require(load.voltageV > 0.0)
        require(load.powerFactor in 0.01..1.0)
        require(load.efficiency in 0.01..1.0)
        require(load.demandFactor in 0.0..1.0)
        require(load.diversityFactor > 0.0)

        val connected =
            load.powerKw *
                load.quantity

        val demand =
            connected *
                load.demandFactor

        val design =
            demand /
                load.diversityFactor

        val electricalInput =
            design /
                load.efficiency

        val power =
            calculatePower(
                powerKw = electricalInput,
                voltageV = load.voltageV,
                powerFactor = load.powerFactor,
                phase = load.phase
            )

        return LoadCalculationResult(
            connectedLoadKw = connected,
            demandLoadKw = demand,
            designLoadKw = design,
            apparentPowerKva =
                power.apparentPowerKva,
            designCurrentA =
                power.currentA
        )
    }

    fun calculateProjectSummary(
        project: EngineeringProject,
        designMargin: Double = 1.15
    ): ProjectSummaryResult {

        require(project.voltageV > 0.0)
        require(project.powerFactor in 0.01..1.0)
        require(designMargin >= 1.0)

        val results =
            project.loads.map {
                calculateLoad(it)
            }

        val connected =
            results.sumOf {
                it.connectedLoadKw
            }

        val demand =
            results.sumOf {
                it.demandLoadKw
            }

        val design =
            results.sumOf {
                it.designLoadKw
            }

        val kva =
            results.sumOf {
                it.apparentPowerKva
            } * designMargin

        val mainCurrent =
            if (kva > 0.0) {
                kva * 1000.0 /
                    (sqrt(3.0) *
                        project.voltageV)
            } else {
                0.0
            }

        val transformerRatings =
            listOf(
                50.0,
                100.0,
                160.0,
                250.0,
                315.0,
                400.0,
                500.0,
                630.0,
                800.0,
                1000.0,
                1250.0,
                1600.0,
                2000.0,
                2500.0,
                3150.0,
                4000.0,
                5000.0
            )

        val transformer =
            transformerRatings.firstOrNull {
                it >= kva
            } ?: transformerRatings.last()

        val breakerRatings =
            listOf(
                16.0,
                20.0,
                25.0,
                32.0,
                40.0,
                50.0,
                63.0,
                80.0,
                100.0,
                125.0,
                160.0,
                200.0,
                250.0,
                315.0,
                400.0,
                500.0,
                630.0,
                800.0,
                1000.0,
                1250.0,
                1600.0,
                2000.0,
                2500.0,
                3200.0,
                4000.0,
                5000.0,
                6300.0
            )

        val breaker =
            breakerRatings.firstOrNull {
                it >= mainCurrent
            } ?: breakerRatings.last()

        return ProjectSummaryResult(
            connectedLoadKw = connected,
            demandLoadKw = demand,
            designLoadKw = design,
            apparentPowerKva = kva,
            mainCurrentA = mainCurrent,
            recommendedTransformerKva =
                transformer,
            recommendedMainBreakerA =
                breaker
        )
    }

    companion object {

        @JvmStatic
        val instance:
            ProfessionalEngineeringCore by lazy {
                ProfessionalEngineeringCore()
            }
    }
}
