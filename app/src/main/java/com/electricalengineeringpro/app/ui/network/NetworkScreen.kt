package com.electricalengineeringpro.app.ui.network

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
fun NetworkScreen(
    onBack: () -> Unit
) {

    val core = ProfessionalEngineeringCore.instance

    val loads = remember {
        listOf(
            ElectricalLoad(
                id = "L1",
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
                id = "L2",
                name = "Sockets",
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
                id = "L3",
                name = "Pumps",
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
        core.network.calculate(
            loads = loads,
            voltageV = 400.0,
            powerFactor = 0.9
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            onClick = onBack
        ) {
            Text("← Back")
        }

        Text(
            text = "Electrical Network",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Network calculation summary"
        )

        NetworkValueCard(
            "Connected Load",
            "%.2f kW".format(
                result.totalConnectedKw
            )
        )

        NetworkValueCard(
            "Demand Load",
            "%.2f kW".format(
                result.totalDemandKw
            )
        )

        NetworkValueCard(
            "Main Current",
            "%.2f A".format(
                result.mainCurrentA
            )
        )

        NetworkValueCard(
            "Estimated Transformer",
            "%.0f kVA".format(
                result.estimatedTransformerKva
            )
        )

        result.buses.forEach { bus ->

            NetworkValueCard(
                title = bus.busName,
                value = """
                    Connected: %.2f kW
                    Demand: %.2f kW
                    Current: %.2f A
                """.trimIndent().format(
                    bus.connectedLoadKw,
                    bus.demandLoadKw,
                    bus.currentA
                )
            )
        }
    }
}

@Composable
private fun NetworkValueCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = value,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
