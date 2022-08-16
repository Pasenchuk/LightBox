package com.turing.mediapager

import com.turing.lightbox.repo.MediaPagesRepo
import com.turing.mediapager.domain.MediaPage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface MediaPageDelegate {

  val pageCountFlow: StateFlow<Int>
  val pagerActionFlow: Flow<MediaPagerMoveAction>

  suspend fun loadPages()

  fun getPage(position: Int): MediaPage?

  suspend fun onPageSelected(position: Int)

  suspend fun moveForward()

  suspend fun moveBackward()
}

@OptIn(ExperimentalCoroutinesApi::class)
class MediaPagerDelegateImpl @Inject constructor(private val mediaPagesRepo: MediaPagesRepo) : MediaPageDelegate {

  override val pageCountFlow: MutableStateFlow<Int> = MutableStateFlow(0)

  private var pages: List<MediaPage> = listOf()

  override val pagerActionFlow: MutableSharedFlow<MediaPagerMoveAction> = MutableSharedFlow()

  var position: Int = 0

  override suspend fun loadPages() {
    pages = mediaPagesRepo.loadMediaPages()
    pageCountFlow.emit(INFINITE_PAGER_COUNT)

    position = getInitialPosition()
    pagerActionFlow.emit(
      MediaPagerMoveAction(
        position,
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
    this.position = position
    // TODO: consider analytics here
    getPage(position)?.let {
      if (position == 0 || position == INFINITE_PAGER_COUNT - 1) {
        // Reset pager position when it is close to start or end
        resetPagerPosition(position)
      }
    }
  }

  override suspend fun moveForward() {
    pagerActionFlow.emit(
      MediaPagerMoveAction(
        position + 1,
        true
      )
    )
  }

  override suspend fun moveBackward() {
    if (position == 1) resetPagerPosition(position - 1)
    else pagerActionFlow.emit(
      MediaPagerMoveAction(
        position - 1,
        true
      )
    )
  }

  private suspend fun resetPagerPosition(position: Int) {
    val index = position % pages.size
    pagerActionFlow.emit(
      MediaPagerMoveAction(
        getInitialPosition() + index,
        false
      )
    )
  }

  private fun getInitialPosition() = CENTER_POSITION - (CENTER_POSITION % pages.size)

  companion object {

    private const val PAGE_DISPlAY_TIMEOUT = 15_00L
    private const val INFINITE_PAGER_COUNT = Int.MAX_VALUE - 1
    private const val CENTER_POSITION = INFINITE_PAGER_COUNT / 2
  }

}
