package com.example.tire.screens.tire.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController



@Composable
fun TireEditCatalogBrandScreen(
    navController: NavHostController,
    viewModel: TireEditCatalogViewModel = hiltViewModel(),
) {
    val listString          by viewModel.listString.observeAsState()
    val filterString        by viewModel.filterString.observeAsState("")

    viewModel.getCatalogBrand(filterString)
    TireEditCatalogList(
        "Брэнд",
        listString,
        viewModel::setValueBrand,
        navController,
        filterString,
        viewModel::getCatalogBrand
    )
}