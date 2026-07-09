package com.example.practice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var fabAdd: MaterialButton
    private lateinit var tvEmpty: TextView
    
    private lateinit var adapter: RestaurantAdapter
    private val viewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        RestaurantRepository.init(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        ViewCompat.setOnApplyWindowInsetsListener(recyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                (16 * resources.displayMetrics.density).toInt() + systemBars.left,
                v.paddingTop,
                (16 * resources.displayMetrics.density).toInt() + systemBars.right,
                0
            )
            insets
        }

        val bottomActionContainer = findViewById<android.widget.FrameLayout>(R.id.bottomActionContainer)
        ViewCompat.setOnApplyWindowInsetsListener(bottomActionContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                systemBars.bottom
            )
            insets
        }

        fabAdd = findViewById(R.id.fabAdd)
        tvEmpty = findViewById(R.id.tvEmpty)

        setupRecyclerView()
        setupButtons()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadRestaurants()
    }

    private fun setupRecyclerView() {
        adapter = RestaurantAdapter(
            onItemClick = { restaurant ->
                openEditActivity(restaurant.id)
            },
            onDeleteClick = { restaurant -> viewModel.removeRestaurant(restaurant) }
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        
        (recyclerView.itemAnimator as? androidx.recyclerview.widget.SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    private fun setupButtons() {
        fabAdd.setOnClickListener {
            openEditActivity()
        }
    }

    private fun openEditActivity(restaurantId: Int = -1) {
        val intent = Intent(this, EditRestaurantActivity::class.java).apply {
            putExtra("RESTAURANT_ID", restaurantId)
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            adapter.submitList(state.restaurants)
            tvEmpty.visibility = if (state.isEmpty) View.VISIBLE else View.GONE
        }
    }
}
