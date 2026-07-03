package com.example.mini_uber_eats_app.ui.navigation

object RestaurantRoutes {
    const val LIST = "restaurants"
    const val DETAIL = "restaurant/{restaurantId}"
    const val FORM = "restaurant_form?restaurantId={restaurantId}"
    const val ARG_RESTAURANT_ID = "restaurantId"

    fun detail(restaurantId: String): String = "restaurant/$restaurantId"

    fun form(restaurantId: String? = null): String {
        return if (restaurantId == null) {
            "restaurant_form"
        } else {
            "restaurant_form?restaurantId=$restaurantId"
        }
    }
}
