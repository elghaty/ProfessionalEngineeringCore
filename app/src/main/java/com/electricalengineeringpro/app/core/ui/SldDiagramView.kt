package com.electricalengineeringpro.app.core.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.electricalengineeringpro.app.core.sld.SldNode
import com.electricalengineeringpro.app.core.sld.SldSymbolType
import com.electricalengineeringpro.app.core.sld.SingleLineDiagram
import kotlin.math.max

class SldDiagramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(
    context,
    attrs
) {

    private var diagram: SingleLineDiagram? = null

    private val linePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = 0xFF263238.toInt()
        }

    private val boxPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = 0xFF1565C0.toInt()
        }

    private val fillPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = 0xFFF4F7FB.toInt()
        }

    private val textPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = 0xFF263238.toInt()
            textSize = 28f
        }

    private val titlePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = 0xFF0B2942.toInt()
            textSize = 34f
            isFakeBoldText = true
        }

    private val smallPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = 0xFF455A64.toInt()
            textSize = 22f
        }

    private val labelPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = 0xFF0D47A1.toInt()
            textSize = 22f
            isFakeBoldText = true
        }

    fun setDiagram(
        value: SingleLineDiagram
    ) {

        diagram = value

        val count =
            max(
                value.nodes.size,
                1
            )

        layoutParams =
            layoutParams?.apply {
                height =
                    dp(
                        210 * count +
                            100
                    )
            }

        requestLayout()
        invalidate()
    }

    override fun onDraw(
        canvas: Canvas
    ) {

        super.onDraw(canvas)

        canvas.drawColor(
            0xFFFFFFFF.toInt()
        )

        val current =
            diagram
                ?: return

        canvas.save()

        val centerX =
            width / 2f

        canvas.drawText(
            "SINGLE LINE DIAGRAM",
            24f,
            42f,
            titlePaint
        )

        val positions =
            current.nodes.associate {
                it.id to
                    Pair(
                        centerX,
                        110f +
                            it.y
                    )
            }

        /*
         * Connections first.
         */
        current.connections.forEach { connection ->

            val from =
                positions[connection.fromId]

            val to =
                positions[connection.toId]

            if (
                from != null &&
                to != null
            ) {

                val x =
                    from.first

                val y1 =
                    from.second + 45f

                val y2 =
                    to.second - 45f

                canvas.drawLine(
                    x,
                    y1,
                    x,
                    y2,
                    linePaint
                )

                if (
                    connection.label.isNotBlank()
                ) {

                    canvas.drawText(
                        connection.label,
                        x + 20f,
                        (y1 + y2) / 2f,
                        labelPaint
                    )
                }
            }
        }

        /*
         * Nodes.
         */
        current.nodes.forEach { node ->

            val position =
                positions[node.id]
                    ?: return@forEach

            drawNode(
                canvas,
                node,
                position.first,
                position.second
            )
        }

        canvas.restore()
    }

    private fun drawNode(
        canvas: Canvas,
        node: SldNode,
        x: Float,
        y: Float
    ) {

        when (node.type) {

            SldSymbolType.TRANSFORMER ->
                drawTransformer(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.GENERATOR ->
                drawGenerator(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.BREAKER ->
                drawBreaker(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.CABLE ->
                drawCable(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.MAIN_SWITCHBOARD,
            SldSymbolType.PANEL,
            SldSymbolType.MCC ->
                drawPanel(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.LOAD ->
                drawLoad(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.MOTOR ->
                drawMotor(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.PUMP ->
                drawPump(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.UTILITY ->
                drawUtility(
                    canvas,
                    x,
                    y
                )

            SldSymbolType.EARTH ->
                drawEarth(
                    canvas,
                    x,
                    y
                )
        }

        drawNodeText(
            canvas,
            node,
            x,
            y
        )
    }

    private fun drawTransformer(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        canvas.drawCircle(
            x - 24f,
            y,
            30f,
            boxPaint
        )

        canvas.drawCircle(
            x + 24f,
            y,
            30f,
            boxPaint
        )
    }

    private fun drawGenerator(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        canvas.drawCircle(
            x,
            y,
            42f,
            boxPaint
        )

        canvas.drawText(
            "G",
            x - 13f,
            y + 13f,
            titlePaint
        )
    }

    private fun drawBreaker(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        val rect =
            RectF(
                x - 40f,
                y - 32f,
                x + 40f,
                y + 32f
            )

        canvas.drawRect(
            rect,
            fillPaint
        )

        canvas.drawRect(
            rect,
            boxPaint
        )

        canvas.drawLine(
            x - 22f,
            y + 22f,
            x + 22f,
            y - 22f,
            boxPaint
        )
    }

    private fun drawCable(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        canvas.drawCircle(
            x,
            y,
            22f,
            boxPaint
        )

        canvas.drawCircle(
            x,
            y,
            9f,
            boxPaint
        )
    }

    private fun drawPanel(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        val rect =
            RectF(
                x - 60f,
                y - 40f,
                x + 60f,
                y + 40f
            )

        canvas.drawRect(
            rect,
            fillPaint
        )

        canvas.drawRect(
            rect,
            boxPaint
        )

        canvas.drawLine(
            x - 35f,
            y,
            x + 35f,
            y,
            boxPaint
        )

        canvas.drawLine(
            x,
            y - 25f,
            x,
            y + 25f,
            boxPaint
        )
    }

    private fun drawLoad(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        val path =
            Path()

        path.moveTo(
            x,
            y - 45f
        )

        path.lineTo(
            x + 45f,
            y
        )

        path.lineTo(
            x,
            y + 45f
        )

        path.lineTo(
            x - 45f,
            y
        )

        path.close()

        canvas.drawPath(
            path,
            fillPaint
        )

        canvas.drawPath(
            path,
            boxPaint
        )
    }

    private fun drawMotor(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        canvas.drawCircle(
            x,
            y,
            42f,
            boxPaint
        )

        canvas.drawText(
            "M",
            x - 13f,
            y + 13f,
            titlePaint
        )
    }

    private fun drawPump(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        canvas.drawCircle(
            x,
            y,
            42f,
            boxPaint
        )

        canvas.drawText(
            "P",
            x - 13f,
            y + 13f,
            titlePaint
        )
    }

    private fun drawUtility(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        canvas.drawCircle(
            x,
            y,
            40f,
            boxPaint
        )

        canvas.drawText(
            "~",
            x - 10f,
            y + 12f,
            titlePaint
        )
    }

    private fun drawEarth(
        canvas: Canvas,
        x: Float,
        y: Float
    ) {

        canvas.drawLine(
            x,
            y - 30f,
            x,
            y,
            linePaint
        )

        canvas.drawLine(
            x - 30f,
            y,
            x + 30f,
            y,
            linePaint
        )

        canvas.drawLine(
            x - 20f,
            y + 12f,
            x + 20f,
            y + 12f,
            linePaint
        )

        canvas.drawLine(
            x - 10f,
            y + 24f,
            x + 10f,
            y + 24f,
            linePaint
        )
    }

    private fun drawNodeText(
        canvas: Canvas,
        node: SldNode,
        x: Float,
        y: Float
    ) {

        val nameY =
            y + 78f

        canvas.drawText(
            node.name,
            x + 75f,
            nameY,
            titlePaint
        )

        var dataY =
            nameY + 28f

        node.electricalData
            .entries
            .forEach { entry ->

                canvas.drawText(
                    "${entry.key}: ${entry.value}",
                    x + 75f,
                    dataY,
                    smallPaint
                )

                dataY += 25f
            }
    }

    private fun dp(
        value: Int
    ): Int {

        return (
            value *
                resources.displayMetrics.density
            ).toInt()
    }
}
