package com.electricalengineeringpro.app.core.sld

class SldLayoutEngine {

    fun layout(
        diagram: SingleLineDiagram,
        verticalSpacing: Float = 150f
    ): SingleLineDiagram {

        val laidOutNodes =
            diagram.nodes.mapIndexed { index, node ->
                node.copy(
                    x = 0f,
                    y = index * verticalSpacing
                )
            }

        return diagram.copy(
            nodes = laidOutNodes
        )
    }
}
