package com.example.translateapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.translateapp.languages.LanguagesView
import com.example.translateapp.translator.TranslateView
import com.example.translateapp.translator.TranslateViewModel
import com.example.translateapp.ui.theme.TranslateAppTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: TranslateViewModel by viewModels()

        // enableEdgeToEdge()
        setContent {
            TranslateAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    TranslateView(viewModel)
                }
            }
        }
    }
}

@Composable
fun  MyView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.title), fontWeight = FontWeight.Bold)
        Text(text = stringResource(id = R.string.subtitle))
    }
}