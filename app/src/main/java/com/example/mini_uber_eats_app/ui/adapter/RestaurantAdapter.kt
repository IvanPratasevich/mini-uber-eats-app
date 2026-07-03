package com.example.mini_uber_eats_app.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mini_uber_eats_app.data.Restaurant
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip

internal class RestaurantAdapter(
    private val onRestaurantClick: (String) -> Unit,
) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RestaurantViewHolder {
        val context = parent.context
        val item = createItemViews(context)
        return RestaurantViewHolder(item, onRestaurantClick)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun createItemViews(context: Context): RestaurantItemViews {
        val card = MaterialCardView(context).apply {
            layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(dp(context, 2), dp(context, 2), dp(context, 2), dp(context, 2))
            }
            radius = dp(context, 16).toFloat()
            cardElevation = dp(context, 2).toFloat()
            useCompatPadding = true
            isClickable = true
            isFocusable = true
        }

        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(context, 16), dp(context, 16), dp(context, 16), dp(context, 16))
        }

        val topRow = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }

        val leftColumn = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
        }

        val nameView = TextView(context).apply {
            setTypeface(typeface, Typeface.BOLD)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }

        val cuisineView = TextView(context).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            alpha = 0.75f
        }

        leftColumn.addView(nameView)
        leftColumn.addView(space(context, 4))
        leftColumn.addView(cuisineView)

        val rightColumn = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            setPadding(dp(context, 12), 0, 0, 0)
            gravity = android.view.Gravity.END
        }

        val ratingView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = android.view.Gravity.END
            }
            setTypeface(typeface, Typeface.BOLD)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(0xFF0E7C66.toInt()) // Зеленый цвет рейтинга
        }

        val deliveryView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = android.view.Gravity.END
            }
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            alpha = 0.72f
        }

        rightColumn.addView(ratingView)
        rightColumn.addView(space(context, 4))
        rightColumn.addView(deliveryView)

        topRow.addView(leftColumn)
        topRow.addView(rightColumn)

        val chipsRow = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            setPadding(0, dp(context, 12), 0, 0)
        }

        val categoryChip = Chip(context).apply {
            isClickable = false
            isCheckable = false
        }

        val addressChip = Chip(context).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                marginStart = dp(context, 8)
            }
            isClickable = false
            isCheckable = false
        }

        chipsRow.addView(categoryChip)
        chipsRow.addView(addressChip)

        root.addView(topRow)
        root.addView(chipsRow)
        card.addView(root)

        return RestaurantItemViews(
            card = card,
            nameView = nameView,
            cuisineView = cuisineView,
            ratingView = ratingView,
            deliveryView = deliveryView,
            categoryChip = categoryChip,
            addressChip = addressChip,
        )
    }

    private fun dp(context: Context, value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics,
        ).toInt()
    }

    private fun space(context: Context, value: Int): TextView {
        return TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, dp(context, value))
            text = ""
        }
    }

    internal data class RestaurantItemViews(
        val card: MaterialCardView,
        val nameView: TextView,
        val cuisineView: TextView,
        val ratingView: TextView,
        val deliveryView: TextView,
        val categoryChip: Chip,
        val addressChip: Chip,
    )

    internal class RestaurantViewHolder(
        private val views: RestaurantItemViews,
        private val onRestaurantClick: (String) -> Unit,
    ) : RecyclerView.ViewHolder(views.card) {
        fun bind(restaurant: Restaurant) {
            views.nameView.text = restaurant.name
            views.cuisineView.text = restaurant.cuisine
            views.ratingView.text = "${restaurant.rating}.0"
            views.deliveryView.text = "${restaurant.deliveryMinutes} min"
            views.categoryChip.text = restaurant.category.label
            views.addressChip.text = restaurant.address
            views.card.setOnClickListener {
                onRestaurantClick(restaurant.id)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }
}
