package com.tpstudio.swipereveal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tpstudio.swipereveal.ui.theme.SwipeRevealTheme
import lib.tpstudio.swipereveal.SwipeContent
import lib.tpstudio.swipereveal.SwipeRevealItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwipeRevealTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        DemoSwipeable(
                            modifier = Modifier.fillMaxWidth()
                        )
                        DemoLazyList()
                    }
                }
            }
        }
    }
}

@Composable
fun DemoLazyList() {
    var openedItemKey by remember { mutableStateOf<Int?>(null) }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(20) { index ->
            SwipeRevealItem(
                modifier = Modifier.fillMaxWidth(),
                contentRadius = 12.dp,
                itemKey = index,
                openedItemKey = openedItemKey,
                onOpen = { key -> openedItemKey = key },
                onClose = { openedItemKey = null },
                startContent = {
                    SwipeContent(
                        background = Color.Green,
                        weight = 1f,
                        onClick = {

                        }
                    ) {
                        Icon(Icons.Default.Done, contentDescription = null, tint = Color.White)
                    }
                },
                startContentWidth = 100.dp,
                endContent = {
                    SwipeContent(
                        background = Color.Red,
                        weight = 1f,
                        onClick = {

                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                endContentWidth = 100.dp
            ) {
                ContentItem("Item $index")
            }
        }
    }
}

@Composable
fun DemoSwipeable(
    modifier: Modifier
) {
    SwipeRevealItem(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentRadius = 12.dp,
        startContent = {
            SwipeContent(
                background = Color.Green,
                weight = 1f,
                onClick = {
                    // TODO
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = null, tint = Color.White)
            }
        },
        startContentWidth = 50.dp,
        endContent = {
            SwipeContent(
                background = Color.Red,
                weight = 1f,
                onClick = {
                    // TODO
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
            }
            SwipeContent(
                background = Color.Yellow,
                weight = 1f,
                onClick = {
                    // TODO
                }
            ) {
                Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
            }
        },
        endContentWidth = 120.dp
    ) {
        ContentItem("Swipe both directions")
    }
}

@Composable
fun ContentItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
    }
}