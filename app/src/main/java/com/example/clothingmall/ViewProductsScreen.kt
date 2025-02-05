package com.example.clothingmall

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clothingmall.components.ProductList
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


data class Product(
        val name: String,
        val imageResId: Int,
        val price: String,
        val quantity: String,
        val description: String
    )

    val products = listOf(
        Product("Stylish T-Shirt", R.drawable.mentshirt, "LKR 2000", "10", "A stylish t-shirt for all occasions."),
        Product("Cool Trouser", R.drawable.trouser, "LKR 3000", "15", "Comfortable and trendy trousers."),
        Product("Summer Shirt", R.drawable.menshirt, "LKR 2500", "8", "A fashionable summer shirt."),
        Product("Classic Shirt", R.drawable.menshirt1, "LKR 2500", "12", "Versatile classic shirt.")
    )

    @Composable
    fun ProductItem(
        product: Product,
        onAddToCart: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge
                )
                
                Text(
                    text = product.price,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Button(onClick = onAddToCart) {
                    Text("Add to Cart")
                }
            }
        }
    }

    @Composable
    fun ProductScreen(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Products",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Weather and Location buttons with navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("weatherClothing") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Weather-based")
                }
                Button(
                    onClick = { navController.navigate("locationSuggestions") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text("Location-based")
                }
            }

            LazyColumn {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onAddToCart = { /* Add to cart logic */ }
                    )
                }
            }
        }
    }
