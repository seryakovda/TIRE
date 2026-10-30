package com.example.tire.screens.tire.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tire.data.RemoteSource

@Composable
fun TireViewContent(
    sessionHandle: String,
    id_image: String,
    remoteSource: RemoteSource
) {
    val context = LocalContext.current

    val imageBytes = produceState<ByteArray?>(
        initialValue = null,
        sessionHandle,
        id_image
    ) {
        value = try {
            remoteSource.getImageBig(
                sessionHandle = sessionHandle,
                idImage = id_image
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    Column(
        modifier = Modifier.padding(5.dp)
    ) {
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
        ) {
            Box {
                imageBytes.value?.let { bytes ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(bytes)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}