package com.muhammadfaishalrizqipratama0094.cookit_.ui.screen

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.muhammadfaishalrizqipratama0094.cookit_.R
import com.muhammadfaishalrizqipratama0094.cookit_.network.UserDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddResepScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        bitmap = getCroppedImage(context.contentResolver, result)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.tambah_resep)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali)
                        )
                    }
                }
            )
        }
    ) { padding ->
        AddResepForm(
            bitmap = bitmap,
            onImageClick = {
                val options = CropImageContractOptions(
                    null, CropImageOptions(
                        imageSourceIncludeGallery = true,
                        imageSourceIncludeCamera = true,
                        fixAspectRatio = true,
                        aspectRatioX = 1,
                        aspectRatioY = 1,
                        guidelines = CropImageView.Guidelines.ON
                    )
                )
                launcher.launch(options)
            },
            onSaveClick = { judul, deskripsi, bahan, instruksi ->
                coroutineScope.launch {
                    val user = UserDataStore(context).userFlow.first()
                    if (bitmap != null && user.email.isNotEmpty()) {
                        viewModel.saveData(user.email, judul, deskripsi, bahan, instruksi, bitmap!!) {
                            navController.popBackStack()
                        }
                    }
                }
            },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun AddResepForm(
    bitmap: Bitmap?,
    onImageClick: () -> Unit,
    onSaveClick: (String, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var judul by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var bahan by remember { mutableStateOf("") }
    var instruksi by remember { mutableStateOf("") }
    val isEnabled = judul.isNotEmpty() && deskripsi.isNotEmpty() && bahan.isNotEmpty() && instruksi.isNotEmpty() && bitmap != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            val imageModifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(stringResource(R.string.klik_untuk_tambah_gambar), modifier = imageModifier.wrapContentSize())
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = judul,
            onValueChange = { judul = it },
            label = { Text(text = stringResource(R.string.judul)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text(text = stringResource(R.string.deskripsi)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField( value = bahan,
            onValueChange = { bahan = it },
            label = { Text(text = stringResource(R.string.bahan)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth().height(100.dp) )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField( value = instruksi,
            onValueChange = { instruksi = it },
            label = { Text(text = stringResource(R.string.instruksi)) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth().height(150.dp) )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onSaveClick(judul, deskripsi, bahan, instruksi) },
            enabled = isEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.simpan))
        }
    }
}