package com.turing.controlpanel.repo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import com.turing.controlpanel.R
import com.turing.controlpanel.domain.ButtonModel
import javax.inject.Inject

interface ControlButtonRepo {

  suspend fun loadButtons(): List<ButtonModel>
}

class ControlButtonRepoImpl @Inject constructor() : ControlButtonRepo {

  override suspend fun loadButtons(): List<ButtonModel> = listOf(
    ButtonModel(Icons.Filled.AccountBox, R.string.login, 0.5f, "Awesome"),
    ButtonModel(Icons.Filled.List, R.string.price_list, 0.75f, "Awesome"),
    ButtonModel(Icons.Filled.Add, R.string.subscribtions, 0.75f, "Awesome"),
    ButtonModel(Icons.Filled.Phone, R.string.call_us, 0.4f, "Awesome"),
    ButtonModel(Icons.Filled.Build, R.string.preferences, 0.4f, "Awesome"),
  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    return true
  }

  override fun hashCode(): Int {
    return javaClass.hashCode()
  }
}