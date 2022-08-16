package com.turing.lightbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.turing.controlpanel.ControlPanelUI
import com.turing.lightbox.core.theme.LightBoxTheme
import com.turing.mediapager.ui.MediaPagerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  val mainVM: MainVM by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainVM.init()
    setContent {
      LightBoxTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          MainUI(mainVM)
        }
      }
    }
  }
}

@Composable
fun MainUI(mainVM: MainVM) {
  Box {
    MediaPagerScreen(mediaPageDelegate = mainVM)
    ControlPanelUI(controlsDelegate = mainVM, modifier = Modifier.align(Alignment.BottomCenter))
  }
}
