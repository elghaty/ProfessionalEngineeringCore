package com.electrical.calculationspro.core

/**
 * Temporary application-side access point.
 *
 * The engineering calculation authority remains
 * ProfessionalEngineeringCore.
 *
 * No engineering formulas belong in the UI layer.
 */
object CoreProvider {

    val core: ProfessionalEngineeringCore
        get() = ProfessionalEngineeringCore.instance
}
