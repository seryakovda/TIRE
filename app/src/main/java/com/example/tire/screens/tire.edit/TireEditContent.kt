package com.example.tire.screens.tire.edit


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavHostController
import com.example.tire.R
import com.example.tire.data.ImageList
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.tire.ConstApp.urlRemoteServer
import com.example.tire.Destination
import com.example.tire.data.ImageListLocal
import com.example.tire.data.TireList
import com.example.tire.di.ComposeFileProvider
import com.example.tire.ui.theme.TireTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TireEditContent(
    tireEditData:  TireList?,
    navigateToSelect: NavHostController,
    imageList:List<ImageList>?,
    imageListLocal:List<ImageListLocal>?,
    sessionHandle: String,
    context:Context,
    imageUri: (uri:Uri) -> Unit = { },
    hasImage: (uri:Boolean) -> Unit = { },
    setIdImageForViewFullScreen: (String) -> Unit = { },
    uriFormGallery: (List<Uri>) -> Unit = { },
    hasIriFormGallery : (Boolean) -> Unit = { },
    idTireEvents: (uri:Int) -> Unit = { }
    ) {



    Column{
        Row(
            modifier = Modifier
                .padding(top = 3.dp, start = 3.dp, bottom = 3.dp, end = 3.dp)
        ) {
            Column {
                tireEditData?.let {
                    rowData("",                     tireEditData.SN,                R.drawable.img_search_sn, navigateToSelect,fontSize = 28)
                }
            }
        }

        Column(
            modifier = Modifier.verticalScroll(
                enabled = true,
                state = ScrollState(0),
                flingBehavior = null,
                reverseScrolling = false
            )
        ){
            Column {
                tireEditData?.let {
                    rowData("Типоразмер: ",         tireEditData.standardSize,      R.drawable.img_edit, navigateToSelect,route = Destination.ScreenEditTireStandardSize.route ,        noEdit = true)
                    rowData("Модель: ",             tireEditData.model,             R.drawable.img_edit, navigateToSelect,route = Destination.ScreenEditTireModel.route ,               noEdit = true)
                    rowData("Брэнд: ",              tireEditData.brand,             R.drawable.img_edit, navigateToSelect,route = Destination.ScreenEditTireBrand.route,                noEdit = true)
                    rowData("Номер шасси: ",        tireEditData.chassisNumber,     R.drawable.img_edit, navigateToSelect,route = Destination.ScreenEditTireChassisNumber.route ,       noEdit = true)
                    rowData("Последняя дата - ",    tireEditData.lastEvent,         navigateToSelect = navigateToSelect,                                                                noEdit = true)
                    rowData("Событие - ",           tireEditData.event,             navigateToSelect = navigateToSelect,                                                                noEdit = true)
                    rowData("Причина - ",           tireEditData.reason,            navigateToSelect = navigateToSelect,                                                                noEdit = true)
                    rowData("Статус - ",            tireEditData.status,            navigateToSelect = navigateToSelect,                                                                noEdit = true)
                    rowData("позиция - ",           tireEditData.positionOnChassis, navigateToSelect = navigateToSelect,                                                                noEdit = true)
                    rowData("Протектор - ",         tireEditData.heightTread,       navigateToSelect = navigateToSelect,                                                                noEdit = true)
                    rowData("Пробег - ",            tireEditData.mileage,           navigateToSelect = navigateToSelect,                                                                noEdit = true)
                }
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Button(
                    onClick = {
                        navigateToSelect.navigate(Destination.ScreenAddEvent.route)
                    },
                    Modifier.width(220.dp),
                    //shape = RectangleShape,
                    //contentPadding = PaddingValues(0.dp),
                ) {
                    Text("Добавить событие (сход/фиксация)",
                        fontSize = 18.sp,
                        style = TextStyle(
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both
                            )
                        )
                    )
                }
            }
            tireEditData?.events?.let{
                it.forEach {
                    val id = it.id

                    Row (modifier = Modifier
                        .padding(1.dp)
                        .background(
                            color = Color(0xFFebf7ff),
                            shape = RoundedCornerShape(2.dp)
                        )

                    ) {
                        Column {
                            Text(
                                text = "${it.name_TypeTireEvent} - ${it.dateEvent}",
                                color = Color(0xFF000000),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 4.dp, start = 15.dp)
                                    .fillMaxWidth()
                            )
                            ButtonsImage(context,imageUri,hasImage,uriFormGallery,hasIriFormGallery,id,idTireEvents)
                        }
                    }

                    it.listPhoto?.let {
                        printImages(imageListLocal,it,sessionHandle,setIdImageForViewFullScreen,navigateToSelect,id)
                    }
                }
            }

        }
    }

}









