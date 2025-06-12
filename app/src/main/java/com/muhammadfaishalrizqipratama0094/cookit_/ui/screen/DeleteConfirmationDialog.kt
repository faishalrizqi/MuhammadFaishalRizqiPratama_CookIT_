package com.muhammadfaishalrizqipratama0094.cookit_.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.muhammadfaishalrizqipratama0094.cookit_.R
import com.muhammadfaishalrizqipratama0094.cookit_.model.Resep

@Composable
fun DeleteConfirmationDialog(
    resep: Resep,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.konfirmasi_hapus)) },
        text = { Text(text = stringResource(R.string.yakin_hapus, resep.judul)) },
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(stringResource(R.string.hapus))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.batal))
            }
        }
    )
}