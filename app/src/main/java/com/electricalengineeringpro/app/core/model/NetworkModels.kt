package com.electricalengineeringpro.app.core.model

enum class NetworkElementType {
    SOURCE,
    TRANSFORMER,
    GENERATOR,
    PANEL,
    MCC,
    BREAKER,
    CABLE,
    LOAD,
    MOTOR,
    PUMP
}

data class NetworkElement(
    val id: String,
    val name: String,
    val type: NetworkElementType,
    val parentId: String? = null,
    val ratingKva: Double = 0.0,
    val currentA: Double = 0.0
)

data class ElectricalNetwork(
    val id: String,
    val name: String,
    val elements: List<NetworkElement>
)
