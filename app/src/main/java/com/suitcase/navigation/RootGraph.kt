package com.suitcase.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.suitcase.utils.AUTHENTICATION
import com.suitcase.utils.CONTENT
import com.suitcase.utils.ROOT
import com.suitcase.viewmodels.AddItemToStorageViewModel
import com.suitcase.viewmodels.GetItemsViewModel
import com.suitcase.viewmodels.LoginViewModel
import com.suitcase.viewmodels.RegisterViewModel

sealed class RootGraph(val route: String) {
    data object Root : RootGraph(ROOT)
    data object Content : RootGraph(CONTENT)
    data object Authentication : RootGraph(AUTHENTICATION)
}

@Composable
fun RootNavGraph(user: FirebaseUser? = FirebaseAuth.getInstance().currentUser) {
    val navHostController = rememberNavController()
    val navController = rememberNavController()
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val addItemToStorageViewModel : AddItemToStorageViewModel = hiltViewModel()
    val getItemsViewModel : GetItemsViewModel = hiltViewModel()
    NavHost(
        navController = navHostController, startDestination =
        if (user?.uid != null) {
            RootGraph.Content.route
        } else {
            RootGraph.Authentication.route
        }, route = RootGraph.Root.route
    ) {
        loginGraph(navHostController, registerViewModel, loginViewModel)

        composable(route = RootGraph.Content.route) {
            Content(navHostController = navController, addItemToStorageViewModel =addItemToStorageViewModel, getItemsViewModel = getItemsViewModel)
        }
    }
}