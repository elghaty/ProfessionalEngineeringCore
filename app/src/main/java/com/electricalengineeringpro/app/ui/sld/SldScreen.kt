package com.electricalengineeringpro.app.ui.sld

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.electricalengineeringpro.app.core.sld.*

@Composable
fun SldScreen(
    diagram: SingleLineDiagram,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 4f)
                    offset += pan
                }
            }
    ) {

        fun position(node: SldNode): Offset {
            return Offset(
                x = node.x * scale + offset.x,
                y = node.y * scale + offset.y
            )
        }

        diagram.connections.forEach { connection ->

            val from = diagram.nodes.firstOrNull {
                it.id == connection.fromId
            } ?: return@forEach

            val to = diagram.nodes.firstOrNull {
                it.id == connection.toId
            } ?: return@forEach

            drawLine(
                start = position(from),
                end = position(to),
                strokeWidth = 4.dp.toPx()
            )
        }

        diagram.nodes.forEach { node ->

            val p = position(node)

            drawRect(
                topLeft = Offset(
                    p.x - 60f * scale,
                    p.y - 30f * scale
                ),
                size = androidx.compose.ui.geometry.Size(
                    120f * scale,
                    60f * scale
                ),
                style = Stroke(
                    width = 3.dp.toPx()
                )
            )

            drawContext.canvas.nativeCanvas.drawText(
                node.label,
                p.x - 50f * scale,
                p.y + 5f * scale,
                android.graphics.Paint().apply {
                    textSize = 13f * scale
                    isAntiAlias = true
                }
            )
        }
    }
}
