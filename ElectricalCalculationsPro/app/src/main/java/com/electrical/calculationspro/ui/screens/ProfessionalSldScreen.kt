package com.electrical.calculationspro.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.electrical.calculationspro.core.ProfessionalEngineeringCore
import com.electrical.calculationspro.core.model.ElectricalLoad
import com.electrical.calculationspro.core.model.Phase
import com.electrical.calculationspro.core.model.SldElement
import com.electrical.calculationspro.core.model.SldElementType
import com.electrical.calculationspro.core.model.SldNetwork
import com.electrical.calculationspro.core.model.SourceType
import kotlin.math.max

private data class SldUiNode(
    val element: SldElement
)

@Composable
fun ProfessionalSldScreen(
    arabic: Boolean = false
) {

    var nodes by remember {

        mutableStateOf(
            listOf(

                SldUiNode(
                    SldElement(
                        id = "utility",
                        name = "UTILITY",
                        type = SldElementType.UTILITY,
                        x = 80f,
                        y = 250f,
                        voltageV = 400.0,
                        sourceType =
                            SourceType.UTILITY,
                        sourceShortCircuitMva =
                            500.0
                    )
                ),

                SldUiNode(
                    SldElement(
                        id = "tr",
                        name = "TR-01",
                        type = SldElementType.TRANSFORMER,
                        x = 330f,
                        y = 250f,
                        voltageV = 400.0,
                        transformerKva =
                            1000.0,
                        transformerPercentZ =
                            6.0
                    )
                ),

                SldUiNode(
                    SldElement(
                        id = "mdb",
                        name = "MDB-01",
                        type = SldElementType.MDB,
                        x = 580f,
                        y = 250f,
                        voltageV = 400.0
                    )
                ),

                SldUiNode(
                    SldElement(
                        id = "load",
                        name = "LOAD-01",
                        type = SldElementType.LOAD,
                        x = 830f,
                        y = 250f,
                        powerKw = 100.0,
                        voltageV = 400.0,
                        phase = Phase.THREE,
                        powerFactor = 0.90,
                        demandFactor = 0.80
                    )
                )
            )
        )
    }

    var zoom by remember {
        mutableFloatStateOf(1f)
    }

    var pan by remember {
        mutableStateOf(Offset.Zero)
    }

    var selectedId by remember {
        mutableStateOf<String?>(
            null
        )
    }

    var calculated by remember {
        mutableStateOf(false)
    }

    val network =
        remember(nodes) {

            SldNetwork(
                elements =
                    nodes.map {
                        it.element
                    }
            )
        }

    val result =
        remember(
            nodes,
            calculated
        ) {

            if (calculated) {

                ProfessionalEngineeringCore
                    .instance
                    .generateSld(
                        network = network,
                        sourceFaultMva = 500.0,
                        voltageFactor = 1.05
                    )

            } else {
                null
            }
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFF071017)
                )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    if (arabic)
                        "SLD الاحترافي"
                    else
                        "Professional SLD",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(
                modifier =
                    Modifier.weight(1f)
            )

            Button(
                onClick = {
                    calculated = true
                }
            ) {
                Text(
                    if (arabic)
                        "احسب الشبكة"
                    else
                        "Calculate Network"
                )
            }

            TextButton(
                onClick = {
                    zoom = 1f
                    pan = Offset.Zero
                }
            ) {
                Text("Reset")
            }
        }

        HorizontalDivider()

        Row(
            modifier =
                Modifier.fillMaxSize()
        ) {

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .pointerInput(Unit) {

                            detectTransformGestures {
                                    centroid,
                                    panChange,
                                    zoomChange,
                                    _ ->

                                zoom =
                                    (zoom * zoomChange)
                                        .coerceIn(
                                            0.35f,
                                            3.5f
                                        )

                                pan += panChange
                            }
                        }
                        .pointerInput(Unit) {

                            detectDragGestures(
                                onDrag = {
                                    change,
                                    dragAmount ->

                                    change.consume()

                                    val id =
                                        selectedId
                                            ?: return@detectDragGestures

                                    nodes =
                                        nodes.map {

                                            if (
                                                it.element.id ==
                                                id
                                            ) {

                                                SldUiNode(
                                                    it.element.copy(
                                                        x =
                                                            it.element.x +
                                                                dragAmount.x /
                                                                zoom,

                                                        y =
                                                            it.element.y +
                                                                dragAmount.y /
                                                                zoom
                                                    )
                                                )

                                            } else {
                                                it
                                            }
                                        }
                                }
                            )
                        }
            ) {

                Canvas(
                    modifier =
                        Modifier.fillMaxSize()
                ) {

                    val transformX =
                        { x: Float ->
                            x * zoom +
                                pan.x
                        }

                    val transformY =
                        { y: Float ->
                            y * zoom +
                                pan.y
                        }

                    nodes
                        .zipWithNext()
                        .forEach {
                            first,
                            second ->

                            drawLine(
                                color =
                                    Color(0xFF8FA6B3),

                                start =
                                    Offset(
                                        transformX(
                                            first.element.x +
                                                90f
                                        ),
                                        transformY(
                                            first.element.y +
                                                45f
                                        )
                                    ),

                                end =
                                    Offset(
                                        transformX(
                                            second.element.x
                                        ),
                                        transformY(
                                            second.element.y +
                                                45f
                                        )
                                    ),

                                strokeWidth =
                                    5f * zoom
                            )
                        }

                    nodes.forEach { uiNode ->

                        val e =
                            uiNode.element

                        val left =
                            transformX(e.x)

                        val top =
                            transformY(e.y)

                        val width =
                            180f * zoom

                        val height =
                            90f * zoom

                        val selected =
                            selectedId ==
                                e.id

                        drawRoundRect(
                            color =
                                if (selected)
                                    Color(0xFF164D42)
                                else
                                    Color(0xFF15232B),

                            topLeft =
                                Offset(
                                    left,
                                    top
                                ),

                            size =
                                androidx.compose.ui.geometry.Size(
                                    width,
                                    height
                                ),

                            cornerRadius =
                                androidx.compose.ui.geometry.CornerRadius(
                                    12f * zoom
                                )
                        )

                        drawRoundRect(
                            color =
                                if (selected)
                                    Color(0xFF00E676)
                                else
                                    Color(0xFF536873),

                            topLeft =
                                Offset(
                                    left,
                                    top
                                ),

                            size =
                                androidx.compose.ui.geometry.Size(
                                    width,
                                    height
                                ),

                            cornerRadius =
                                androidx.compose.ui.geometry.CornerRadius(
                                    12f * zoom
                                ),

                            style =
                                Stroke(
                                    width =
                                        2f * zoom
                                )
                        )
                    }
                }

                nodes.forEach { uiNode ->

                    val e =
                        uiNode.element

                    Box(
                        modifier =
                            Modifier
                                .padding(
                                    start =
                                        (e.x * zoom +
                                            pan.x).dp,

                                    top =
                                        (e.y * zoom +
                                            pan.y).dp
                                )
                    ) {

                        Card(
                            modifier =
                                Modifier
                                    .width(
                                        180.dp
                                    )
                                    .height(
                                        90.dp
                                    ),

                            shape =
                                RoundedCornerShape(
                                    12.dp
                                ),

                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        Color.Transparent
                                ),

                            onClick = {
                                selectedId =
                                    e.id
                            }
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(
                                        10.dp
                                    )
                            ) {

                                Text(
                                    text = e.name,
                                    color =
                                        Color.White,
                                    fontSize =
                                        15.sp
                                )

                                Text(
                                    text =
                                        e.type.name,
                                    color =
                                        Color(0xFF8FA6B3),
                                    fontSize =
                                        11.sp
                                )

                                if (
                                    e.powerKw > 0
                                ) {

                                    Text(
                                        text =
                                            "${e.powerKw} kW",
                                        color =
                                            Color(0xFF00BCD4),
                                        fontSize =
                                            13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Card(
                modifier =
                    Modifier
                        .width(270.dp)
                        .fillMaxSize()
                        .padding(10.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFF101B22)
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(14.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        text =
                            if (arabic)
                                "Engineering Results"
                            else
                                "Engineering Results",

                        color = Color.White,
                        fontSize = 18.sp
                    )

                    Text(
                        text =
                            if (
                                selectedId != null
                            ) {

                                nodes
                                    .firstOrNull {
                                        it.element.id ==
                                            selectedId
                                    }
                                    ?.element
                                    ?.name
                                    ?: "-"

                            } else {
                                "-"
                            },

                        color =
                            Color(0xFF00BCD4)
                    )

                    if (result != null) {

                        ResultRow(
                            "Connected Load",
                            "${format(result.totalLoadKw)} kW"
                        )

                        ResultRow(
                            "Demand Load",
                            "${format(result.totalDemandKw)} kW"
                        )

                        ResultRow(
                            "Source Current",
                            "${format(result.sourceCurrentA)} A"
                        )

                        ResultRow(
                            "Maximum Fault",
                            "${format(result.sourceFaultCurrentKA)} kA"
                        )
                    } else {

                        Text(
                            text =
                                if (arabic)
                                    "اضغط احسب الشبكة"
                                else
                                    "Press Calculate Network",

                            color =
                                Color(0xFF9BA8B2)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultRow(
    title: String,
    value: String
) {

    Column {

        Text(
            title,
            color =
                Color(0xFF9BA8B2),
            fontSize = 11.sp
        )

        Text(
            value,
            color =
                Color.White,
            fontSize = 16.sp
        )
    }
}

private fun format(
    value: Double
): String =
    if (value.isFinite()) {
        "%.2f".format(value)
    } else {
        "-"
    }
