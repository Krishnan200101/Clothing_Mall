package com.example.clothingmall.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clothingmall.Product
import com.example.clothingmall.products

@Composable
fun ProductList(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onReviewClick = { 
                    navController.navigate("addReview")
                }
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onReviewClick: () -> Unit
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
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onReviewClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Review")
            }
        }
    }
} 