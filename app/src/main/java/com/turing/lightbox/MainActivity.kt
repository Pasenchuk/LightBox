package com.turing.lightbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.turing.mediapager.ui.MediaPagerScreen
import com.turing.lightbox.ui.theme.LightBoxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


  val mainVM: MainVM by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LightBoxTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          mainVM.init()
          Greeting(mainVM)
        }
      }
    }
  }
}

@Composable
fun Greeting(mainVM: MainVM) {
  Box {
    MediaPagerScreen(mediaPageDelegate = mainVM)

    Column {
      FlowRow(
        mainAxisAlignment = FlowMainAxisAlignment.Center,
      ) {
        Text(text = "aaa", Modifier.padding(15.dp))
        Text(text = "bbbbbbb", Modifier.padding(15.dp))
        Text(text = "cccccccccccc", Modifier.padding(15.dp))
        Text(text = "eee", Modifier.padding(15.dp))
        Text(text = "eeeffffffffff", Modifier.padding(15.dp))
        Text(text = "DDDDDDDDDDDDDDDDDDDDDDDDDD", Modifier.padding(15.dp))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  LightBoxTheme {
//    Greeting("Android")
  }
}
