package com.suitcase.screens


import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.suitcase.R
import com.suitcase.viewmodels.AddItemToStorageViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactForm(
    modifier: Modifier = Modifier,
    addItemToStorageViewModel: AddItemToStorageViewModel
) {

    val item = addItemToStorageViewModel.delegatedItem
    val context = LocalContext.current
    var phone by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Delegate some") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (item != null) {
                AsyncImage(
                    model = context.let {
                        ImageRequest.Builder(it)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .build()
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(7))
                )
            }

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(text = "phone ") },
                modifier = modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.surfaceTint,
                    focusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
                ),
            )
            Spacer(modifier = modifier.height(20.dp))


            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(text = "Message ") },
                placeholder = {
                    item?.name
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.surfaceTint,
                    focusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
                ),
                modifier = modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 7

            )
            Spacer(modifier = modifier.height(20.dp))
            OutlinedButton(
                onClick = {
                    sendMessage(phone ,message,context)
                },
                enabled = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(

                )
            ) {
                Text(text = "Send",color = MaterialTheme.colorScheme.surface)
            }


        }

    }


}
fun sendMessage(phone : String, text: String, context: Context) {
    val smsManager = SmsManager.getDefault()
    try {
        smsManager.sendTextMessage(
            phone, null,
            text, null, null
        )
        Toast.makeText(context, "sent Successfully", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error sending ${e.message.toString()}", Toast.LENGTH_LONG).show()

    }


}

