package com.example.tire.screens.tire.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun TireEditCatalogDivisionScreen(
    navController: NavHostController,
    viewModel: TireEditCatalogViewModel = hiltViewModel()
) {
    val listString          by viewModel.listString.observeAsState()
    val filterString        by viewModel.filterString.observeAsState("")

    viewModel.getCatalogDivision(filterString)
    TireEditCatalogList(
        "Участок",
        listString,
        selectionFunc = viewModel::setValueDivision,
        navController,
        filterString,
        viewModel::getCatalogDivision
    )
}