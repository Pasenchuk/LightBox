package com.turing.lightbox.mediapager

import com.turing.lightbox.domain.MediaPage
import com.turing.lightbox.domain.PhotoPage
import com.turing.lightbox.domain.VideoPage
import com.turing.lightbox.repo.MediaPagesRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.isActive
import javax.inject.Inject

interface MediaPageDelegate {

  val pageCountFlow: StateFlow<Int>
  val pagerActionFlow: Flow<MediaPagerAction>

  suspend fun loadPages()

  fun getPage(position: Int): MediaPage?

  suspend fun onPageSelected(position: Int)
  suspend fun onVideoEnded(position: Int)
}

@OptIn(ExperimentalCoroutinesApi::class)
class MediaPagerDelegateImpl @Inject constructor(private val mediaPagesRepo: MediaPagesRepo) : MediaPageDelegate {

  override val pageCountFlow: MutableStateFlow<Int> = MutableStateFlow(0)

  private var pages: List<MediaPage> = listOf()

  private val immediatePagerActionFlow: MutableSharedFlow<MediaPagerAction> = MutableSharedFlow()
  private val delayedPagerActionFlow: MutableSharedFlow<MediaPagerAction> = MutableSharedFlow()

  private val delayedPagerActionTimerFlow: Flow<MediaPagerAction> = delayedPagerActionFlow
    .flatMapLatest {
      when (it) {
        DoNoting -> flow { emit(DoNoting) }
        is MoveToPage -> flow {
          delay(PAGE_DISPlAY_TIMEOUT)
          if (currentCoroutineContext().isActive)
            emit(it)
        }
      }
    } // automatically scroll after timeout

  override val pagerActionFlow = merge(immediatePagerActionFlow, delayedPagerActionTimerFlow)

  override suspend fun loadPages() {
    pages = mediaPagesRepo.loadMediaPages()
    pageCountFlow.emit(INFINITE_PAGER_COUNT)

    immediatePagerActionFlow.emit(
      MoveToPage(
        getInitialPosition(),
        false
      )
    ) // Infinite pager pointed to zero item
  }

  override fun getPage(position: Int): MediaPage? {
    if (pages.isNotEmpty()) {
      val index = position % pages.size
      return pages[index]
    }
    return null // TODO: consider default page
  }

  override suspend fun onPageSelected(position: Int) {
    // TODO: consider analytics here
    getPage(position)?.let { page ->
      if (position == 0 || position == INFINITE_PAGER_COUNT - 1) { // Reset pager position when it is close to start or end
        resetPagerPosition(position)
      } else when (page) {
        is PhotoPage -> delayedPagerActionFlow.emit(MoveToPage(position + 1, true))
        is VideoPage -> delayedPagerActionFlow.emit(DoNoting)
      }
    }
  }

  private suspend fun resetPagerPosition(position: Int) {
    val index = position % pages.size
    immediatePagerActionFlow.emit(
      MoveToPage(
        getInitialPosition() + index,
        false
      )
    )
  }

  override suspend fun onVideoEnded(position: Int) {
    delay(100)
    immediatePagerActionFlow.emit(MoveToPage(position + 1, true))
  }

  private fun getInitialPosition() = CENTER_POSITION - (CENTER_POSITION % pages.size)

  companion object {

    private const val PAGE_DISPlAY_TIMEOUT = 15_00L
    private const val INFINITE_PAGER_COUNT = Int.MAX_VALUE - 1
    private const val CENTER_POSITION = INFINITE_PAGER_COUNT / 2
  }

}
