@file:OptIn(ExperimentalPagerApi::class)

package com.turing.lightbox.mediapager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
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
  val count by mediaPageDelegate.pageCountFlow.collectAsState()
  Box {
    if (count > 0) {
      MediaPager(mediaPageDelegate, count)
    } else PagerLoading()
  }
}

@Composable
private fun MediaPager(mediaPageDelegate: MediaPageDelegate, count: Int) {

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val pagerState = rememberPagerState()

  LaunchedEffect(pagerState) {
    // Collect from the pager state a snapshotFlow reading the currentPage
    snapshotFlow { pagerState.currentPage }.collect { page ->
      mediaPageDelegate.onPageSelected(page)
    }
  }

  val exoPlayer = remember {
    ExoPlayer.Builder(context)
      .build()
      .apply {
        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        repeatMode = Player.REPEAT_MODE_ONE
        prepare()
      }
  }

  HorizontalPager(
    count = count,
    state = pagerState,
  ) { position ->
    MediaPage(mediaPageDelegate, exoPlayer, position)
  }


  coroutineScope.launch {
    mediaPageDelegate.pagerActionFlow.collect { action ->
      when (action) {
        DoNoting -> {} // nothing
        is MoveToPage -> {
          if (action.animated) pagerState.animateScrollToPage(action.position)
          else pagerState.scrollToPage(action.position)
        }
      }
    }
  }
}


@Composable
private fun MediaPage(mediaPageDelegate: MediaPageDelegate, exoPlayer: ExoPlayer, position: Int) {
  val page = mediaPageDelegate.getPage(position)
  when (page) {
    is PhotoPage -> PhotoPageUI(page)
    is VideoPage -> VideoPageUI(page, position, mediaPageDelegate, exoPlayer)
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
fun VideoPageUI(page: VideoPage, position: Int, mediaPageDelegate: MediaPageDelegate, exoPlayer: ExoPlayer) {
  val coroutineScope = rememberCoroutineScope()
  VideoPlayer(uri = page.uri, exoPlayer = exoPlayer) {
    coroutineScope.launch {
      mediaPageDelegate.onVideoEnded(position)
    }
  }
}

@Composable
private fun PagerLoading() {
  Text(
    text = stringResource(R.string.loading),
    modifier = Modifier.fillMaxWidth()
  )
}
