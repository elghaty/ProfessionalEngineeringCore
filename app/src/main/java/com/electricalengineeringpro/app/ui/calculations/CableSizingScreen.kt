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
import com.electricalengineeringpro.app.core.model.CableInput
import com.electricalengineeringpro.app.core.model.CableInsulation
import com.electricalengineeringpro.app.core.model.ConductorMaterial
import com.electricalengineeringpro.app.core.model.InstallationMethod
import com.electricalengineeringpro.app.core.model.Phase

@Composable
fun CableSizingScreen(
    onBack: () -> Unit
) {
    val core = ProfessionalEngineeringCore.instance

    var current by remember { mutableStateOf("100") }
    var length by remember { mutableStateOf("50") }
    var voltage by remember { mutableStateOf("400") }
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
            title = "Cable Sizing",
            subtitle = "Preliminary cable selection and voltage-drop check"
        )

        EngineeringInputField(
            value = current,
            onValueChange = { current = it },
            label = "Design Current",
            unit = "A"
        )

        EngineeringInputField(
            value = length,
            onValueChange = { length = it },
            label = "Cable Length",
            unit = "m"
        )

        EngineeringInputField(
            value = voltage,
            onValueChange = { voltage = it },
            label = "Voltage",
            unit = "V"
        )

        EngineeringInputField(
            value = pf,
            onValueChange = { pf = it },
            label = "Power Factor"
        )

        Button(
            onClick = {
                val r = core.cables.calculate(
                    CableInput(
                        designCurrentA =
                            current.toDoubleOrNull() ?: 0.0,
                        lengthM =
                            length.toDoubleOrNull() ?: 0.0,
                        voltageV =
                            voltage.toDoubleOrNull() ?: 400.0,
                        powerFactor =
                            pf.toDoubleOrNull() ?: 0.9,
                        phase = Phase.THREE_PHASE,
                        conductorMaterial =
                            ConductorMaterial.COPPER,
                        installationMethod =
                            InstallationMethod.TRAY,
                        insulation =
                            CableInsulation.XLPE
                    )
                )

                result = """
                    Selected Size : %.1f mm²
                    Ampacity      : %.1f A
                    Voltage Drop  : %.2f %%
                    Utilization   : %.1f %%
                    Conductor     : %s
                """.trimIndent().format(
                    r.selectedSizeMm2,
                    r.ampacityA,
                    r.voltageDropPercent,
                    r.utilizationPercent,
                    r.conductorDescription
                )
            }
        ) {
            Text("Select Cable")
        }

        result?.let {
            CalculationResultCard(
                title = "Cable Result",
                result = it
            )
        }
    }
}
