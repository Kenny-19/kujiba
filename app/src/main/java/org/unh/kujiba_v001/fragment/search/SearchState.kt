package org.unh.kujiba_v001.fragment.search

import androidx.compose.runtime.mutableStateOf

class SearchState(initialQuery: String = "") {
    val queryState = mutableStateOf(initialQuery)
}
