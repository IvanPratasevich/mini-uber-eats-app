package com.example.mini_uber_eats_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mini_uber_eats_app.data.Restaurant
import com.example.mini_uber_eats_app.data.RestaurantCategory
import com.example.mini_uber_eats_app.data.RestaurantFormState
import com.example.mini_uber_eats_app.ui.adapter.RestaurantAdapter
import com.example.mini_uber_eats_app.ui.navigation.RestaurantRoutes
import com.example.mini_uber_eats_app.ui.viewmodel.RestaurantViewModel
import androidx.compose.runtime.saveable.rememberSaveable

private val LightColors = lightColorScheme(
    primary = Color(0xFF0E7C66),
    onPrimary = Color.White,
    secondary = Color(0xFFB45309),
    onSecondary = Color.White,
    tertiary = Color(0xFF1D4ED8),
    onTertiary = Color.White,
    background = Color(0xFFFBFAF7),
    surface = Color.White,
    onSurface = Color(0xFF1F2937),
    onBackground = Color(0xFF1F2937),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF38B2A3),
    onPrimary = Color(0xFF062822),
    secondary = Color(0xFFF59E0B),
    onSecondary = Color(0xFF261400),
    tertiary = Color(0xFF60A5FA),
    onTertiary = Color(0xFF0B1B33),
)

