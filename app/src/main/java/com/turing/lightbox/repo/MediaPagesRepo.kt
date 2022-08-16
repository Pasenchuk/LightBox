package com.turing.lightbox.repo

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.turing.lightbox.R
import com.turing.lightbox.domain.MediaPage
import com.turing.lightbox.domain.PhotoPage
import com.turing.lightbox.domain.VideoPage
import javax.inject.Inject

interface MediaPagesRepo {

  suspend fun loadMediaPages(): List<MediaPage>
}

class LocalMediaPagesRepo @Inject constructor(private val context: Context) : MediaPagesRepo {

  override suspend fun loadMediaPages(): List<MediaPage> {
    // assume this as a cache
    return listOf(
      VideoPage(R.raw.media_1.toUri()),
      PhotoPage(R.raw.media_2.toUri()),
      VideoPage(R.raw.media_3.toUri()),
      PhotoPage(R.raw.media_4.toUri())
    )
  }

  private fun Int.toUri(): Uri {
    return Uri.Builder()
      .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
      .authority(context.packageName)
      .path(this.toString())
      .build()
  }

}