package com.catpics.cats


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.catpics.api.CatsApi
import com.catpics.cats.data.CatPicsDataSource
import com.catpics.model.Cat
import javax.inject.Inject

class CatPicsViewModel @Inject constructor(private val catsApi: CatsApi) : ViewModel() {

    var catPicsLiveData: LiveData<PagedList<Cat>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()
        catPicsLiveData = initializedPagedListBuilder(config).build()
    }

    fun getCatPics(): LiveData<PagedList<Cat>> = catPicsLiveData

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Cat> {

        val dataSourceFactory = object : DataSource.Factory<Int, Cat>() {
            override fun create(): DataSource<Int, Cat> {
                return CatPicsDataSource(viewModelScope, catsApi)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}