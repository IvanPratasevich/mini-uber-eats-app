package com.example.mini_uber_eats_app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mini_uber_eats_app.ui.screens.RestaurantApp
import com.example.mini_uber_eats_app.ui.viewmodel.RestaurantViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val application = LocalContext.current.applicationContext as Application
            val restaurantViewModel: RestaurantViewModel = viewModel(
                factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application),
            )
            RestaurantApp(viewModel = restaurantViewModel)
        }
    }
}