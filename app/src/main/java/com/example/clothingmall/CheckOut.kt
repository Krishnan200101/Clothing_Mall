package com.example.clothingmall

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.OutlinedTextFieldDefaults

@Composable
fun Checkout(navController: NavController) {
    var selectedPaymentMethod by remember { mutableStateOf("Card Payment") }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var showScanner by remember { mutableStateOf(false) }

    val productName = "Stylish T-Shirt"
    val productQuantity = 2
    val productImage = painterResource(id = R.drawable.mentshirt)

    val scrollState= rememberScrollState()

    if (showScanner) {
        BarcodeScannerScreen(
            onScanned = { barcode ->
                // Handle scanned barcode
                showScanner = false
            },
            onDismiss = { showScanner = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Check Out",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Image(
            painter = productImage,
            contentDescription = "Purchased Product",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(30.dp)
        )

        Text(
            text = productName,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground)
        Text(
            text = "Quantity: $productQuantity",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground)

        Text(
            text = "Select Payment Method:",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RadioButton(
                selected = selectedPaymentMethod == "Card Payment",
                onClick = { selectedPaymentMethod = "Card Payment" }
            )
            Text(
                text = "Card Payment",
                modifier = Modifier.clickable { selectedPaymentMethod = "Card Payment" },
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onBackground)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            RadioButton(
                selected = selectedPaymentMethod == "Cash Payment",
                onClick = { selectedPaymentMethod = "Cash Payment" }
            )
            Text(text = "Cash Payment", modifier = Modifier.clickable { selectedPaymentMethod = "Cash Payment" },
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onBackground)
        }

        Text(
            text = "Email Address:",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text("Email Address") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // Update Barcode Scanner Button
        Button(
            onClick = { showScanner = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Icon(
                imageVector = Icons.Default.QrCode,
                contentDescription = "Scan",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Scan Barcode")
        }

        Button(
            onClick = { /* Handle payment */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Proceed to Payment")
        }
    }
}

