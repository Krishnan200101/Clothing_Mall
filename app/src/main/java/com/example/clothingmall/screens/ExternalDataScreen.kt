package com.example.clothingmall.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clothingmall.data.Post
import com.example.clothingmall.viewmodel.ExternalDataViewModel
import com.example.clothingmall.viewmodel.ExternalDataState

@Composable
fun ExternalDataScreen(
    onBack: () -> Unit
) {
    val viewModel: ExternalDataViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadExternalData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "External Data",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (state) {
            is ExternalDataState.Loading -> {
                CircularProgressIndicator()
            }
            is ExternalDataState.Success -> {
                val successState = state as ExternalDataState.Success
                if (selectedPost == null) {
                    LazyColumn {
                        items(successState.data.posts) { post ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { selectedPost = post }
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = post.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Detail view
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Post Details",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "ID: ${selectedPost?.id}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Title: ${selectedPost?.title}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            
                            // Show related comments
                            val comments = successState.data.comments.filter { 
                                it.postId == selectedPost?.id 
                            }
                            Text(
                                text = "Comments:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )
                            LazyColumn {
                                items(comments) { comment ->
                                    Text(
                                        text = comment.body,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }

                            Button(
                                onClick = { selectedPost = null },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Text("Back to List")
                            }
                        }
                    }
                }
            }
            is ExternalDataState.Error -> {
                Text(
                    text = (state as ExternalDataState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {}
        }
    }
}