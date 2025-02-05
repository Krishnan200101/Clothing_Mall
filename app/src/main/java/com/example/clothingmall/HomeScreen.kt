package com.example.clothingmall

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    var showMenu by remember { mutableStateOf(false) }
    val images = listOf(
        R.drawable.mallimage1,
        R.drawable.mallimage2,
        R.drawable.mallimage3,
        R.drawable.mallimage4
    )
    var currentImageIndex by remember { mutableIntStateOf(0) }



    LaunchedEffect(currentImageIndex) {
        delay(5000L) // 5 second interval
        currentImageIndex = (currentImageIndex + 1) % images.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(painterResource(id = R.drawable.hamburger), contentDescription = "Menu")
                }
            }
        )

        if (showMenu) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp)
            ) {
                HamburgerMenuItem("Review and Feedback")
                HamburgerMenuItem("Customer Service")
                HamburgerMenuItem("My Orders")
                HamburgerMenuItem("My Loyalty Points")
                HamburgerMenuItem("Search")
            }
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = images[currentImageIndex]),
                    contentDescription = "Sliding Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        val description = stringResource(id = R.string.mall_description)

        Text(
            text = description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}



@Composable
fun HamburgerMenuItem(label: String) {
    Button(
        onClick = { /* Handle click */ },
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 4.dp),
    ) {
        Text(label, fontSize = 16.sp)
    }
}
