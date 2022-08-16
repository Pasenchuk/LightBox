package com.turing.lightbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turing.lightbox.mediapager.MediaPageDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class MainVM @Inject constructor(mediaPageDelegate: MediaPageDelegate) :
  ViewModel(), MediaPageDelegate by mediaPageDelegate {
    fun init() {
      viewModelScope.launch {
        loadPages()
      }
    }
}

