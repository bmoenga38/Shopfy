package com.suitcase.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.suitcase.screens.LoginScreen
import com.suitcase.screens.RegisterScreen
import com.suitcase.utils.FORGOT
import com.suitcase.utils.LOGIN
import com.suitcase.utils.SIGNUP
import com.suitcase.viewmodels.LoginViewModel
import com.suitcase.viewmodels.RegisterViewModel

sealed class AuthGraph (val route : String){
    data object SignupPage : AuthGraph(SIGNUP)
    data object LoginPage : AuthGraph(LOGIN)
    data object ForgotPage : AuthGraph(FORGOT)
}

fun NavGraphBuilder.loginGraph(navHostController: NavHostController,registerViewModel: RegisterViewModel,loginViewModel: LoginViewModel){
    navigation(startDestination = AuthGraph.SignupPage.route,route = RootGraph.Authentication.route){
        composable(route = AuthGraph.SignupPage.route){
            RegisterScreen(navHostController = navHostController, registerViewModel = registerViewModel)

        }
        composable(route = AuthGraph.LoginPage.route){
            LoginScreen(navController = navHostController, loginViewModel = loginViewModel )

        }
        composable(route = AuthGraph.ForgotPage.route){

        }
    }
}