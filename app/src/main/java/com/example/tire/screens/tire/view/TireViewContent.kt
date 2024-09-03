package com.example.tire.screens.tire.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tire.ConstApp

@Composable
fun TireViewContent(
    sessionHandle: String,
    id_image:String,

) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ){
        val requestTxt ="${ConstApp.urlRemoteServer}?" +
                "r0=SYS" +
                "&r1=getImageBig" +
                "&sessionHandle=$sessionHandle" +
                "&id_image=$id_image"
        Row(
            modifier = Modifier
                .verticalScroll(
                    enabled = true,
                    state = ScrollState(0),
                    flingBehavior = null,
                    reverseScrolling = false
                )
                .horizontalScroll(
                    enabled = true,
                    state = ScrollState(0),
                    flingBehavior = null,
                    reverseScrolling = false
                )

        ){
            Box(
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        // картинка 1
                        .data(requestTxt)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,

                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}