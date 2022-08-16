package com.turing.mediapager.ui

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.turing.lightbox.core.theme.LightBoxTheme
import com.turing.lightbox.repo.LocalMediaPagesRepo
import com.turing.mediapager.MediaPageDelegate
import com.turing.mediapager.MediaPagerDelegateImpl
import com.turing.mediapager.R
import com.turing.mediapager.domain.PhotoPage
import com.turing.mediapager.domain.VideoPage
import com.turing.mediapager.video.VideoPlayer
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun MediaPagerScreen(mediaPageDelegate: MediaPageDelegate) {

  val count by mediaPageDelegate.pageCountFlow.collectAsState()
  Box {
    if (count > 0) {
      MediaPager(mediaPageDelegate, count)

      SideArrowButton(
        Modifier.align(Alignment.CenterStart),
        R.drawable.chevron_left,
        mediaPageDelegate::moveBackward
      )
      SideArrowButton(
        Modifier.align(Alignment.CenterEnd),
        R.drawable.chevron_right,
        mediaPageDelegate::moveForward
      )
    } else PagerLoading()
  }
}

@Composable
private fun SideArrowButton(
  modifier: Modifier,
  @DrawableRes icon: Int,
  clickCallback: KSuspendFunction0<Unit>
) {

  val coroutineScope = rememberCoroutineScope()
  Image(
    painterResource(icon),
    contentDescription = "",
    contentScale = ContentScale.Inside,
    modifier = modifier
      .size(60.dp)
      .clickable {
        coroutineScope.launch {
          clickCallback()
        }
      }
  )
}

@OptIn(ExperimentalPagerApi::class)
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

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  LightBoxTheme {
    val context = LocalContext.current
    MediaPagerScreen(mediaPageDelegate = MediaPagerDelegateImpl(LocalMediaPagesRepo(context)))
  }
}

