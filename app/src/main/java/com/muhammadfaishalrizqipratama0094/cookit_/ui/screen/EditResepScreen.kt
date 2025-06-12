package com.muhammadfaishalrizqipratama0094.cookit_.ui.screen

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.muhammadfaishalrizqipratama0094.cookit_.R
import com.muhammadfaishalrizqipratama0094.cookit_.model.Resep
import com.muhammadfaishalrizqipratama0094.cookit_.network.ResepApi
import com.muhammadfaishalrizqipratama0094.cookit_.network.UserDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditResepScreen(
    navController: NavController,
    viewModel: MainViewModel,
    resepId: String
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    // Cari resep berdasarkan ID
    val resep = remember(resepId) { viewModel.getResepById(resepId) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        bitmap = getCroppedImage(context.contentResolver, result)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.edit_resep)) },
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
        if (resep == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.resep_tidak_ditemukan))
            }
        } else {
            EditResepForm(
                resep = resep,
                bitmap = bitmap,
                modifier = Modifier.padding(padding),
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
                onSaveClick = { _, judul, deskripsi, bahan, instruksi, newBitmap ->
                    coroutineScope.launch {
                        val user = UserDataStore(context).userFlow.first()
                        if (user.email.isNotEmpty()) {
                            viewModel.updateData(user.email, resep.id, judul, deskripsi, bahan, instruksi, newBitmap) {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun EditResepForm(
    resep: Resep,
    bitmap: Bitmap?,
    modifier: Modifier = Modifier,
    onImageClick: () -> Unit,
    onSaveClick: (String, String, String, String, String, Bitmap?) -> Unit
) {
    var judul by remember { mutableStateOf(resep.judul) }
    var deskripsi by remember { mutableStateOf(resep.deskripsi) }
    var bahan by remember { mutableStateOf(resep.bahan) }
    var instruksi by remember { mutableStateOf(resep.instruksi) }
    val isEnabled = judul.isNotEmpty() && deskripsi.isNotEmpty() && bahan.isNotEmpty() && instruksi.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { onImageClick() }) {
            val imageModifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = stringResource(R.string.edit_gambar),
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = ResepApi.getImageUrl(resep.imageId),
                    contentDescription = stringResource(R.string.edit_gambar),
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_img)
                )
            }
            Text(
                text = stringResource(R.string.klik_untuk_ubah_gambar),
                color = Color.White,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
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
            onClick = { onSaveClick(resep.id, judul, deskripsi, bahan, instruksi, bitmap) },
            enabled = isEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.simpan_perubahan))
        }
    }
}