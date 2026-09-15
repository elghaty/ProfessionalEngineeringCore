package com.electricalengineeringpro.app.ui.calculations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.PumpInput

@Composable
fun PumpScreen(
    onBack: () -> Unit
) {
    val core = ProfessionalEngineeringCore.instance

    var flow by remember { mutableStateOf("300") }
    var head by remember { mutableStateOf("7") }
    var efficiency by remember { mutableStateOf("80") }
    var voltage by remember { mutableStateOf("400") }

    var result by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onBack) {
            Text("← Back")
        }

        EngineeringModuleHeader(
            title = "Pump Calculation",
            subtitle = "Hydraulic power and motor requirement"
        )

        EngineeringInputField(
            value = flow,
            onValueChange = { flow = it },
            label = "Flow",
            unit = "L/s"
        )

        EngineeringInputField(
            value = head,
            onValueChange = { head = it },
            label = "Total Head",
            unit = "m"
        )

        EngineeringInputField(
            value = efficiency,
            onValueChange = { efficiency = it },
            label = "Pump Efficiency",
            unit = "%"
        )

        EngineeringInputField(
            value = voltage,
            onValueChange = { voltage = it },
            label = "Voltage",
            unit = "V"
        )

        Button(
            onClick = {
                val r = core.pumps.calculate(
                    PumpInput(
                        flowLps =
                            flow.toDoubleOrNull() ?: 0.0,
                        headM =
                            head.toDoubleOrNull() ?: 0.0,
                        efficiency =
                            (efficiency.toDoubleOrNull() ?: 80.0) / 100.0,
                        voltageV =
                            voltage.toDoubleOrNull() ?: 400.0
                    )
                )

                result = """
                    Flow          : %.2f L/s
                    Head          : %.2f m
                    Hydraulic     : %.2f kW
                    Motor Power   : %.2f kW
                    Current       : %.2f A
                """.trimIndent().format(
                    flow.toDoubleOrNull() ?: 0.0,
                    head.toDoubleOrNull() ?: 0.0,
                    r.hydraulicPowerKw,
                    r.motorPowerKw,
                    r.motorCurrentA
                )
            }
        ) {
            Text("Calculate Pump")
        }

        result?.let {
            CalculationResultCard(
                title = "Pump Result",
                result = it
            )
        }
    }
}
