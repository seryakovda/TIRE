package com.example.tire.screens.tire.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController



@Composable
fun TireEditSelectEventScreen(
    navController: NavHostController,
    viewModel: TireEditCatalogViewModel = hiltViewModel(),
) {
    val listTireEvents          by viewModel.listTireEvents.observeAsState()
    val filterString        by viewModel.filterString.observeAsState("")

    viewModel.getListEvents(filterString)
    TireEditSelectEventContent(
        listTireEvents,
        viewModel::addEventForTire,
        navController,
        filterString,
        viewModel::getCatalogBrand
    )
}