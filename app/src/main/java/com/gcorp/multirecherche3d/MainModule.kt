package com.gcorp.multirecherche3d

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideCustomTabsIntent(@ApplicationContext context: Context): CustomTabsIntent =
        CustomTabsIntent
            .Builder()
            .setShowTitle(true)
            .setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .build()
}