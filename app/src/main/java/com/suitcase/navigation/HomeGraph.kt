package com.suitcase.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.suitcase.R
import com.suitcase.screens.AddProduct
import com.suitcase.screens.ContactForm
import com.suitcase.screens.HomePage
import com.suitcase.screens.ItemsScreen
import com.suitcase.utils.ADDPRODUCT
import com.suitcase.utils.HOME
import com.suitcase.utils.ORDERS
import com.suitcase.viewmodels.AddItemToStorageViewModel
import com.suitcase.viewmodels.GetItemsViewModel

@Composable
fun Content(
    navHostController: NavHostController,
    getItemsViewModel: GetItemsViewModel,
    addItemToStorageViewModel: AddItemToStorageViewModel
) {
    NavHost(navController = navHostController, startDestination = HomeBottomBar.Home.route) {
        composable(route = HomeBottomBar.Home.route) {
            HomePage(navController = navHostController, addItemToStorageViewModel = addItemToStorageViewModel, getItemsViewModel = getItemsViewModel)
        }
        composable(route = HomeBottomBar.Account.route) {
            ItemsScreen(navController = navHostController, getItemsViewModel = getItemsViewModel , addItemToStorageViewModel = addItemToStorageViewModel)
        }
        composable(route = HomeBottomBar.AddProduct.route) {
            AddProduct(addItemToStorageViewModel = addItemToStorageViewModel)
        }
        composable(route = HomeBottomBar.Message.route) {
           ContactForm(addItemToStorageViewModel = addItemToStorageViewModel)
        }
    }

}

sealed class HomeBottomBar(
    val route: String,
    val icon: Int
) {
    data object Home : HomeBottomBar(
        route = HOME,
        icon = R.drawable.outline_home_24
    )

    data object Account : HomeBottomBar(
        route = ORDERS,
        icon = R.drawable.outline_person_24
    )

    data object AddProduct : HomeBottomBar(
        route = ADDPRODUCT,
        icon = R.drawable.outline_home_24
    )
    data object Message : HomeBottomBar(
        route = "message",
        icon = R.drawable.outline_home_24
    )
}

@Composable
fun BottomBar(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val screens = listOf(HomeBottomBar.Home, HomeBottomBar.Account)

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier
            .height(54.dp)
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        screens.forEach { screen ->
            NavigationBarItem(selected = currentDestination == screen.route,
                onClick = {
                    navHostController.navigate(screen.route) {
                        navHostController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        tint = MaterialTheme.colorScheme.surface,
                        contentDescription = HOME, modifier = modifier.size(24.dp)
                    )


                })
        }

    }

}