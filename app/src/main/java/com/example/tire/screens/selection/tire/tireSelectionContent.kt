package com.example.tire.screens.selection.tire

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tire.data.TireList
import com.example.tire.ui.theme.TireTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionTireContent(
    serialNumber: String,
    chassisNumber: String,
    tireList: List<TireList>?,
    navigateToEdit : () -> Unit = { },
    changeSerialNumber: (String) -> Unit,
    changeСhassisNumber: (String) -> Unit,
    selectedTire: (TireList) -> Unit,
    addTire: (String) -> Unit,
) {


    Column {
        Row {
            InputFilter(
                serialNumber,
                chassisNumber,
                changeSerialNumber,
                changeСhassisNumber,
            )
        }
        Row {
            TireList(
                tireList,
                serialNumber,
                selectedTire,
                navigateToEdit,
                addTire
            )
        }
    }

}



@Preview(showBackground = true, showSystemUi = false)
@Composable
fun MainTireContent_Preview() {
    TireTheme {
        //MainTireContent()
    }
}