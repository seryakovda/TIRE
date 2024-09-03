package com.example.tire.screens.tire.edit

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tire.ui.theme.TireTheme

@Composable
fun TireEditCatalogList(
    caption         :String,
    catalogList     :List<String>?,
    selectionFunc   :(value:String) -> Unit = {},
    navController   :NavHostController,
    filterString    :String,
    filterFunction  :(name:String) -> Unit = {},

){
    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Row{
            Text(
                caption,
                fontSize = 50.sp,
                modifier = Modifier
                    .background(
                        color = Color(0xFF93C2FF),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .fillMaxWidth()
            )}
        Row{
            TextField(

                label = { Text(text = "Поиск") },

                value =  filterString,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(fontSize = 30.sp),
//                colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.tertiary),
                onValueChange = {
                    filterFunction(it)

                }

            )
        }
        Column(
            modifier = Modifier.verticalScroll(
                enabled = true,
                state = ScrollState(0),
                flingBehavior = null,
                reverseScrolling = false
            )
        ){
            if (catalogList != null) {
                if (!catalogList.isEmpty()) {
                    catalogList.forEach {
                        Row {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .clickable {
                                        selectionFunc(it)
                                        navController.navigateUp()
                                    }
                            ) {
                                Text(
                                    text = it,
                                    color = Color(0xFF000000),
                                    fontSize = 28.sp,
                                    modifier = Modifier
                                        .padding(top = 4.dp, start = 15.dp)
                                        .fillMaxWidth(0.85f)
                                )
                            }
                        }
                    }
                    //RowCatalog(it,selectionFunc)
                } else {

                    Row {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .clickable {
                                    selectionFunc(filterString)
                                    navController.navigateUp()
                                }
                        ) {
                            Text(
                                text = "Создать и выбрать - $filterString",
                                color = Color(0xFF000000),
                                fontSize = 28.sp,
                                modifier = Modifier
                                    .padding(top = 4.dp, start = 15.dp)
                                    .fillMaxWidth(0.85f)
                            )
                        }
                    }
                }
            }
        }
    }
}



/*
@Composable
fun RowCatalog(
    item:String,
    selectionFunc : (String) -> Unit = { },
){

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TireEditContent_Preview() {
    TireTheme {
        TireEditCatalogList(
            "Брэнд",
            listOf(
                "Michelin",
                "TITAN",
                "GOODYEAR",
                "Tianli",
                "Bridgestone",
                "SUPERHAWK",
                "Белшина",
                "MAXAM",
                "Techking",
                "Красный Яр - шина",
                "Luan"
            ),
            {  },
            { }
            )
    }


}

 */