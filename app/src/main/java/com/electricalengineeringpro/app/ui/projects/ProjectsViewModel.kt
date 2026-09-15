package com.electricalengineeringpro.app.ui.projects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.electricalengineeringpro.app.data.AppDatabase
import com.electricalengineeringpro.app.data.ProjectEntity
import com.electricalengineeringpro.app.data.ProjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ProjectRepository(
        AppDatabase.get(application).projectDao()
    )

    val projects = repository.observeProjects()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun createProject(
        name: String,
        client: String,
        location: String
    ) {
        if (name.isBlank()) return

        viewModelScope.launch {
            repository.save(
                ProjectEntity(
                    name = name.trim(),
                    client = client.trim(),
                    location = location.trim(),
                    engineer = "Eng. Abdelraouf Elghaty",
                    voltage = 400.0,
                    frequency = 50.0,
                    phase = "THREE",
                    source = "UTILITY"
                )
            )
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.delete(project)
        }
    }
}
