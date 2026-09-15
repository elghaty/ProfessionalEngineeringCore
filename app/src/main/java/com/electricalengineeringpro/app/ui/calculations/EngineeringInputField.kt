package com.electricalengineeringpro.app.ui.calculations

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EngineeringInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unit: String = ""
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
        label = {
            Text(
                if (unit.isBlank())
                    label
                else
                    "$label ($unit)"
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}
