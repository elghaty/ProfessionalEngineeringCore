package com.electricalengineeringpro.app.core.sld

enum class SldSymbolType {
    UTILITY,
    TRANSFORMER,
    GENERATOR,
    MAIN_SWITCHBOARD,
    PANEL,
    MCC,
    BREAKER,
    CABLE,
    MOTOR,
    PUMP,
    LOAD,
    EARTH
}

data class SldNode(
    val id: String,
    val name: String,
    val type: SldSymbolType,
    val x: Float = 0f,
    val y: Float = 0f,
    val electricalData: Map<String, String> = emptyMap()
)

data class SldConnection(
    val fromId: String,
    val toId: String,
    val label: String = ""
)

data class SingleLineDiagram(
    val nodes: List<SldNode>,
    val connections: List<SldConnection>
)
