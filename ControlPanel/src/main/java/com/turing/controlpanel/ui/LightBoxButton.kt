package com.turing.controlpanel.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraLight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turing.controlpanel.R
import com.turing.controlpanel.domain.ButtonModel
import com.turing.lightbox.core.theme.LightBoxTheme

@Composable
fun LightBoxButton(buttonModel: ButtonModel) {
  val configuration = LocalConfiguration.current

  OutlinedButton(
    border = BorderStroke(1.dp, Color.Blue),
    modifier = Modifier
      .width(configuration.screenWidthDp.dp * buttonModel.screenPercentage)
      .padding(5.dp),
    onClick = {}
  ) {

    Icon(
      imageVector = buttonModel.icon,
      contentDescription = "Phone Icon",
      modifier = Modifier.size(ButtonDefaults.IconSize)
    )
    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
    Column {
      Text(stringResource(buttonModel.caption))
      Text(buttonModel.subtext, fontWeight = ExtraLight)
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  LightBoxTheme {
    Column(Modifier.fillMaxWidth()) {
      LightBoxButton(ButtonModel(Icons.Filled.AccountBox, R.string.login, 0.5f, "Awesome"))
      LightBoxButton(ButtonModel(Icons.Filled.List, R.string.price_list, 0.75f, "Awesome"))
      LightBoxButton(ButtonModel(Icons.Filled.Add, R.string.subscribtions, 0.75f, "Awesome"))
      LightBoxButton(ButtonModel(Icons.Filled.Phone, R.string.call_us, 0.4f, "Awesome"))
      LightBoxButton(ButtonModel(Icons.Filled.Build, R.string.preferences, 0.4f, "Awesome"))
    }
  }
}
