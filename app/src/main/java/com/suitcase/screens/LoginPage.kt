package com.suitcase.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.suitcase.R
import com.suitcase.navigation.AuthGraph
import com.suitcase.navigation.RootGraph
import com.suitcase.viewmodels.LoginViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    val status = loginViewModel.loginStatus.value
    var userEmail by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(state = rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Spacer(modifier = modifier.height(10.dp))


        Text(
            text = stringResource(id = R.string.login),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surface,
            fontSize = 40.sp,

            modifier = modifier.padding(20.dp)
        )
        Spacer(modifier = modifier.height(100.dp))

        OutlinedText(
            value = userEmail,
            onValueChanged = { userEmail = it },
            visualTransformation = VisualTransformation.None,
            icon = R.drawable.baseline_person_outline_24,
            label = R.string.email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )



        OutlinedText(
            value = password,
            onValueChanged = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            icon = R.drawable.outline_lock_24,
            label = R.string.password,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )


        Spacer(modifier = modifier.height(10.dp))
        OutlinedButton(
            onClick = {
                if (!emailCheck(userEmail)) {
                    Toast.makeText(context, "Please Enter valid email", Toast.LENGTH_LONG).show()
                } else {
                    loginViewModel.loginUser(userEmail, password)
                }
            },
            enabled = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(

            )
        ) {
            Text(
                text = stringResource(id = R.string.login),
                color = MaterialTheme.colorScheme.surface
            )
        }

        if (status.isLoading) {
            CircularProgressIndicator()
        }

        LaunchedEffect(key1 = status) {
            if (status.isSuccess) {
                Toast.makeText(context, "Logged in Successfully", Toast.LENGTH_LONG).show()
                navController.navigate(RootGraph.Content.route) {
                    popUpTo(RootGraph.Content.route)
                }

            }
        }

        Row {
            Text(
                text = stringResource(id = R.string.no_account),
                color = MaterialTheme.colorScheme.surfaceTint,
                modifier = modifier.padding(top = 15.dp)
            )

            TextButton(onClick = {
                navController.navigate(AuthGraph.SignupPage.route)

            }) {
                Text(
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colorScheme.surfaceTint
                )

            }

        }

    }


}

@Composable
fun OutlinedText(
    value: String,
    onValueChanged: (String) -> Unit,
    visualTransformation: VisualTransformation,
    icon: Int,
    label: Int,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions

) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        visualTransformation = visualTransformation,
        leadingIcon = { Icon(painter = painterResource(id = icon), contentDescription = null) },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 18.dp, top = 9.dp, bottom = 9.dp),
        shape = CutCornerShape(
            topStart = 10.dp,
            topEnd = 0.dp,
            bottomStart = 0.dp,
            bottomEnd = 10.dp
        ),
        label = { Text(text = stringResource(id = label)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.surfaceTint,
            focusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
            focusedLeadingIconColor = MaterialTheme.colorScheme.surface,
            focusedPlaceholderColor = MaterialTheme.colorScheme.surface,

            ),
        keyboardOptions = keyboardOptions


    )
}


//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun LoginPrev(){
//    LoginScreen( modifier = Modifier)
//
//}
