package com.turing.lightbox.controls

import kotlinx.coroutines.flow.Flow

interface ControlsDelegate {
  val buttonsFlow: Flow<List<ButtonModel>>

  fun loadButtons()
}