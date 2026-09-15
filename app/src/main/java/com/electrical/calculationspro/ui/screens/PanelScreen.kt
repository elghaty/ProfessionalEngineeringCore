package com.electrical.calculationspro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electrical.calculationspro.core.ProfessionalEngineeringFacade
import com.electrical.calculationspro.core.calculators.PanelFeederInput
import com.electrical.calculationspro.core.calculators.PanelInput

private data class FeederUi(
    val name: String,
    val loadKw: String
)

@Composable
fun PanelScreen(
    onBack: (() -> Unit)? = null
) {

    val facade =
        remember {
            ProfessionalEngineeringFacade()
        }

    var panelName by remember {
        mutableStateOf("MDB-01")
    }

    var voltage by remember {
        mutableStateOf("400")
    }

    var feederName by remember {
        mutableStateOf("")
    }

    var feederLoad by remember {
        mutableStateOf("")
    }

    val feeders =
        remember {
            mutableStateListOf<FeederUi>()
        }

    var resultText by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "MDB / DB / MCC",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Panel load, feeder and main breaker calculation",
            modifier = Modifier.padding(
                top = 4.dp,
                bottom = 16.dp
            )
        )

        OutlinedTextField(
            value = panelName,
            onValueChange = {
                panelName = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Panel Name")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = voltage,
            onValueChange = {
                voltage = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            label = {
                Text("Voltage (V)")
            },
            singleLine = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = feederName,
                onValueChange = {
                    feederName = it
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text("Feeder")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = feederLoad,
                onValueChange = {
                    feederLoad = it
                },
                modifier = Modifier.weight(1f),
                label = {
                    Text("kW")
                },
                singleLine = true
            )
        }

        Button(
            onClick = {

                if (
                    feederName.isNotBlank() &&
                    feederLoad.toDoubleOrNull() != null
                ) {

                    feeders.add(
                        FeederUi(
                            name = feederName,
                            loadKw = feederLoad
                        )
                    )

                    feederName = ""
                    feederLoad = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Add Feeder")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            items(
                items = feeders,
                key = {
                    "${it.name}-${it.loadKw}"
                }
            ) { feeder ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = feeder.name
                        )

                        Text(
                            text =
                                "${feeder.loadKw} kW"
                        )
                    }
                }
            }
        }

        Button(
            onClick = {

                val voltageValue =
                    voltage.toDoubleOrNull()
                        ?: 400.0

                val panelInput =
                    PanelInput(
                        name = panelName,
                        voltageV =
                            voltageValue,
                        feeders =
                            feeders.map {
                                PanelFeederInput(
                                    name = it.name,
                                    loadKw =
                                        it.loadKw
                                            .toDouble(),
                                    powerFactor = 0.90,
                                    voltageV =
                                        voltageValue,
                                    demandFactor = 1.0
                                )
                            }
                    )

                val result =
                    facade
                        .getCore()
                        .panel
                        .calculate(
                            panelInput
                        )

                resultText =
                    buildString {

                        appendLine(
                            "Connected Load: %.2f kW"
                                .format(
                                    result.connectedLoadKw
                                )
                        )

                        appendLine(
                            "Demand Load: %.2f kW"
                                .format(
                                    result.demandLoadKw
                                )
                        )

                        appendLine(
                            "Apparent Power: %.2f kVA"
                                .format(
                                    result.apparentPowerKva
                                )
                        )

                        appendLine(
                            "Design Current: %.2f A"
                                .format(
                                    result.designCurrentA
                                )
                        )

                        appendLine(
                            "Main Breaker: %.0f A"
                                .format(
                                    result.recommendedMainBreakerA
                                )
                        )

                        appendLine(
                            "Busbar Required: %.0f A"
                                .format(
                                    result.requiredBusbarCurrentA
                                )
                        )

                        result.feeders.forEach {
                            appendLine()
                            appendLine(
                                "${it.name}: %.2f A / %.0f A"
                                    .format(
                                        it.currentA,
                                        it.recommendedBreakerA
                                    )
                            )
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate Panel")
        }

        if (resultText.isNotBlank()) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {

                Text(
                    text = resultText,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (onBack != null) {

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Back")
            }
        }
    }
}
