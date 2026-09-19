package com.electricalengineeringpro.app.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfessionalEngineeringApp()
        }
    }
}

@Composable
private fun ProfessionalEngineeringApp() {

    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color(0xFF081018)
                    )
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Professional Engineering",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Electrical Design & Calculation System",
                    color = Color(0xFF9EABB5),
                    fontSize = 14.sp
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                CalculatorCard(
                    title = "Power Calculator",
                    description =
                        "Electrical power and current calculations"
                )

                CalculatorCard(
                    title = "Load Calculator",
                    description =
                        "Load and demand calculations"
                )

                CalculatorCard(
                    title = "Cable Calculator",
                    description =
                        "Cable sizing and selection"
                )

                CalculatorCard(
                    title = "Voltage Drop",
                    description =
                        "Voltage drop calculations"
                )

                CalculatorCard(
                    title = "Short Circuit",
                    description =
                        "Short circuit current calculations"
                )

                CalculatorCard(
                    title = "Breaker Selection",
                    description =
                        "Circuit breaker selection"
                )

                CalculatorCard(
                    title = "Transformer",
                    description =
                        "Transformer calculations and sizing"
                )

                CalculatorCard(
                    title = "Generator",
                    description =
                        "Generator sizing calculations"
                )

                CalculatorCard(
                    title = "Motor",
                    description =
                        "Motor electrical calculations"
                )

                CalculatorCard(
                    title = "Pump",
                    description =
                        "Pump electrical calculations"
                )

                CalculatorCard(
                    title = "MDB",
                    description =
                        "Main distribution board calculations"
                )

                CalculatorCard(
                    title = "Protection",
                    description =
                        "Electrical protection calculations"
                )

                CalculatorCard(
                    title = "Network Design",
                    description =
                        "Electrical network design"
                )

                CalculatorCard(
                    title = "Complete Design",
                    description =
                        "Integrated electrical design"
                )

                CalculatorCard(
                    title = "Single Line Diagram",
                    description =
                        "Automatic SLD generation"
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text =
                        "Engineering Core Engine",
                    color = Color(0xFF00BCD4),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text =
                        "Professional Engineering Core",
                    color = Color.White,
                    fontSize = 13.sp
                )

                Text(
                    text =
                        "Version 1.0",
                    color = Color(0xFF9EABB5),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun CalculatorCard(
    title: String,
    description: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                Color(0xFF111D26)
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = description,
                        color = Color(0xFF9EABB5),
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        // Calculator screens will be connected
                        // in the next development stage.
                    }
                ) {
                    Text("Open")
                }
            }
        }
    }
}
