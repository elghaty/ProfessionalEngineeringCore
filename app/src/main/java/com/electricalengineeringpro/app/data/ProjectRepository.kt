package com.electricalengineeringpro.app.data

import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val dao: ProjectDao
) {

    fun observeProjects(): Flow<List<ProjectEntity>> {
        return dao.observeProjects()
    }

    suspend fun save(project: ProjectEntity): Long {
        return dao.insert(project)
    }

    suspend fun delete(project: ProjectEntity) {
        dao.delete(project)
    }

    suspend fun get(id: Long): ProjectEntity? {
        return dao.get(id)
    }
}
