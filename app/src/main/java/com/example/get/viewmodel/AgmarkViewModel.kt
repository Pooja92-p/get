package com.example.get.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.get.scraper.AgmarkScraper

class AgmarkViewModel : ViewModel() {
    private val scraper = AgmarkScraper()

    private val _marketData = MutableStateFlow<List<List<String>>>(emptyList())
    val marketData = _marketData.asStateFlow()

    fun fetchTomatoMysore() {
        viewModelScope.launch {
            val rows = scraper.fetchTomatoMysore()
            _marketData.value = rows
        }
    }
}
