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
import com.electricalengineeringpro.app.core.calculation.MdbInput

@Composable
fun PanelScreen(
    onBack: () -> Unit
) {
    val core = ProfessionalEngineeringCore.instance

    var load by remember { mutableStateOf("400") }
    var demand by remember { mutableStateOf("80") }
    var pf by remember { mutableStateOf("0.9") }

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
            title = "MDB / DB / MCC",
            subtitle = "Panel incomer and busbar preliminary sizing"
        )

        EngineeringInputField(
            value = load,
            onValueChange = { load = it },
            label = "Connected Load",
            unit = "kW"
        )

        EngineeringInputField(
            value = demand,
            onValueChange = { demand = it },
            label = "Demand Factor",
            unit = "%"
        )

        EngineeringInputField(
            value = pf,
            onValueChange = { pf = it },
            label = "Power Factor"
        )

        Button(
            onClick = {
                val r = core.mdb.calculate(
                    MdbInput(
                        connectedLoadKw =
                            load.toDoubleOrNull() ?: 0.0,
                        demandFactor =
                            (demand.toDoubleOrNull() ?: 80.0) / 100.0,
                        powerFactor =
                            pf.toDoubleOrNull() ?: 0.9
                    )
                )

                result = """
                    Demand Load : %.2f kW
                    Design kVA  : %.2f kVA
                    Design Current : %.2f A
                    Incomer : %.0f A
                    Busbar  : %.0f A
                """.trimIndent().format(
                    r.demandLoadKw,
                    r.apparentPowerKva,
                    r.designCurrentA,
                    r.recommendedIncomerA,
                    r.recommendedBusbarA
                )
            }
        ) {
            Text("Calculate Panel")
        }

        result?.let {
            CalculationResultCard(
                title = "Panel Result",
                result = it
            )
        }
    }
}
