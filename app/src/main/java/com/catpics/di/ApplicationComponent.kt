package com.catpics.di

import android.app.Application
import android.content.Context
import com.catpics.CatPicsApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, NetworkModule::class, CatsModule::class])
interface ApplicationComponent : AndroidInjector<CatPicsApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}