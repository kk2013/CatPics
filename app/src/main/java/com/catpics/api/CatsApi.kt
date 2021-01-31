package com.catpics.api

import com.catpics.model.Cat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {

    @GET("/v1/images/search")
    suspend fun getCats(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("mime_types") mime_types: List<String> = listOf("gif"),
        @Query("order") order: String = "DESC"
    ): Response<List<Cat>>
}
