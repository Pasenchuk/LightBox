package com.turing.controlpanel.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

class ButtonModel(
  val icon: ImageVector,
  @StringRes val caption: Int,
  val screenPercentage: Float,
  val subtext: String = "",
)