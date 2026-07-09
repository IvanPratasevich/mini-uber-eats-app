package com.example.practice

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditRestaurantActivity : AppCompatActivity() {

    private val viewModel: EditRestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        RestaurantRepository.init(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_restaurant)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val tvToolbarTitle = findViewById<android.widget.TextView>(R.id.tvToolbarTitle)
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        toolbar.setNavigationOnClickListener { finish() }

        val scrollView = findViewById<android.widget.ScrollView>(R.id.editScrollView)
        scrollView.clipToPadding = false
        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                0,
                systemBars.right,
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

        val etName = findViewById<EditText>(R.id.etRestaurantName)
        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val etRating = findViewById<EditText>(R.id.etRating)
        val etDeliveryTime = findViewById<EditText>(R.id.etDeliveryTime)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnSave = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSave)
        val ivRestaurantImage = findViewById<android.widget.ImageView>(R.id.ivRestaurantImage)

        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            RestaurantCategory.entries
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        categorySpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val category = RestaurantCategory.entries[position]
                val imageRes = when (category) {
                    RestaurantCategory.FAST_FOOD -> R.drawable.fastfood
                    RestaurantCategory.CAFE -> R.drawable.cafe
                    RestaurantCategory.RESTAURANT -> R.drawable.restaurant
                }
                ivRestaurantImage.setImageResource(imageRes)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        val restaurantId = intent.getIntExtra("RESTAURANT_ID", -1)
        if (restaurantId == -1) {
            tvToolbarTitle.text = getString(R.string.add_title)
            btnSave.text = getString(R.string.add_button)
            btnSave.setIconResource(R.drawable.ic_add)
            categorySpinner.isEnabled = true
        } else {
            tvToolbarTitle.text = getString(R.string.edit_title)
            btnSave.text = getString(R.string.save)
            btnSave.setIconResource(R.drawable.ic_save)
            categorySpinner.isEnabled = false 
            categorySpinner.alpha = 0.6f
        }
        viewModel.loadRestaurant(restaurantId)

        viewModel.restaurant.observe(this) { restaurant ->
            restaurant?.let {
                etName.setText(it.name)
                val categoryIndex = RestaurantCategory.entries.indexOf(it.category)
                if (categoryIndex != -1) {
                    categorySpinner.setSelection(categoryIndex)
                }
                etRating.setText(it.rating.toString())
                etDeliveryTime.setText(it.deliveryTime)
                etAddress.setText(it.address)
                etDescription.setText(it.description)
                ivRestaurantImage.setImageResource(it.imageResId)
            }
        }

        btnSave.setOnClickListener {
            val newName = etName.text.toString()
            val newCategory = categorySpinner.selectedItem as RestaurantCategory
            val newRating = etRating.text.toString().toDoubleOrNull() ?: 0.0
            val newDeliveryTime = etDeliveryTime.text.toString()
            val newAddress = etAddress.text.toString()
            val newDescription = etDescription.text.toString()
            
            viewModel.saveRestaurant(
                newName,
                newCategory,
                newRating,
                newDeliveryTime,
                newAddress,
                newDescription
            )
            finish()
        }
    }
}
