package com.electricalengineeringpro.app.ui.calculations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EngineeringModuleHeader(
    title: String,
    subtitle: String
) {

    Column {

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
