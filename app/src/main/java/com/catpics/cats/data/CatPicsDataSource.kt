package com.catpics.cats.data

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.catpics.api.CatsApi
import com.catpics.model.Cat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatPicsDataSource(
    private val scope: CoroutineScope,
    private val service: CatsApi
) : PageKeyedDataSource<Int, Cat>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Cat>
    ) {
        scope.launch {
            try {
                val response = service.getCats(page = 1, limit = params.requestedLoadSize)
                when {
                    response.isSuccessful -> {
                        response.body()?.let {cats ->
                            callback.onResult(cats.toMutableList(), null, 2)
                        }
                    }
                }

            } catch (exception: Exception) {
                Log.e("CatsDataSource", "Failed to fetch data: ${exception.message}")
            }

        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Cat>) {
        scope.launch {
            try {
                val page = params.key
                val response = service.getCats(page = 1, limit = params.requestedLoadSize)
                when {
                    response.isSuccessful -> {
                        response.body()?.let {cats ->
                            callback.onResult(cats.toMutableList(), page + 1)
                        }
                    }
                }

            } catch (exception: Exception) {
                Log.e("CatsDataSource", "Failed to fetch data: ${exception.message}")
            }
        }

    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Cat>
    ) {
        //TODO
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}