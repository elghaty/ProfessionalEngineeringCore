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
import com.electricalengineeringpro.app.core.model.Phase
import com.electricalengineeringpro.app.core.model.ShortCircuitInput

@Composable
fun ShortCircuitScreen(
    onBack: () -> Unit
) {
    val core = ProfessionalEngineeringCore.instance

    var transformer by remember { mutableStateOf("1000") }
    var impedance by remember { mutableStateOf("6") }
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
            title = "Short Circuit",
            subtitle = "Prospective fault current at transformer LV terminals"
        )

        EngineeringInputField(
            value = transformer,
            onValueChange = { transformer = it },
            label = "Transformer Rating",
            unit = "kVA"
        )

        EngineeringInputField(
            value = impedance,
            onValueChange = { impedance = it },
            label = "Transformer Impedance",
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
                val r = core.shortCircuit.calculate(
                    ShortCircuitInput(
                        transformerKva =
                            transformer.toDoubleOrNull() ?: 0.0,
                        transformerImpedancePercent =
                            impedance.toDoubleOrNull() ?: 0.0,
                        voltageV =
                            voltage.toDoubleOrNull() ?: 400.0,
                        phase = Phase.THREE_PHASE
                    )
                )

                result = """
                    Transformer : %.0f kVA
                    Voltage     : %.0f V
                    Impedance   : %.2f %%
                    Fault Current: %.2f kA
                """.trimIndent().format(
                    transformer.toDoubleOrNull() ?: 0.0,
                    voltage.toDoubleOrNull() ?: 400.0,
                    impedance.toDoubleOrNull() ?: 0.0,
                    r.prospectiveFaultCurrentKA
                )
            }
        ) {
            Text("Calculate Fault Current")
        }

        result?.let {
            CalculationResultCard(
                title = "Short-Circuit Result",
                result = it
            )
        }
    }
}
