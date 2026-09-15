package com.electricalengineeringpro.app.core.network

import com.electricalengineeringpro.app.core.model.NetworkElement

data class NetworkTreeNode(
    val element: NetworkElement,
    val children: List<NetworkTreeNode>
)

class NetworkTreeBuilder {

    fun build(
        elements: List<NetworkElement>
    ): List<NetworkTreeNode> {

        val byParent =
            elements.groupBy { it.parentId }

        fun createNode(
            element: NetworkElement
        ): NetworkTreeNode {

            val children =
                byParent[element.id]
                    .orEmpty()
                    .map(::createNode)

            return NetworkTreeNode(
                element = element,
                children = children
            )
        }

        return byParent[null]
            .orEmpty()
            .map(::createNode)
    }
}
