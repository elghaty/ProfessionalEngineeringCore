package com.electrical.calculationspro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.electrical.calculationspro.ui.screens.ProfessionalSldScreen
import com.electrical.calculationspro.ui.theme.ElectricalCalculationsProTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {

            ElectricalCalculationsProTheme {

                ProfessionalSldScreen(
                    arabic = false
                )
            }
        }
    }
}
