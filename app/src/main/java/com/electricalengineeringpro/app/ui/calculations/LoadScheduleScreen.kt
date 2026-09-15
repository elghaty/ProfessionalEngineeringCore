package com.electricalengineeringpro.app.ui.calculations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.Phase

@Composable
fun LoadScheduleScreen(
    onBack: () -> Unit
) {

    val core = ProfessionalEngineeringCore.instance

    val loads = remember {
        listOf(
            ElectricalLoad(
                id = "LS-01",
                name = "Lighting",
                type = LoadType.LIGHTING,
                quantity = 1,
                unitPowerKw = 25.0,
                powerFactor = 0.95,
                demandFactor = 0.9,
                phase = Phase.THREE_PHASE,
                voltageV = 400.0,
                panelName = "MDB"
            ),
            ElectricalLoad(
                id = "LS-02",
                name = "General Sockets",
                type = LoadType.SOCKET,
                quantity = 1,
                unitPowerKw = 40.0,
                powerFactor = 0.9,
                demandFactor = 0.8,
                phase = Phase.THREE_PHASE,
                voltageV = 400.0,
                panelName = "MDB"
            ),
            ElectricalLoad(
                id = "LS-03",
                name = "Pump",
                type = LoadType.PUMP,
                quantity = 2,
                unitPowerKw = 30.0,
                powerFactor = 0.85,
                demandFactor = 0.8,
                phase = Phase.THREE_PHASE,
                voltageV = 400.0,
                panelName = "MCC-01"
            )
        )
    }

    val result = remember {
        core.loadSchedule.calculate(loads)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Button(
            onClick = onBack
        ) {
            Text("← Back")
        }

        EngineeringModuleHeader(
            title = "Load Schedule",
            subtitle = "Electrical load schedule summary"
        )

        result.rows.forEachIndexed { index, row ->

            Card(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    modifier = Modifier.padding(14.dp)
                ) {

                    Text(
                        text = "${index + 1}. ${row.load.name}"
                    )

                    Text(
                        text =
                            "Connected: %.2f kW".format(
                                row.connectedKw
                            )
                    )

                    Text(
                        text =
                            "Demand: %.2f kW".format(
                                row.demandKw
                            )
                    )

                    Text(
                        text =
                            "Current: %.2f A".format(
                                row.designCurrentA
                            )
                    )
                }
            }
        }

        CalculationResultCard(
            title = "Schedule Total",
            result = """
                Total Connected: %.2f kW
                Total Demand: %.2f kW
                Maximum Current: %.2f A
            """.trimIndent().format(
                result.totalConnectedKw,
                result.totalDemandKw,
                result.maximumDesignCurrentA
            )
        )
    }
}
