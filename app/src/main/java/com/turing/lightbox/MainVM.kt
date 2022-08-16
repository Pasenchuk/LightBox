package com.turing.lightbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turing.controlpanel.ControlsDelegate
import com.turing.mediapager.MediaPageDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MainVM @Inject constructor(
  mediaPageDelegate: MediaPageDelegate,
  controlsDelegate: ControlsDelegate,
) :
  ViewModel(), MediaPageDelegate by mediaPageDelegate, ControlsDelegate by controlsDelegate {

  fun init() {
    viewModelScope.launch {
      loadPages()
      loadButtons()
    }
  }
}

