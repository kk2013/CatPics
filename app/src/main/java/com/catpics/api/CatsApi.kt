package com.catpics.api

import com.catpics.model.Cat
import com.catpics.model.Cats
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {

    @GET("/v1/images/search")
    suspend fun getCats(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<Cat>>
}