package com.example.mini_uber_eats_app.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class RestaurantRepository(context: Context) {
    private val file = File(context.filesDir, "restaurants.json")

    var restaurants by mutableStateOf<List<Restaurant>>(emptyList())
        private set

    init {
        restaurants = readRestaurants()
        if (restaurants.isEmpty() && !file.exists()) {
            restaurants = defaultRestaurants()
            persist()
        }
    }

    fun getRestaurant(id: String?): Restaurant? = restaurants.firstOrNull { it.id == id }

    fun saveRestaurant(formState: RestaurantFormState, existingId: String? = null): Restaurant {
        val current = getRestaurant(existingId)
        val saved = formState.toRestaurant(existing = current)
        restaurants = if (current == null) {
            restaurants + saved
        } else {
            restaurants.map { restaurant ->
                if (restaurant.id == current.id) saved else restaurant
            }
        }
        persist()
        return saved
    }

    fun deleteRestaurant(id: String) {
        restaurants = restaurants.filterNot { it.id == id }
        persist()
    }

    private fun readRestaurants(): List<Restaurant> {
        if (!file.exists()) {
            return emptyList()
        }

        return runCatching {
            val raw = file.readText()
            if (raw.isBlank()) {
                emptyList()
            } else {
                val array = JSONArray(raw)
                buildList {
                    for (index in 0 until array.length()) {
                        add(array.getJSONObject(index).toRestaurant())
                    }
                }
            }
        }.getOrElse {
            emptyList()
        }
    }

    private fun persist() {
        file.writeText(JSONArray(restaurants.map { it.toJson() }).toString(2))
    }

    private fun Restaurant.toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("category", category.name)
        put("cuisine", cuisine)
        put("address", address)
        put("phone", phone)
        put("rating", rating)
        put("deliveryMinutes", deliveryMinutes)
        put("description", description)
    }

    private fun JSONObject.toRestaurant(): Restaurant = Restaurant(
        id = getString("id"),
        name = getString("name"),
        category = RestaurantCategory.entries.firstOrNull { it.name == getString("category") }
            ?: RestaurantCategory.Cafe,
        cuisine = getString("cuisine"),
        address = getString("address"),
        phone = optString("phone", ""),
        rating = optInt("rating", 5),
        deliveryMinutes = optInt("deliveryMinutes", 30),
        description = optString("description", ""),
    )

    private fun defaultRestaurants(): List<Restaurant> = listOf(
        Restaurant(
            id = "demo-cafe",
            name = "Mango Cafe",
            category = RestaurantCategory.Cafe,
            cuisine = "Coffee & brunch",
            address = "12 Main Street",
            phone = "+1 555 0101",
            rating = 5,
            deliveryMinutes = 20,
            description = "A bright city cafe with specialty coffee and fresh breakfast bowls.",
        ),
        Restaurant(
            id = "demo-fast-food",
            name = "City Burger",
            category = RestaurantCategory.FastFood,
            cuisine = "Burgers & fries",
            address = "84 Market Avenue",
            phone = "+1 555 0102",
            rating = 4,
            deliveryMinutes = 25,
            description = "Fast delivery, classic burgers, and combo meals for busy evenings.",
        ),
        Restaurant(
            id = "demo-fine-dining",
            name = "Azure Table",
            category = RestaurantCategory.FineDining,
            cuisine = "European tasting menu",
            address = "7 Riverside Drive",
            phone = "+1 555 0103",
            rating = 5,
            deliveryMinutes = 35,
            description = "A polished dinner spot with seasonal plates and premium service.",
        ),
    )
}
