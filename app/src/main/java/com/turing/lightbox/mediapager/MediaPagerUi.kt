@file:OptIn(ExperimentalPagerApi::class)

package com.turing.lightbox.mediapager

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.turing.lightbox.R
import com.turing.lightbox.domain.PhotoPage
import com.turing.lightbox.domain.VideoPage
import com.turing.lightbox.video.VideoPlayer
import kotlinx.coroutines.launch

@Composable
fun MediaPagerScreen(mediaPageDelegate: MediaPageDelegate) {

  val coroutineScope = rememberCoroutineScope()

  val count by mediaPageDelegate.pageCountFlow.collectAsState()
  Box {
    if (count > 0) {
      MediaPager(mediaPageDelegate, count)

      Image(
        painterResource(R.drawable.chevron_left),
        contentDescription = "",
        contentScale = ContentScale.Inside,
        modifier = Modifier
          .align(Alignment.CenterStart)
          .size(60.dp)
          .clickable {
            coroutineScope.launch {
              mediaPageDelegate.moveBackward()
            }
          }
      )
      Image(
        painterResource(R.drawable.chevron_right),
        contentDescription = "",
        contentScale = ContentScale.Inside,
        modifier = Modifier
          .align(Alignment.CenterEnd)
          .size(60.dp)
          .clickable {
            coroutineScope.launch {
              mediaPageDelegate.moveForward()
            }
          }
      )
    } else PagerLoading()
  }
}

@Composable
private fun MediaPager(mediaPageDelegate: MediaPageDelegate, count: Int) {

  val coroutineScope = rememberCoroutineScope()

  val pagerState = rememberPagerState()

  LaunchedEffect(pagerState) {
    // Collect from the pager state a snapshotFlow reading the currentPage
    snapshotFlow { pagerState.currentPage }.collect { page ->
      mediaPageDelegate.onPageSelected(page)
    }
  }

  HorizontalPager(
    count = count,
    state = pagerState,
  ) { position ->
    MediaPage(mediaPageDelegate, position)
  }

  coroutineScope.launch {
    mediaPageDelegate.pagerActionFlow.collect { action ->
      if (action.animated) pagerState.animateScrollToPage(action.position)
      else pagerState.scrollToPage(action.position)
    }
  }
}

@Composable
private fun MediaPage(mediaPageDelegate: MediaPageDelegate, position: Int) {
  when (val page = mediaPageDelegate.getPage(position)) {
    is PhotoPage -> PhotoPageUI(page)
    is VideoPage -> VideoPlayer(uri = page.uri)
    null -> PagerLoading()
  }
}

@Composable
fun PhotoPageUI(page: PhotoPage) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(page.uri)
      .crossfade(true)
      .build(),
    contentDescription = stringResource(R.string.description),
    contentScale = ContentScale.Crop,
  )
}

@Composable
private fun PagerLoading() {
  Text(
    text = stringResource(R.string.loading),
    modifier = Modifier.fillMaxWidth()
  )
}
