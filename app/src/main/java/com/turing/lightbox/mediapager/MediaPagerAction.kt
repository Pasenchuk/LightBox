package com.turing.lightbox.mediapager

sealed class MediaPagerAction
class MoveToPage(val position: Int, val animated: Boolean) : MediaPagerAction()
object DoNoting : MediaPagerAction()