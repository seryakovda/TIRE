package com.example.tire.screens.tire.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun TireEditCatalogModelScreen(
    navController: NavHostController,
    viewModel: TireEditCatalogViewModel = hiltViewModel()
) {
    val listString          by viewModel.listString.observeAsState()
    val filterString        by viewModel.filterString.observeAsState("")

    viewModel.getCatalogModel(filterString)
    TireEditCatalogList(
        "Модель",
        listString,
        selectionFunc = viewModel::setValueModel,
        navController,
        filterString,
        viewModel::getCatalogModel
    )
}