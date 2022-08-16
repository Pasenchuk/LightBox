package com.turing.lightbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turing.mediapager.MediaPageDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MainVM @Inject constructor(mediaPageDelegate: com.turing.mediapager.MediaPageDelegate) :
  ViewModel(), com.turing.mediapager.MediaPageDelegate by mediaPageDelegate {
    fun init() {
      viewModelScope.launch {
        loadPages()
      }
    }
}

