package com.turing.lightbox.video

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(exoPlayer: ExoPlayer, uri: Uri, videoEndedCallback: () -> Unit = {}) {
  val context = LocalContext.current

  val defaultDataSourceFactory = DefaultDataSource.Factory(context)
  val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
    context,
    defaultDataSourceFactory
  )
  val source = ProgressiveMediaSource.Factory(dataSourceFactory)
    .createMediaSource(MediaItem.fromUri(uri))

  exoPlayer.setMediaSource(source)

  exoPlayer.playWhenReady = true

  exoPlayer.prepare()

  exoPlayer.addListener(object : Player.Listener {

    override fun onPlaybackStateChanged(state: Int) {
      if (state == Player.STATE_ENDED) videoEndedCallback()
    }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
          if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT) videoEndedCallback()
        }
  })

  DisposableEffect(
    AndroidView(factory = {
      PlayerView(context).apply {
        hideController()
        useController = false
        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        player = exoPlayer
        layoutParams =
          FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

      }

    })
  ) {
    onDispose { exoPlayer.release() }
  }
}

