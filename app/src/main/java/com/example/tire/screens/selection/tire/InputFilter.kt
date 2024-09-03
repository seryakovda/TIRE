package com.example.tire.screens.selection.tire

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp


@Composable
fun InputFilter(
    serialNumber: String,
    chassisNumber: String,
    changeSerialNumber: (String) -> Unit,
    changeСhassisNumber: (String) -> Unit,

    ) {

    Row() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                label = { Text(text = "Серийный номер") },
                value =  serialNumber,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(fontSize = 20.sp),
//                colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.tertiary),
                onValueChange = {
                    changeSerialNumber(it)
                }

            )

            TextField(
                label = { Text(text = "Номер шасси") },
                value = chassisNumber,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(fontSize = 20.sp),

                onValueChange = {
                    changeСhassisNumber(it)
                }
            )
            /*
            Button(
                onClick = {}
            )
            {
                Text(
                    "Отфильтровать", modifier = Modifier
                        .fillMaxWidth(), fontSize = 16.sp
                )
            }

             */
        }
    }
}
/*
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun InputFilter_Preview() {
    TireTheme {
        InputFilter()
    }
}

 */