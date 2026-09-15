package com.electricalengineeringpro.app.core.report

data class EngineeringReportProject(
    val projectName: String,
    val clientName: String = "",
    val projectNumber: String = "",
    val engineerName: String = "Eng. Abdelraouf Elghaty",
    val standard: String = "IEC",
    val voltageV: Double = 400.0,
    val frequencyHz: Double = 50.0
)

data class EngineeringReportSummary(
    val connectedLoadKw: Double,
    val demandLoadKw: Double,
    val designCurrentA: Double,
    val transformerKva: Double,
    val mainBreakerA: Double,
    val shortCircuitKA: Double
)

data class EngineeringReport(
    val project: EngineeringReportProject,
    val summary: EngineeringReportSummary,
    val sections: List<EngineeringReportSection>
)

data class EngineeringReportSection(
    val title: String,
    val items: List<EngineeringReportItem>
)

data class EngineeringReportItem(
    val label: String,
    val value: String
)
