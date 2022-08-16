package com.turing.lightbox.domain

import android.net.Uri

sealed class MediaPage(val uri: Uri)

class PhotoPage(uri: Uri): MediaPage(uri)
class VideoPage(uri: Uri): MediaPage(uri)