@Composable
fun ButtonsImage(
    context:Context,
    imageUri: (uri:Uri) -> Unit = { },
    hasImage: (uri:Boolean) -> Unit = { },
    uriFormGallery: (List<Uri>) -> Unit = { },
    hasIriFormGallery : (Boolean) -> Unit = { },
    id_event:Int,
    idTireEvents: (uri:Int) -> Unit = { },
)
{
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            idTireEvents(id_event)
            hasImage(success)
        }
    )

    //var selectImages by remember { mutableStateOf(listOf<Uri>()) }
    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents(),
            //contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = {
                uriFormGallery(it)
                idTireEvents(id_event)
                if (it.isEmpty()){
                    hasIriFormGallery(false)
                }
                else{
                    hasIriFormGallery(true)
                }
            }
        )



    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Box(
            modifier = Modifier
                .size(100.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.photoapparat),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(5.dp)
                    .size(70.dp)
                    .clickable {
                        val uri = ComposeFileProvider.getImageUri(context)
                        imageUri(uri)
                        cameraLauncher.launch(uri)
                    }

            )
        }
        Box(
            modifier = Modifier
                .size(100.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.gallery),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(5.dp)
                    .size(70.dp)
                    .clickable {
                        galleryLauncher.launch("image/*")
                    }
            )
        }
    }

}

@Composable
fun printImages(
    imageListLocal:List<ImageListLocal>?,
    imageList:List<ImageList>?,
    sessionHandle:String,
    setIdImageForViewFullScreen: (String) -> Unit = { },
    navigateToSelect: NavHostController,
    id_event:Int,
    )
{
    val iL1 = imageListLocal
    Column {
        if (iL1 != null) { // Если не Null
            if (!iL1.isEmpty()) { // и не пустой то выводим перечисление
                iL1.forEach {
                    if (id_event == it.idTireEvents){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Card (
                                modifier = Modifier
                                    .padding(10.dp)
                                    .height(300.dp)
                                    .width(300.dp)
                                    .clickable {
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                shape = RectangleShape,
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(it.fileName),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }

    }
    val iL = imageList

    Column{
        if (iL != null) { // Если не Null
            if (!iL.isEmpty()) { // и не пустой то выводим перечисление
                iL.forEach {
                    val requestTxt ="$urlRemoteServer?" +
                            "r0=SYS" +
                            "&r1=getImageSmall" +
                            "&sessionHandle=$sessionHandle" +
                            "&id_image=${it.id_image}"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        Card (
                            modifier = Modifier
                                .padding(10.dp)
                                .height(300.dp)
                                .width(300.dp)
                                .clickable {
                                    setIdImageForViewFullScreen(it.id_image)
                                    navigateToSelect.navigate(Destination.ScreenViewRemoteImage.route)
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RectangleShape,
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    // картинка 1
                                    .data(requestTxt)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rowData(
    caption:String,
    data:String,
    resourceImage:Int = 0,
    navigateToSelect: NavHostController,
    route:String? = null,
    fontSize:Int = 20,
    noEdit:Boolean = false
){
    Row (modifier = Modifier
        .padding(1.dp)
        .background(
            color = Color(0xFFebf7ff),
            shape = RoundedCornerShape(2.dp)
        )

    ){
        var width:Float
        if (noEdit)
            width = 1f
        else
            width = 0.85f
        Column {
            Text(
                text = "$caption$data",
                color = Color(0xFF000000),
                fontSize = fontSize.sp,
                modifier = Modifier
                    .padding(top = 4.dp, start = 15.dp)
                    .fillMaxWidth(width)
            )
        }
        if (!noEdit) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .clickable {
                        if (route == null) {
                            navigateToSelect.navigateUp()
                        } else {
                            navigateToSelect.navigate(route)
                        }

                    }
            ) {
                Image(
                    painter = painterResource(id = resourceImage),
                    contentDescription = ""
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ImageItemPreview() {
    TireTheme {
        Card (
            modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
                .width(100.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RectangleShape,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_edit),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
        }
    }
}
