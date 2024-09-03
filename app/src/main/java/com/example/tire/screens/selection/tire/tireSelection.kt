package com.example.tire.screens.selection.tire

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.engine.Resource
import com.example.tire.R
import com.example.tire.data.TireList
import com.example.tire.ui.theme.TireTheme


@Composable
fun TireList(
    tireList: List<TireList>?,
    serialNumber: String,
    selectedTire: (TireList) -> Unit,
    navigateToEdit: () -> Unit = { },
    addTire: (String) -> Unit,
) {
    val tL = tireList
    Column(
        modifier = Modifier.verticalScroll(
            enabled = true,
            state = ScrollState(0),
            flingBehavior = null,
            reverseScrolling = false
        )
    ) {
        if (tL != null) { // Если не Null
            if (!tL.isEmpty()){ // и не пустой то выводим перечисление
                tL.forEach {
                    Row (modifier = Modifier
                        .padding(top = 3.dp, start = 3.dp, bottom = 3.dp, end = 3.dp)

                        .background(
                            color = Color(0xFFebf7ff),
                            shape = RoundedCornerShape(4.dp)
                        )

                    ) {
                        Column {
                            Text(text = it.SN
                                ,fontSize = 30.sp
                                ,color = Color(0xFF000000)
                                ,modifier = Modifier
                                    .padding(top = 5.dp, start = 5.dp)
                                    .fillMaxWidth(0.85f)
                            )
                            Text(text = "Типоразмер ${it.standardSize}"
                                ,fontSize = 18.sp
                                ,color = Color(0xFF000000)
                                ,modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                            Text(text = "Модель ${it.model}"
                                ,fontSize = 18.sp
                                ,color = Color(0xFF000000)
                                ,modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                            Text(text = "Брэнд ${it.brand}"
                                ,fontSize = 18.sp
                                ,color = Color(0xFF000000)
                                ,modifier = Modifier
                                    .padding(start = 5.dp, bottom = 5.dp)
                            )
                            Text(text = "Шасси ${it.chassisNumber}"
                                ,fontSize = 18.sp
                                ,color = Color(0xFF000000)
                                ,modifier = Modifier
                                    .padding(start = 5.dp)
                            )

                            Text(text = "[[ ${it.positionOnChassis} ]], пробег - ${it.mileage}"
                                ,fontSize = 18.sp
                                ,color = Color(0xFF000000)
                                ,modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(end = 5.dp)
                                .clickable {
                                    selectedTire(it)
                                    navigateToEdit()

                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.select) ,
                                contentDescription = ""
                            )
                        }
                    }

                }
            }else {// получается, что по фильтру не нашлось записей значит выводи кнопку с добавлением
                if (serialNumber.isNotEmpty()) {
                    Row {
                        Button(
                            onClick = {
                                addTire(serialNumber)
                            }
                        )
                        {
                            Text(
                                "Добавить S№-$serialNumber", modifier = Modifier
                                    .fillMaxWidth(), fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TireList_Preview() {
    TireTheme {
        //TireList()
    }
}