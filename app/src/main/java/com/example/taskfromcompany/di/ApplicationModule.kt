package com.example.taskfromcompany.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {


     @Singleton
     @Provides
     @Named("String 1")
     fun provideString()="This is a string 1 from DI"




}