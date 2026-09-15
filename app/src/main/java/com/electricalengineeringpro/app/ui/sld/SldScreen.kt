package com.electricalengineeringpro.app.ui.sld

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.NetworkElement
import com.electricalengineeringpro.app.core.model.NetworkElementType
import com.electricalengineeringpro.app.core.sld.SldNode
import kotlin.math.max

@Composable
fun SldScreen(
    onBack: () -> Unit
) {

    val core = ProfessionalEngineeringCore.instance

    var scale by remember {
        mutableFloatStateOf(1f)
    }

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    val diagram = remember {

        core.sld.generate(
            source = NetworkElement(
                id = "SOURCE",
                name = "Utility",
                type = NetworkElementType.SOURCE
            ),
            panels = listOf(
                NetworkElement(
                    id = "MDB",
                    name = "MDB",
                    type = NetworkElementType.PANEL
                ),
                NetworkElement(
                    id = "DB-01",
                    name = "DB-01",
                    type = NetworkElementType.PANEL
                ),
                NetworkElement(
                    id = "MCC-01",
                    name = "MCC-01",
                    type = NetworkElementType.PANEL
                )
            ),
            feeders = emptyList()
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "←",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {

                    detectTransformGestures { _, pan, zoom, _ ->

                        scale =
                            max(
                                0.5f,
                                (scale * zoom).coerceAtMost(3f)
                            )

                        offset += pan
                    }
                }
        ) {

            val centerX = size.width / 2f

            val startY = 180f

            val spacing = 180f

            diagram.nodes.forEachIndexed { index, node ->

                val x = centerX + offset.x
                val y =
                    startY +
                        index * spacing +
                        offset.y

                if (index > 0) {

                    drawLine(
                        start = Offset(
                            centerX + offset.x,
                            y - spacing * scale
                        ),
                        end = Offset(
                            centerX + offset.x,
                            y
                        ),
                        strokeWidth = 5f * scale,
                        cap = StrokeCap.Round
                    )
                }

                drawRect(
                    topLeft = Offset(
                        x - 70f * scale,
                        y - 30f * scale
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        140f * scale,
                        60f * scale
                    ),
                    style = Stroke(
                        width = 4f * scale
                    )
                )

                drawContext.canvas.nativeCanvas.drawText(
                    node.name,
                    x - 50f * scale,
                    y + 7f * scale,
                    android.graphics.Paint().apply {
                        textSize = 18f * scale
                        textAlign =
                            android.graphics.Paint.Align.LEFT
                    }
                )
            }
        }
    }
}
