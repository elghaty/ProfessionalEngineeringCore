package com.elghaty.professionalengineeringcore.core.model

data class LoadInput(
    val name: String,
    val loadType: LoadType,
    val phase: Phase,
    val voltage: Double,
    val powerKw: Double,
    val powerFactor: Double = 0.9,
    val efficiency: Double = 1.0,
    val demandFactor: Double = 1.0,
    val diversityFactor: Double = 1.0
)

data class LoadResult(
    val name: String,
    val connectedKw: Double,
    val demandKw: Double,
    val diversifiedKw: Double,
    val kva: Double,
    val currentA: Double,
    val status: CalculationStatus,
    val message: String = ""
)

data class ElectricalLoad(
    val id: String,
    val name: String,
    val loadType: LoadType,
    val phase: Phase,
    val voltage: Double,
    val powerKw: Double,
    val powerFactor: Double = 0.9,
    val efficiency: Double = 1.0,
    val demandFactor: Double = 1.0,
    val diversityFactor: Double = 1.0,
    val panelName: String? = null,
    val feederName: String? = null,
    val circuitName: String? = null
)

data class LoadScheduleInput(
    val loads: List<ElectricalLoad>,
    val defaultDemandFactor: Double = 1.0,
    val defaultDiversityFactor: Double = 1.0
)

data class PanelLoadSummary(
    val panelName: String,
    val connectedKw: Double,
    val demandKw: Double,
    val diversifiedKw: Double,
    val kva: Double,
    val currentA: Double
)

data class LoadScheduleResult(
    val connectedKw: Double,
    val demandKw: Double,
    val diversifiedKw: Double,
    val kva: Double,
    val currentA: Double,
    val panels: List<PanelLoadSummary>,
    val status: CalculationStatus,
    val message: String = ""
)
