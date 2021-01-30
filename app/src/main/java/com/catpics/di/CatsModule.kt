package com.catpics.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.catpics.cats.CatPicsFragment
import com.catpics.cats.CatPicsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CatsModule {

    @Binds
    internal abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory) : ViewModelProvider.Factory

    @ContributesAndroidInjector
    internal abstract fun firstFragment(): CatPicsFragment

    @Binds
    @IntoMap
    @ViewModelKey(CatPicsViewModel::class)
    abstract fun bindsCatPicsViewModel(viewModel: CatPicsViewModel): ViewModel
}
