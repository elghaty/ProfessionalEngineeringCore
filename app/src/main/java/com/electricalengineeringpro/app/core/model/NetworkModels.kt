package com.electricalengineeringpro.app.core.model

enum class NetworkElementType {
    SOURCE,
    TRANSFORMER,
    GENERATOR,
    MDB,
    DB,
    MCC,
    FEEDER,
    MOTOR,
    PUMP,
    LOAD
}

data class NetworkElement(
    val id: String,
    val name: String,
    val type: NetworkElementType,
    val parentId: String? = null,
    val voltage: Double = 400.0,
    val powerKw: Double = 0.0,
    val currentA: Double = 0.0,
    val breakerRatingA: Double = 0.0,
    val cableSizeMm2: Double = 0.0
)

data class ElectricalNetwork(
    val projectId: Long,
    val elements: List<NetworkElement> = emptyList()
)
