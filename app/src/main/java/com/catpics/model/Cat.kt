package com.catpics.model

data class Cat(
    val breeds: List<Breed>?,
    val categories: List<Category>?,
    val id: String,
    val url: String
)
