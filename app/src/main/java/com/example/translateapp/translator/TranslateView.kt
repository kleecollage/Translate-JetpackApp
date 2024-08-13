package com.example.translateapp.translator

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.translateapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TranslateView(viewModel: TranslateViewModel) {
    val state = viewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val languageOptions = viewModel.languageOptions
    val itemSelection = viewModel.itemSelection
    val itemVoice = viewModel.itemsVoice
    var indexSource by remember { mutableIntStateOf(0) }
    var indexTarget by remember { mutableIntStateOf(1) }
    var expandSource by remember { mutableStateOf(false) }
    var expandTarget by remember { mutableStateOf(false) }
    var selectedSourceLang by remember { mutableStateOf(languageOptions[0]) }
    var selectedTargetLang by remember { mutableStateOf(languageOptions[1]) }
    var selectedTargetVoice by remember { mutableStateOf(itemVoice[1]) }
    val permissionState = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)

    SideEffect {
        permissionState.launchPermissionRequest()
    }

    val speechRecognitionLauncher = rememberLauncherForActivityResult(
        contract = SpeechRecognizerContract()) {
        viewModel.onValue(
             it.toString()
            .replace("[", "")
            .replace("]", "")
            .trimStart())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DropdownLang(
                itemSelection = itemSelection,
                selectedIndex = indexSource,
                expand = expandSource,
                onClickExpanded = { expandSource = true },
                onClickDismiss = { expandSource = false },
                onClickItem = { index ->
                    indexSource = index
                    selectedSourceLang = languageOptions[index]
                    expandSource = false
                }
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 15.dp)
            )

            DropdownLang(
                itemSelection = itemSelection,
                selectedIndex = indexTarget,
                expand = expandTarget,
                onClickExpanded = { expandTarget = true },
                onClickDismiss = { expandTarget = false },
                onClickItem = { index ->
                    indexTarget = index
                    selectedTargetLang = languageOptions[index]
                    selectedTargetVoice = itemVoice[index]
                    expandTarget = false
                }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = state.textToTanslate,
            onValueChange = { viewModel.onValue(it) },
            label = { Text(text = "Escribe ...")},
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // lo que hace el enter en el teclado ...
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.onTranslate(
                        state.textToTanslate,
                        context,
                        selectedSourceLang,
                        selectedTargetLang
                    )
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            MainIconButton(icon = R.drawable.mic) {
                if (permissionState.status.isGranted) {
                    speechRecognitionLauncher.launch(Unit)
                } else  {
                    permissionState.launchPermissionRequest()
                }
            }
            MainIconButton(icon = R.drawable.translate) {
                viewModel.onTranslate(
                    state.textToTanslate,
                    context,
                    selectedSourceLang,
                    selectedTargetLang
                )
            }
            MainIconButton(icon = R.drawable.speek) {
                viewModel.textToSpeech(context, selectedTargetVoice)
            }
            MainIconButton(icon = R.drawable.delete) {
                viewModel.clean()
            }
        }

        if (state.isDownloading) {
            CircularProgressIndicator()
            Text(text = "Descargando Modelo ...")
        } else {
            OutlinedTextField(
                value = state.translateText,
                onValueChange = {},
                label = { Text(text = "Tu texto traducido ...")},
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }

    }
}