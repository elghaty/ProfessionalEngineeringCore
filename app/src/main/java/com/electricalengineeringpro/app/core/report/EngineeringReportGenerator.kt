package com.electricalengineeringpro.app.core.report

import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.ElectricalLoad

class EngineeringReportGenerator(
    private val core: ProfessionalEngineeringCore =
        ProfessionalEngineeringCore.instance
) {

    fun generate(
        project: EngineeringReportProject,
        loads: List<ElectricalLoad>,
        transformerKva: Double = 0.0,
        shortCircuitKA: Double = 0.0
    ): EngineeringReport {

        require(project.projectName.isNotBlank()) {
            "Project name is required."
        }

        val loadResults =
            loads.map {
                core.loads.calculate(it)
            }

        val connectedKw =
            loadResults.sumOf {
                it.connectedKw
            }

        val demandKw =
            loadResults.sumOf {
                it.demandKw
            }

        val summary =
            core.designSummary.calculate(
                loads = loads,
                voltage = project.voltageV,
                powerFactor = 0.90
            )

        val finalTransformerKva =
            if (transformerKva > 0.0) {
                transformerKva
            } else {
                summary.recommendedTransformerKva
            }

        val sections =
            listOf(

                EngineeringReportSection(
                    title = "Project Information",
                    items = listOf(
                        EngineeringReportItem(
                            label = "Project",
                            value = project.projectName
                        ),
                        EngineeringReportItem(
                            label = "Client",
                            value = project.clientName
                        ),
                        EngineeringReportItem(
                            label = "Project Number",
                            value = project.projectNumber
                        ),
                        EngineeringReportItem(
                            label = "Engineer",
                            value = project.engineerName
                        ),
                        EngineeringReportItem(
                            label = "Standard",
                            value = project.standard
                        )
                    )
                ),

                EngineeringReportSection(
                    title = "Electrical Design Summary",
                    items = listOf(
                        EngineeringReportItem(
                            label = "Connected Load",
                            value = "%.2f kW"
                                .format(connectedKw)
                        ),
                        EngineeringReportItem(
                            label = "Demand Load",
                            value = "%.2f kW"
                                .format(demandKw)
                        ),
                        EngineeringReportItem(
                            label = "Total Design Load",
                            value = "%.2f kW"
                                .format(summary.designLoadKw)
                        ),
                        EngineeringReportItem(
                            label = "Main Current",
                            value = "%.2f A"
                                .format(summary.totalCurrentA)
                        ),
                        EngineeringReportItem(
                            label = "Recommended Main Breaker",
                            value = "%.0f A"
                                .format(
                                    summary.recommendedMainBreakerA
                                )
                        ),
                        EngineeringReportItem(
                            label = "Recommended Transformer",
                            value = "%.0f kVA"
                                .format(finalTransformerKva)
                        ),
                        EngineeringReportItem(
                            label = "Short Circuit",
                            value = "%.2f kA"
                                .format(shortCircuitKA)
                        )
                    )
                ),

                EngineeringReportSection(
                    title = "Design Basis",
                    items = listOf(
                        EngineeringReportItem(
                            label = "Voltage",
                            value = "%.0f V"
                                .format(project.voltageV)
                        ),
                        EngineeringReportItem(
                            label = "Frequency",
                            value = "%.0f Hz"
                                .format(project.frequencyHz)
                        ),
                        EngineeringReportItem(
                            label = "Power Unit",
                            value = "kW"
                        ),
                        EngineeringReportItem(
                            label = "Apparent Power Unit",
                            value = "kVA"
                        ),
                        EngineeringReportItem(
                            label = "Current Unit",
                            value = "A"
                        ),
                        EngineeringReportItem(
                            label = "Fault Current Unit",
                            value = "kA"
                        )
                    )
                )
            )

        return EngineeringReport(
            project = project,

            summary =
                EngineeringReportSummary(
                    connectedLoadKw = connectedKw,
                    demandLoadKw = demandKw,
                    designCurrentA =
                        summary.totalCurrentA,
                    transformerKva =
                        finalTransformerKva,
                    mainBreakerA =
                        summary.recommendedMainBreakerA,
                    shortCircuitKA =
                        shortCircuitKA
                ),

            sections = sections
        )
    }
}
