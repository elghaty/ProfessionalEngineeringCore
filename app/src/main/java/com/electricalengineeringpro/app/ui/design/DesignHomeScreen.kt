package com.electricalengineeringpro.app.ui.design

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class DesignModule(
    val title: String
)

@Composable
fun DesignHomeScreen(
    modifier: Modifier = Modifier
) {
    val modules = listOf(
        DesignModule("Load Calculation"),
        DesignModule("Load Schedule"),
        DesignModule("Cable Sizing"),
        DesignModule("Breaker Selection"),
        DesignModule("Voltage Drop"),
        DesignModule("Motors"),
        DesignModule("Pumps"),
        DesignModule("Transformers"),
        DesignModule("Generators"),
        DesignModule("Short Circuit"),
        DesignModule("MDB / DB / MCC"),
        DesignModule("Single Line Diagram"),
        DesignModule("Engineering Report")
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(modules) { module ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(module.title)
                    }
                }
            }
        }
    }
}
