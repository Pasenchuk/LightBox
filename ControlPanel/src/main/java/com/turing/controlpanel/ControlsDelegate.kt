package com.turing.controlpanel

import com.turing.controlpanel.domain.ButtonModel
import com.turing.controlpanel.repo.ControlButtonRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface ControlsDelegate {

  val buttonsFlow: StateFlow<List<ButtonModel>>

  suspend fun loadButtons()
}

class ControlsDelegateImpl @Inject constructor(private val controlButtonRepo: ControlButtonRepo) : ControlsDelegate {

  override val buttonsFlow: MutableStateFlow<List<ButtonModel>> = MutableStateFlow(listOf())

  override suspend fun loadButtons() {
    buttonsFlow.emit(controlButtonRepo.loadButtons())
  }

}