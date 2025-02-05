package com.example.clothingmall

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ManageAccountScreen(navController: NavHostController) {
    val context = LocalContext.current
    val firebaseAuthHelper = remember { FirebaseAuthHelper() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Manage Account",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Add Review Button with functionality
        Button(
            onClick = { 
                navController.navigate("addReview")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Review")
        }

        // View Orders Button with functionality
        Button(
            onClick = { 
                // Show orders in a dialog or navigate to orders screen
                navController.navigate("viewOrders")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Orders")
        }

        // Add External Data Button
        Button(
            onClick = { navController.navigate("externalData") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View External Data")
        }

        // Sign Out Button
        Button(
            onClick = {
                firebaseAuthHelper.signOut()
                navController.navigate("login") {
                    popUpTo(0) // Clear the back stack
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Out")
        }
    }
}

@Composable
fun ViewOrdersScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your Orders",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Show orders list or empty state
        Text(
            text = "There are no previous orders",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ManageAccount(modifier: Modifier = Modifier) {

    val accountOptions = listOf(
        "My Wallet",
        "My Rewards",
        "My Offers",
        "Personal Information",
        "Address Details",
        "Sign Out",
        "Notifications"
    )

    val scrollState= rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Manage Account",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 30.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {

            //accountOptions.forEach { option ->
                //AccountOptionItem(option) {

                    //}
                    //}
                }
            }
        }
        Spacer(modifier = Modifier.height(200.dp))
    }
}

@Composable
fun AccountOptionItem(option: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(16.dp),
    ) {

        Text(
            text = option,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
