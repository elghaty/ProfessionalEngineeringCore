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

        val byId =
            elements.associateBy {
                it.id
            }

        val childrenByParent =
            elements
                .filter {
                    it.parentId != null &&
                        byId.containsKey(it.parentId)
                }
                .groupBy {
                    it.parentId
                }

        fun createNode(
            element: NetworkElement,
            visiting: Set<String>
        ): NetworkTreeNode {

            if (element.id in visiting) {
                return NetworkTreeNode(
                    element = element,
                    children = emptyList()
                )
            }

            val nextVisiting =
                visiting + element.id

            val children =
                childrenByParent[element.id]
                    .orEmpty()
                    .map {
                        createNode(
                            element = it,
                            visiting = nextVisiting
                        )
                    }

            return NetworkTreeNode(
                element = element,
                children = children
            )
        }

        return elements
            .filter {
                it.parentId == null ||
                    !byId.containsKey(it.parentId)
            }
            .map {
                createNode(
                    element = it,
                    visiting = emptySet()
                )
            }
    }
}
