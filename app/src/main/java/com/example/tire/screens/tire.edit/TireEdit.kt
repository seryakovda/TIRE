package com.example.tire.screens.main.tire

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tire.data.TireList
import com.example.tire.ui.theme.TireTheme


@Composable
fun TireEdit(
    //tireList: State<List<TireList>?>,
    //serialNumber: String
) {



}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TireEdit_Preview() {
    TireTheme {
        TireEdit()
    }
}