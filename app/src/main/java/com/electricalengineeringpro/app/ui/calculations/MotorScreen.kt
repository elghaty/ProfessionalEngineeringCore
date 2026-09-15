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
import com.electricalengineeringpro.app.core.model.MotorInput

@Composable
fun MotorScreen(
    onBack: () -> Unit
) {
    val core = ProfessionalEngineeringCore.instance

    var power by remember { mutableStateOf("75") }
    var voltage by remember { mutableStateOf("400") }
    var efficiency by remember { mutableStateOf("90") }
    var pf by remember { mutableStateOf("0.85") }

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
            title = "Motor Calculation",
            subtitle = "Full-load and starting current"
        )

        EngineeringInputField(
            value = power,
            onValueChange = { power = it },
            label = "Motor Power",
            unit = "kW"
        )

        EngineeringInputField(
            value = voltage,
            onValueChange = { voltage = it },
            label = "Voltage",
            unit = "V"
        )

        EngineeringInputField(
            value = efficiency,
            onValueChange = { efficiency = it },
            label = "Efficiency",
            unit = "%"
        )

        EngineeringInputField(
            value = pf,
            onValueChange = { pf = it },
            label = "Power Factor"
        )

        Button(
            onClick = {
                val r = core.motors.calculate(
                    MotorInput(
                        powerKw =
                            power.toDoubleOrNull() ?: 0.0,
                        voltageV =
                            voltage.toDoubleOrNull() ?: 400.0,
                        efficiency =
                            (efficiency.toDoubleOrNull() ?: 90.0) / 100.0,
                        powerFactor =
                            pf.toDoubleOrNull() ?: 0.85
                    )
                )

                result = """
                    Motor Power       : %.2f kW
                    Full Load Current : %.2f A
                    Starting Current  : %.2f A
                """.trimIndent().format(
                    r.powerKw,
                    r.fullLoadCurrentA,
                    r.startingCurrentA
                )
            }
        ) {
            Text("Calculate Motor")
        }

        result?.let {
            CalculationResultCard(
                title = "Motor Result",
                result = it
            )
        }
    }
}
