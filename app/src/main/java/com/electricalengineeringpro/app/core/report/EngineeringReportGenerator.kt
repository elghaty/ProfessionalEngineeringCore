package com.electricalengineeringpro.app.core.report

import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore

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

        val loadResults =
            loads.map { core.loads.calculate(it) }

        val connectedKw =
            loadResults.sumOf { it.connectedLoadKw }

        val demandKw =
            loadResults.sumOf { it.demandLoadKw }

        val summary =
            core.designSummary.calculate(loads)

        val sections = listOf(

            EngineeringReportSection(
                title = "Project Information",
                items = listOf(
                    EngineeringReportItem(
                        "Project",
                        project.projectName
                    ),
                    EngineeringReportItem(
                        "Client",
                        project.clientName
                    ),
                    EngineeringReportItem(
                        "Project Number",
                        project.projectNumber
                    ),
                    EngineeringReportItem(
                        "Engineer",
                        project.engineerName
                    ),
                    EngineeringReportItem(
                        "Standard",
                        project.standard
                    )
                )
            ),

            EngineeringReportSection(
                title = "Electrical Design Summary",
                items = listOf(
                    EngineeringReportItem(
                        "Connected Load",
                        "%.2f kW".format(connectedKw)
                    ),
                    EngineeringReportItem(
                        "Demand Load",
                        "%.2f kW".format(demandKw)
                    ),
                    EngineeringReportItem(
                        "Design Current",
                        "%.2f A".format(summary.designCurrentA)
                    ),
                    EngineeringReportItem(
                        "Recommended Transformer",
                        "%.0f kVA".format(
                            if (transformerKva > 0)
                                transformerKva
                            else
                                summary.recommendedTransformerKva
                        )
                    ),
                    EngineeringReportItem(
                        "Main Breaker",
                        "%.0f A".format(
                            summary.recommendedMainBreakerA
                        )
                    ),
                    EngineeringReportItem(
                        "Short Circuit",
                        "%.2f kA".format(shortCircuitKA)
                    )
                )
            ),

            EngineeringReportSection(
                title = "Design Basis",
                items = listOf(
                    EngineeringReportItem(
                        "Voltage",
                        "%.0f V".format(project.voltageV)
                    ),
                    EngineeringReportItem(
                        "Frequency",
                        "%.0f Hz".format(project.frequencyHz)
                    ),
                    EngineeringReportItem(
                        "Calculation Unit",
                        "kW"
                    ),
                    EngineeringReportItem(
                        "Apparent Power Unit",
                        "kVA"
                    ),
                    EngineeringReportItem(
                        "Current Unit",
                        "A"
                    ),
                    EngineeringReportItem(
                        "Fault Current Unit",
                        "kA"
                    )
                )
            )
        )

        return EngineeringReport(
            project = project,
            summary = EngineeringReportSummary(
                connectedLoadKw = connectedKw,
                demandLoadKw = demandKw,
                designCurrentA = summary.designCurrentA,
                transformerKva =
                    if (transformerKva > 0)
                        transformerKva
                    else
                        summary.recommendedTransformerKva,
                mainBreakerA =
                    summary.recommendedMainBreakerA,
                shortCircuitKA = shortCircuitKA
            ),
            sections = sections
        )
    }
}
