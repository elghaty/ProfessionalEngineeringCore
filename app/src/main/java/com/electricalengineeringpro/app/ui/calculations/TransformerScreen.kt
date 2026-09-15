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
import com.electricalengineeringpro.app.core.calculation.TransformerSizingInput

@Composable
fun TransformerScreen(
    onBack: () -> Unit
) {
    val core = ProfessionalEngineeringCore.instance

    var demand by remember { mutableStateOf("400") }
    var pf by remember { mutableStateOf("0.9") }
    var spare by remember { mutableStateOf("20") }

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
            title = "Transformer Sizing",
            subtitle = "Transformer capacity based on demand load"
        )

        EngineeringInputField(
            value = demand,
            onValueChange = { demand = it },
            label = "Demand Load",
            unit = "kW"
        )

        EngineeringInputField(
            value = pf,
            onValueChange = { pf = it },
            label = "Power Factor"
        )

        EngineeringInputField(
            value = spare,
            onValueChange = { spare = it },
            label = "Spare Capacity",
            unit = "%"
        )

        Button(
            onClick = {
                val r = core.transformerSizing.calculate(
                    TransformerSizingInput(
                        demandLoadKw =
                            demand.toDoubleOrNull() ?: 0.0,
                        powerFactor =
                            pf.toDoubleOrNull() ?: 0.9,
                        spareCapacity =
                            (spare.toDoubleOrNull() ?: 20.0) / 100.0
                    )
                )

                result = """
                    Required Transformer : %.2f kVA
                    Selected Transformer : %.0f kVA
                    Utilization           : %.1f %%
                """.trimIndent().format(
                    r.requiredKva,
                    r.selectedKva,
                    r.utilizationPercent
                )
            }
        ) {
            Text("Select Transformer")
        }

        result?.let {
            CalculationResultCard(
                title = "Transformer Result",
                result = it
            )
        }
    }
}
