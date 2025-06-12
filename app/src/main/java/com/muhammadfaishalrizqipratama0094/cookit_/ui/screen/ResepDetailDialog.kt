package com.muhammadfaishalrizqipratama0094.cookit_.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.muhammadfaishalrizqipratama0094.cookit_.R
import com.muhammadfaishalrizqipratama0094.cookit_.model.Resep
import com.muhammadfaishalrizqipratama0094.cookit_.network.ResepApi

@Composable
fun ResepDetailDialog(resep: Resep, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = ResepApi.getImageUrl(resep.imageId ?: ""),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_img),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(resep.judul, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(resep.deskripsi, style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.bahan), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(resep.bahan, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.instruksi), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(resep.instruksi, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.tutup))
                }
            }
        }
    }
}