package com.turing.controlpanel

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.turing.controlpanel.domain.ButtonModel
import com.turing.controlpanel.ui.LightBoxButton
import com.turing.lightbox.core.theme.LightBoxTheme

@Composable
fun ControlPanelUI(controlsDelegate: ControlsDelegate, modifier: Modifier) {
  val buttons by controlsDelegate.buttonsFlow.collectAsState()
  ControlPanelUI(modifier, buttons)
}

@Composable
private fun ControlPanelUI(
  modifier: Modifier,
  buttons: List<ButtonModel>
) {
  FlowRow(
    modifier = modifier.fillMaxHeight(0.35f),
    mainAxisAlignment = FlowMainAxisAlignment.Center,
  ) {

    buttons.forEach {
      LightBoxButton(it)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  LightBoxTheme {
    ControlPanelUI(
      Modifier.fillMaxHeight(), listOf(
        ButtonModel(Icons.Filled.AccountBox, R.string.login, 0.5f),
        ButtonModel(Icons.Filled.List, R.string.price_list, 0.75f),
        ButtonModel(Icons.Filled.Add, R.string.subscribtions, 0.75f),
        ButtonModel(Icons.Filled.Phone, R.string.call_us, 0.4f),
        ButtonModel(Icons.Filled.Build, R.string.preferences, 0.4f),
      )
    )
  }
}
