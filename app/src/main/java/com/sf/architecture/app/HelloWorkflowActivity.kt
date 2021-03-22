package com.sf.architecture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sf.architecture.domain.RealMessageService
import com.sf.architecture.ui.HelloScreen
import com.sf.architecture.workflow.HelloWorkflow
import com.squareup.workflow1.asWorker
import com.squareup.workflow1.ui.WorkflowLayout
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.flow.StateFlow

@OptIn(WorkflowUiExperimentalApi::class)
class HelloWorkflowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This ViewModel will survive configuration changes. It's instantiated
        // by the first call to viewModels(), and that original instance is returned by
        // succeeding calls.
        val model: HelloViewModel by viewModels()
        setContentView(
            WorkflowLayout(this).apply { start(model.renderings) }
        )
    }
}

class HelloViewModel(savedState: SavedStateHandle) : ViewModel() {
    @OptIn(WorkflowUiExperimentalApi::class)
    val renderings: StateFlow<HelloScreen> by lazy {
        renderWorkflowIn(
            workflow = HelloWorkflow(RealMessageService().messageStateFlow.asWorker()),
            scope = viewModelScope,
            savedStateHandle = savedState
        )
    }
}


