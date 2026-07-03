package com.example.mini_uber_eats_app.data

import java.util.UUID

enum class RestaurantCategory(val label: String) {
    FastFood("Fast food"),
    Cafe("Cafe"),
    FineDining("Fine dining")
}

data class Restaurant(
    val id: String,
    val name: String,
    val category: RestaurantCategory,
    val cuisine: String,
    val address: String,
    val phone: String,
    val rating: Int,
    val deliveryMinutes: Int,
    val description: String,
)

data class RestaurantFormState(
    val name: String = "",
    val category: RestaurantCategory = RestaurantCategory.Cafe,
    val cuisine: String = "",
    val address: String = "",
    val phone: String = "",
    val rating: String = "5",
    val deliveryMinutes: String = "30",
    val description: String = "",
)

fun Restaurant.toFormState(): RestaurantFormState = RestaurantFormState(
    name = name,
    category = category,
    cuisine = cuisine,
    address = address,
    phone = phone,
    rating = rating.toString(),
    deliveryMinutes = deliveryMinutes.toString(),
    description = description,
)

fun RestaurantFormState.toRestaurant(existing: Restaurant? = null): Restaurant {
    val parsedRating = rating.toIntOrNull()?.coerceIn(1, 5) ?: 5
    val parsedDeliveryMinutes = deliveryMinutes.toIntOrNull()?.coerceAtLeast(5) ?: 30

    return Restaurant(
        id = existing?.id ?: UUID.randomUUID().toString(),
        name = name.trim(),
        category = existing?.category ?: category,
        cuisine = cuisine.trim(),
        address = address.trim(),
        phone = phone.trim(),
        rating = parsedRating,
        deliveryMinutes = parsedDeliveryMinutes,
        description = description.trim(),
    )
}