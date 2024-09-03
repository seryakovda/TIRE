package com.example.tire.screens.selection.tire

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tire.Destination


@Composable
fun SelectionTireScreen(
    navController: NavHostController,
    viewModel: SelectionTireViewModel = hiltViewModel()
) {
    val serialNumber by viewModel.serialNumber.observeAsState("")
    val chassisNumber by viewModel.chassisNumber.observeAsState("")
    val tireList by viewModel.tireList.observeAsState()

    SelectionTireContent(
        serialNumber,
        chassisNumber,
        tireList,
        navigateToEdit = { navController.navigate(Destination.ScreenEditTire.route) },
        { viewModel.changeSerialNumber(it) },
        { viewModel.changeСhassisNumber(it) },
        { viewModel.selectedTire(it) },
        { viewModel.addTire(it) }
    )
}
