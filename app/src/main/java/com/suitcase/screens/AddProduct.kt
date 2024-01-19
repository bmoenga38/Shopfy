package com.suitcase.screens


import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.suitcase.viewmodels.AddItemToStorageViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("NewApi")
@Composable
fun AddProduct(
    modifier: Modifier = Modifier,
    addItemToStorageViewModel: AddItemToStorageViewModel
) {

    val uploadImageState = addItemToStorageViewModel.uploadStatus.collectAsState()
    val uploadProductState = addItemToStorageViewModel.uploadProduct.collectAsState()
    val context = LocalContext.current

    var selectedImages by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    var image: Uri = Uri.EMPTY

    var name by remember {
        mutableStateOf("")
    }
    var isClicked by remember {
        mutableStateOf(false)
    }


    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            selectedImages = it

        }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text(text = "Add Item")},
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

        }
    ){
            paddingValues ->   Card(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
    ) {
        Spacer(modifier = modifier.height(8.dp))


        OutlinedTextUpload(value = name, onValueChanged = { name = it }, label = "Product name")

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(3.dp)

        ) {

        }


        OutlinedButton(
            onClick = {
                pickMedia.launch("image/*")
            },
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Select Image", color = MaterialTheme.colorScheme.surfaceTint)

        }
        OutlinedButton(
            onClick = {
                      addItemToStorageViewModel.uploadItem(name,image)
                Toast.makeText(context,uploadProductState.value,Toast.LENGTH_SHORT).show()
                Toast.makeText(context,uploadImageState.value,Toast.LENGTH_SHORT).show()
            },
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Upload Product", color = MaterialTheme.colorScheme.surfaceTint)

        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.height(400.dp)
        ) {
            items(selectedImages) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = modifier
                        .padding(1.dp)
                        .size(100.dp)
                        .clickable(
                            enabled = true,
                            onClickLabel = "Selected",
                            onClick = {
                                image = uri
                                isClicked = !isClicked
                            }
                        ),
                    alpha = if (isClicked) {
                        0.5f
                    } else {
                        0.8f
                    },
                    contentScale = ContentScale.Crop


                )
            }
        }


    }

    }



}

@Composable
fun OutlinedTextUpload(
    modifier: Modifier = Modifier,
    value: String, onValueChanged: (String) -> Unit,
    label: String
) {

    OutlinedTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        onValueChange = onValueChanged,
        placeholder = {
            Text(text = label, color = MaterialTheme.colorScheme.onSecondary)
        },
        label = {
            Text(text = label, color = MaterialTheme.colorScheme.onSecondary)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.surfaceTint,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            cursorColor = MaterialTheme.colorScheme.surfaceTint,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondary,
            focusedLabelColor = MaterialTheme.colorScheme.surfaceTint,
            unfocusedLabelColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(50)

    )

}


//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun Show(){
//    OutlinedTextUpload()
//}