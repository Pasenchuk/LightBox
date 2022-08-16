package com.turing.lightbox.app

import android.content.Context
import com.turing.lightbox.mediapager.MediaPageDelegate
import com.turing.lightbox.mediapager.MediaPagerDelegateImpl
import com.turing.lightbox.repo.LocalMediaPagesRepo
import com.turing.lightbox.repo.MediaPagesRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

  @Singleton
  @Binds
  fun bindContext(@ApplicationContext appContext: Context): Context

  @Singleton
  @Binds
  fun bindMediaPagerDelegate(impl: MediaPagerDelegateImpl): MediaPageDelegate

  @Singleton
  @Binds
  fun bindMediaPagesRepo(impl: LocalMediaPagesRepo): MediaPagesRepo

}
