package com.suitcase.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suitcase.R
import com.suitcase.data.Item
import com.suitcase.navigation.BottomBar
import com.suitcase.navigation.HomeBottomBar
import com.suitcase.viewmodels.AddItemToStorageViewModel
import com.suitcase.viewmodels.GetItemsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    getItemsViewModel: GetItemsViewModel,
    addItemToStorageViewModel: AddItemToStorageViewModel
) {
    val delegated = getItemsViewModel.items.collectAsState()

    val delegatedItems = mutableListOf<Item>()
    delegated.value.forEach { item ->
        if (item.delegated) {
            delegatedItems.add(item)

        }
    }

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text =
                        "Delegated items", fontFamily = FontFamily.SansSerif
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.round_addchart_24),
                        modifier = modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .clickable {
                                navController.navigate(HomeBottomBar.AddProduct.route)
                            },
                        contentDescription = null
                    )
                }
            )
        },
        bottomBar = { BottomBar(navHostController = navController) }
    ) { paddingValues ->
        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (delegatedItems.isEmpty()) {
                Card(modifier = modifier.fillMaxSize()) {
                    Text(
                        text =
                        "You have not delegated any Items Yet ... ",
                        modifier = modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )

                }
            } else {
                LazyColumn(modifier = modifier.height(700.dp)) {
                    items(delegatedItems) { item ->

                        if (item.delegated) {
                            DelegatedItem(
                                item = item,
                                context = context,
                                addItemToStorageViewModel = addItemToStorageViewModel
                            )
                        }

                    }
                }
            }


        }
    }
}

@Composable
fun DelegatedItem(
    modifier: Modifier = Modifier,
    item: Item,
    context: Context,
    addItemToStorageViewModel: AddItemToStorageViewModel?
) {
    var bought by remember {
        mutableStateOf(item.bought)
    }


    Card(
        modifier = modifier
            .height(90.dp)
            .fillMaxWidth()
            .padding(2.dp)

    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Color.Transparent,
                ),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = context.let {
                    ImageRequest.Builder(it)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .build()
                },
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(7)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = modifier.padding(top = 10.dp)) {
                Text(text = item.name)
                Spacer(modifier = Modifier.height(10.dp))
            }

            Column(modifier = modifier.padding(top = 10.dp)) {
                Text(text = "Purchased")
                Spacer(modifier = Modifier.height(10.dp))
                Icon(
                    painter = painterResource(
                        id =
                        if (bought) {
                            R.drawable.baseline_check_circle_outline_24
                        } else {
                            R.drawable.baseline_block_24
                        }
                    ),
                    tint = if (bought) {
                        Color.Green
                    } else {
                        Color.Red
                    },
                    modifier = modifier
                        .size(20.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            bought = !bought
                            addItemToStorageViewModel?.manipulateDelegated(
                                item = item,
                                delegated = false,
                                bought = true
                            )
                        },
                    contentDescription = null
                )
            }
            Column(modifier = modifier.padding(top = 10.dp)) {
                Icon(painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = modifier
                        .size(25.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            addItemToStorageViewModel?.removeItem(item)
                        })
            }


        }
    }
}
