package com.example.taskfromcompany.di

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.example.taskfromcompany.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named


@Module
@InstallIn(ActivityComponent::class)
object MainModule {


    @ActivityScoped
    @Named("String 2")
    @Provides
    fun provideString(
        @ApplicationContext context: Context,
        @Named("String 1") str1: String
    ) =
        context.getString(R.string.string_from_di) + " $str1"



}
