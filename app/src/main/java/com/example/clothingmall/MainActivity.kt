package com.example.clothingmall

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.clothingmall.ui.theme.ClothingMallTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import androidx.compose.material3.NavigationBarItemState
//import androidx.compose.material3.currentBackStackEntryAsState
import com.example.clothingmall.screens.ReviewScreen
import com.example.clothingmall.screens.ExternalDataScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClothingMallTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }

    @Composable
    fun LoginScreen(navController: NavHostController) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val firebaseAuthHelper = remember { FirebaseAuthHelper() }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Login",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                TextButton(
                    onClick = { passwordVisibility = !passwordVisibility },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        if (passwordVisibility) "Hide Password" else "Show Password",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = {
                        isLoading = true
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                firebaseAuthHelper.loginUser(email, password)
                                    .onSuccess {
                                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                    .onFailure {
                                        Toast.makeText(context, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Login")
                    }
                }

                TextButton(
                    onClick = { navController.navigate("register") }
                ) {
                    Text(
                        "Don't have an account? Sign Up",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    @Composable
    fun MainApp() {
        val navController = rememberNavController()
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        
        Scaffold(
            bottomBar = {
                if (currentRoute != "login" && currentRoute != "register") {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, "Home") },
                            label = { Text("Home") },
                            selected = currentRoute == "home",
                            onClick = { navController.navigate("home") }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.ShoppingCart, "Products") },
                            label = { Text("Products") },
                            selected = currentRoute == "products",
                            onClick = { navController.navigate("products") }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.ShoppingBasket, "Cart") },
                            label = { Text("Cart") },
                            selected = currentRoute == "cart",
                            onClick = { navController.navigate("cart") }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Person, "Account") },
                            label = { Text("Account") },
                            selected = currentRoute == "manageAccount",
                            onClick = { navController.navigate("manageAccount") }
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("login") { LoginScreen(navController) }
                composable("register") { RegisterPage(navController) }
                composable("home") { HomePage() }
                composable("products") { ProductScreen(navController) }
                composable("manageAccount") { ManageAccountScreen(navController) }
                composable("cart") { Checkout(navController) }
                composable("addReview") {
                    ReviewScreen(
                        onReviewSubmitted = {
                            navController.popBackStack()
                        }
                    )
                }
                composable("weatherClothing") { WeatherClothingScreen() }
                composable("locationSuggestions") { LocationSuggestionsScreen() }
                composable("viewOrders") { ViewOrdersScreen() }
                composable("externalData") {
                    ExternalDataScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}


