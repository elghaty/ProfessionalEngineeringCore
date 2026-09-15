package com.electricalengineeringpro.app.core.sld

enum class SldSymbolType {
    UTILITY,
    TRANSFORMER,
    GENERATOR,
    ACB,
    MCCB,
    MCB,
    MDB,
    DB,
    MCC,
    MOTOR,
    PUMP,
    FEEDER,
    LOAD
}

data class SldNode(
    val id: String,
    val label: String,
    val type: SldSymbolType,
    val x: Float = 0f,
    val y: Float = 0f,
    val parentId: String? = null,
    val electricalData: Map<String, String> = emptyMap()
)

data class SldConnection(
    val fromId: String,
    val toId: String
)

data class SingleLineDiagram(
    val nodes: List<SldNode>,
    val connections: List<SldConnection>
)
