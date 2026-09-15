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
import com.electricalengineeringpro.app.core.calculation.GeneratorInput
import com.electricalengineeringpro.app.core.model.Phase

@Composable
fun GeneratorScreen(
    onBack: () -> Unit
) {

    val core = ProfessionalEngineeringCore.instance

    var kva by remember { mutableStateOf("500") }
    var voltage by remember { mutableStateOf("400") }
    var pf by remember { mutableStateOf("0.8") }

    var result by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "← Back",
            modifier = Modifier
                .padding(bottom = 8.dp)
        )

        EngineeringModuleHeader(
            title = "Generator Sizing",
            subtitle = "Generator rating, current and breaker"
        )

        EngineeringInputField(
            value = kva,
            onValueChange = { kva = it },
            label = "Generator Rating",
            unit = "kVA"
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

                val input =
                    GeneratorInput(
                        ratingKva = kva.toDoubleOrNull() ?: 0.0,
                        voltageV = voltage.toDoubleOrNull() ?: 0.0,
                        powerFactor = pf.toDoubleOrNull() ?: 0.8,
                        phase = Phase.THREE_PHASE
                    )

                val calculated =
                    core.generators.calculate(input)

                result =
                    """
                    Generator: %.0f kVA
                    Active Power: %.2f kW
                    Full Load Current: %.2f A
                    Recommended Breaker: %.0f A
                    """.trimIndent().format(
                        calculated.ratingKva,
                        calculated.activePowerKw,
                        calculated.fullLoadCurrentA,
                        calculated.recommendedBreakerA
                    )
            }
        ) {
            Text("Calculate")
        }

        result?.let {
            CalculationResultCard(
                title = "Generator Result",
                result = it
            )
        }
    }
}
