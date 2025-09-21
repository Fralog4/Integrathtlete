package com.app.integrathlete.model

data class Form(
    val name: String,
    val quality: String
)

data class Study(
    val title: String,
    val link: String
)

data class Product(
    val brand: String,
    val form: String,
    val price: Double,
    val link: String
)

data class Supplement(
    val id: Int,
    val name: String,
    val forms: List<Form>,
    val sport: List<String>,
    val description: String,
    val studies: List<Study>,
    val products: List<Product>
)
