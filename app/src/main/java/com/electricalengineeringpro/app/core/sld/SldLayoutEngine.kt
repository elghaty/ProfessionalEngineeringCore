package com.electricalengineeringpro.app.core.sld

/**
 * Automatic vertical SLD layout.
 *
 * The electrical sequence remains:
 *
 * SOURCE
 *    |
 * BREAKER
 *    |
 * CABLE
 *    |
 * PANEL
 *    |
 * LOAD
 */
class SldLayoutEngine {

    fun layout(
        diagram: SingleLineDiagram,
        verticalSpacing: Float = 170f
    ): SingleLineDiagram {

        require(
            verticalSpacing > 0f
        ) {
            "Vertical spacing must be greater than zero."
        }

        val laidOutNodes =
            diagram.nodes.mapIndexed { index, node ->

                node.copy(
                    x = 0f,
                    y = index *
                        verticalSpacing
                )
            }

        return diagram.copy(
            nodes =
                laidOutNodes
        )
    }
}
