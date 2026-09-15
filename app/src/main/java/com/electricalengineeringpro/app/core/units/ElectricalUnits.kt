package com.electricalengineeringpro.app.core.units

object ElectricalUnits {

    fun wattsToKw(watts: Double): Double = watts / 1000.0

    fun kwToWatts(kw: Double): Double = kw * 1000.0

    fun hpToKw(hp: Double): Double = hp * 0.746

    fun kwToHp(kw: Double): Double = kw / 0.746

    fun kvaToKw(kva: Double, powerFactor: Double): Double {
        require(powerFactor in 0.0..1.0)
        return kva * powerFactor
    }

    fun kwToKva(kw: Double, powerFactor: Double): Double {
        require(powerFactor > 0.0)
        return kw / powerFactor
    }

    fun percent(value: Double): Double = value / 100.0
}
