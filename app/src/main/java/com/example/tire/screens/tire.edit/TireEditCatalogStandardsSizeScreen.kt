package com.example.tire.screens.tire.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun TireEditCatalogStandardsSizeScreen(
    navController: NavHostController,
    viewModel: TireEditCatalogViewModel = hiltViewModel()
) {
    val listString          by viewModel.listString.observeAsState()
    val filterString        by viewModel.filterString.observeAsState("")

    viewModel.getCatalogStandardSize(filterString)
    TireEditCatalogList(
        "Типоразмер",
        listString,
        selectionFunc = viewModel::setValueStandardSize,
        navController,
        filterString,
        viewModel::getCatalogStandardSize
    )
}