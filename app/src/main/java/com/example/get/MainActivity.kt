package com.example.get

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.get.viewmodel.AgmarkViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: AgmarkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchTomatoMysore()


        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val data by viewModel.marketData.collectAsState()
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tomato (Mysore) Prices", style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(8.dp))
                        LazyColumn {
                            items(data) { row ->
                                Text(row.joinToString(" | "))
                                Spacer(Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
