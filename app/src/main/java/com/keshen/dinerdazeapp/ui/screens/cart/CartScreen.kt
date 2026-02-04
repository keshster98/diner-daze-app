package com.keshen.dinerdazeapp.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val tableNumber by cartViewModel.tableNumber.collectAsState()
    val paymentSuccess by cartViewModel.paymentSuccess.collectAsState()

    val totalPrice = cartViewModel.totalPrice

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cart") })
        }
    ) { padding ->

        if (paymentSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(72.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "You have paid RM %.2f to Diner Daze.\nYour order will be served shortly."
                            .format(totalPrice),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(onClick = { cartViewModel.resetPaymentState() }) {
                        Text("Done")
                    }
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nothing added to cart")
                }
                return@Column
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(cartItems.values.toList()) { item ->
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(Modifier.weight(1f)) {
                                Text(item.menu.name, maxLines = 2)
                                Text("RM %.2f".format(item.menu.price))
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                IconButton(
                                    onClick = { cartViewModel.decrease(item.menu) },
                                    enabled = item.quantity > 0
                                ) {
                                    Text("−")
                                }

                                Text(
                                    text = item.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                IconButton(
                                    onClick = { cartViewModel.increase(item.menu) },
                                    enabled = item.quantity < 5
                                ) {
                                    Text("+")
                                }
                            }


                            IconButton(onClick = {
                                cartViewModel.remove(item.menu.uid)
                            }) {
                                Icon(Icons.Default.Delete, null)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Total Items: ${cartViewModel.totalItems}")
            Text(
                text = "Total: RM %.2f".format(totalPrice),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = tableNumber,
                onValueChange = cartViewModel::onTableNumberChange,
                label = { Text("Table Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedButton(
                    onClick = cartViewModel::clearCart,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear Cart")
                }

                Button(
                    onClick = cartViewModel::payNow,
                    enabled = tableNumber.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pay Now")
                }
            }
        }
    }
}