@Composable
fun RestaurantApp(viewModel: RestaurantViewModel) {
    val navController = rememberNavController()

    MaterialTheme(colorScheme = if (androidx.compose.foundation.isSystemInDarkTheme()) DarkColors else LightColors) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = RestaurantRoutes.LIST,
            ) {
                composable(RestaurantRoutes.LIST) {
                    RestaurantListScreen(
                        restaurants = viewModel.restaurants,
                        onAdd = { navController.navigate(RestaurantRoutes.form()) },
                        onSelect = { restaurantId -> navController.navigate(RestaurantRoutes.detail(restaurantId)) },
                    )
                }

                composable(
                    route = RestaurantRoutes.DETAIL,
                    arguments = listOf(
                        navArgument(RestaurantRoutes.ARG_RESTAURANT_ID) {
                            type = NavType.StringType
                        },
                    ),
                ) { backStackEntry ->
                    val restaurantId = backStackEntry.arguments?.getString(RestaurantRoutes.ARG_RESTAURANT_ID)
                    val restaurant = viewModel.getRestaurant(restaurantId)

                    if (restaurant == null) {
                        LaunchedEffect(Unit) {
                            navController.popBackStack(RestaurantRoutes.LIST, inclusive = false)
                        }
                    } else {
                        RestaurantDetailScreen(
                            restaurant = restaurant,
                            onBack = { navController.popBackStack() },
                            onEdit = { navController.navigate(RestaurantRoutes.form(restaurant.id)) },
                            onDelete = {
                                viewModel.deleteRestaurant(restaurant.id)
                                navController.popBackStack(RestaurantRoutes.LIST, inclusive = false)
                            },
                        )
                    }
                }

                composable(
                    route = RestaurantRoutes.FORM,
                    arguments = listOf(
                        navArgument(RestaurantRoutes.ARG_RESTAURANT_ID) {
                            type = NavType.StringType
                            defaultValue = ""
                        },
                    ),
                ) { backStackEntry ->
                    val restaurantId = backStackEntry.arguments
                        ?.getString(RestaurantRoutes.ARG_RESTAURANT_ID)
                        .orEmpty()
                        .ifBlank { null }
                    val restaurant = viewModel.getRestaurant(restaurantId)

                    RestaurantFormScreen(
                        restaurant = restaurant,
                        onBack = { navController.popBackStack() },
                        onSave = { formState ->
                            viewModel.saveRestaurant(formState, existingId = restaurant?.id)
                            navController.popBackStack(RestaurantRoutes.LIST, inclusive = false)
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RestaurantListScreen(
    restaurants: List<Restaurant>,
    onAdd: () -> Unit,
    onSelect: (String) -> Unit,
) {
    val adapter = remember(onSelect) { RestaurantAdapter(onSelect) }

    LaunchedEffect(restaurants, adapter) {
        adapter.submitList(restaurants)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Restaurants") },
                actions = {
                    IconButton(onClick = onAdd) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                modifier = Modifier.navigationBarsPadding(),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add restaurant")
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFF7F2E8), Color(0xFFFBFAF7)),
                    ),
                )
                .padding(paddingValues),
        ) {
            if (restaurants.isEmpty()) {
                EmptyState(
                    title = "No restaurants yet",
                    description = "Add the first restaurant to keep your list ready for the exam demo.",
                    actionLabel = "Create restaurant",
                    onAction = onAdd,
                )
            } else {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        RecyclerView(context).apply {
                            layoutManager = LinearLayoutManager(context)
                            this.adapter = adapter
                            setHasFixedSize(true)
                            clipToPadding = false
                            setPadding(0, 0, 0, context.resources.displayMetrics.density.times(96).toInt())
                        }
                    },
                    update = { recyclerView ->
                        if (recyclerView.adapter !== adapter) {
                            recyclerView.adapter = adapter
                        }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RestaurantDetailScreen(
    restaurant: Restaurant,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurant.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFEEF6F4), Color(0xFFFDFCF9)),
                    ),
                )
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                if (this.maxWidth > 700.dp) {
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        SummaryPanel(
                            restaurant = restaurant,
                            modifier = Modifier.weight(1f),
                        )
                        DetailPanel(
                            restaurant = restaurant,
                            modifier = Modifier.weight(1f),
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SummaryPanel(restaurant = restaurant, modifier = Modifier.fillMaxWidth())
                        DetailPanel(restaurant = restaurant, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryPanel(
    restaurant: Restaurant,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = restaurant.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = restaurant.category.label,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            MetricRow(label = "Cuisine", value = restaurant.cuisine)
            MetricRow(label = "Address", value = restaurant.address)
            MetricRow(label = "Phone", value = restaurant.phone)
        }
    }
}

@Composable
private fun DetailPanel(
    restaurant: Restaurant,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            MetricRow(label = "Rating", value = "${restaurant.rating} / 5")
            MetricRow(label = "Delivery", value = "${restaurant.deliveryMinutes} minutes")
            MetricRow(label = "Description", value = restaurant.description.ifBlank { "No description provided." })
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
            modifier = Modifier.widthIn(min = 96.dp, max = 160.dp),
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RestaurantFormScreen(
    restaurant: Restaurant?,
    onBack: () -> Unit,
    onSave: (RestaurantFormState) -> Unit,
) {
    val isEditing = restaurant != null
    var name by rememberSaveable(restaurant?.id) { mutableStateOf(restaurant?.name ?: "") }
    var selectedCategory by rememberSaveable(restaurant?.id) {
        mutableStateOf(restaurant?.category ?: RestaurantCategory.Cafe)
    }
    var cuisine by rememberSaveable(restaurant?.id) { mutableStateOf(restaurant?.cuisine ?: "") }
    var address by rememberSaveable(restaurant?.id) { mutableStateOf(restaurant?.address ?: "") }
    var phone by rememberSaveable(restaurant?.id) { mutableStateOf(restaurant?.phone ?: "") }
    var rating by rememberSaveable(restaurant?.id) { mutableStateOf(restaurant?.rating?.toString() ?: "5") }
    var deliveryMinutes by rememberSaveable(restaurant?.id) {
        mutableStateOf(restaurant?.deliveryMinutes?.toString() ?: "30")
    }
    var description by rememberSaveable(restaurant?.id) { mutableStateOf(restaurant?.description ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit restaurant" else "Add restaurant") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        onSave(
                            RestaurantFormState(
                                name = name,
                                category = selectedCategory,
                                cuisine = cuisine,
                                address = address,
                                phone = phone,
                                rating = rating,
                                deliveryMinutes = deliveryMinutes,
                                description = description,
                            ),
                        )
                    }) {
                        Text("Save")
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFAF7F0), Color(0xFFFDFDFC)),
                    ),
                )
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val twoColumns = this.maxWidth > 700.dp
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (twoColumns) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                FormField(label = "Name", value = name, onValueChange = { name = it })
                                CategoryField(
                                    category = selectedCategory,
                                    onCategoryChange = { selectedCategory = it },
                                    editable = !isEditing,
                                )
                                FormField(label = "Cuisine", value = cuisine, onValueChange = { cuisine = it })
                                FormField(label = "Phone", value = phone, onValueChange = { phone = it })
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                FormField(label = "Address", value = address, onValueChange = { address = it })
                                FormField(label = "Rating (1-5)", value = rating, onValueChange = { rating = it })
                                FormField(
                                    label = "Delivery minutes",
                                    value = deliveryMinutes,
                                    onValueChange = { deliveryMinutes = it },
                                )
                                FormField(label = "Description", value = description, onValueChange = { description = it }, singleLine = false)
                            }
                        }
                    } else {
                        FormField(label = "Name", value = name, onValueChange = { name = it })
                        CategoryField(
                            category = selectedCategory,
                            onCategoryChange = { selectedCategory = it },
                            editable = !isEditing,
                        )
                        FormField(label = "Cuisine", value = cuisine, onValueChange = { cuisine = it })
                        FormField(label = "Address", value = address, onValueChange = { address = it })
                        FormField(label = "Phone", value = phone, onValueChange = { phone = it })
                        FormField(label = "Rating (1-5)", value = rating, onValueChange = { rating = it })
                        FormField(
                            label = "Delivery minutes",
                            value = deliveryMinutes,
                            onValueChange = { deliveryMinutes = it },
                        )
                        FormField(label = "Description", value = description, onValueChange = { description = it }, singleLine = false)
                    }

                    Button(
                        onClick = {
                            onSave(
                                RestaurantFormState(
                                    name = name,
                                    category = selectedCategory,
                                    cuisine = cuisine,
                                    address = address,
                                    phone = phone,
                                    rating = rating,
                                    deliveryMinutes = deliveryMinutes,
                                    description = description,
                                ),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Save restaurant")
                    }
                }
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryField(
    category: RestaurantCategory,
    onCategoryChange: (RestaurantCategory) -> Unit,
    editable: Boolean,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Type",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
        )
        if (!editable) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Text(
                    text = category.label,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Medium,
                )
            }
        } else {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                RestaurantCategory.entries.forEachIndexed { index, item ->
                    SegmentedButton(
                        selected = category == item,
                        onClick = { onCategoryChange(item) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = RestaurantCategory.entries.size),
                    ) {
                        Text(item.label)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(
    title: String,
    description: String,
    actionLabel: String,
    onAction: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            description,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
            modifier = Modifier.widthIn(max = 380.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAction) { Text(actionLabel) }
    }
}
