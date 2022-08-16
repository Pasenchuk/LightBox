package com.turing.lightbox

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
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
import androidx.core.view.WindowCompat
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

    hideSystemUI()
  }

  fun hideSystemUI() {

    //Hides the ugly action bar at the top
    actionBar?.hide()

    //Hide the status bars

    WindowCompat.setDecorFitsSystemWindows(window, false)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
      window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
      window.insetsController?.apply {
        hide(WindowInsets.Type.statusBars())
        systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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
