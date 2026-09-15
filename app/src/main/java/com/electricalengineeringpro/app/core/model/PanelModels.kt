package com.electricalengineeringpro.app.core.model

data class Panel(
    val id: String,
    val name: String,
    val type: NetworkElementType,
    val voltage: Double = 400.0,
    val incomingBreakerA: Double = 0.0,
    val outgoingFeeders: List<String> = emptyList()
)

data class Feeder(
    val id: String,
    val name: String,
    val sourcePanelId: String,
    val destinationPanelId: String?,
    val loadIds: List<Long> = emptyList(),
    val designCurrentA: Double = 0.0,
    val cableSizeMm2: Double = 0.0,
    val breakerRatingA: Double = 0.0,
    val voltageDropPercent: Double = 0.0,
    val shortCircuitKA: Double = 0.0
)
