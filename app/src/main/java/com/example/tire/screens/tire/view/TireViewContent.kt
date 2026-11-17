
package com.example.tire.screens.tire.view

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tire.data.RemoteSource
import kotlin.math.max

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

    val scale = remember(id_image) {
        mutableFloatStateOf(1f)
    }

    val offsetX = remember(id_image) {
        mutableFloatStateOf(0f)
    }

    val offsetY = remember(id_image) {
        mutableFloatStateOf(0f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .clipToBounds()
            .pointerInput(id_image) {
                detectTransformGestures { _, pan, zoom, _ ->

                    val oldScale = scale.floatValue
                    val newScale = (oldScale * zoom).coerceIn(1f, 5f)

                    scale.floatValue = newScale

                    if (newScale <= 1f) {
                        offsetX.floatValue = 0f
                        offsetY.floatValue = 0f
                    } else {
                        val scaleFactor = newScale / oldScale

                        // Учитываем изменение масштаба
                        var newOffsetX =
                            offsetX.floatValue * scaleFactor + pan.x

                        var newOffsetY =
                            offsetY.floatValue * scaleFactor + pan.y

                        // Максимально допустимое смещение
                        val maxX = max(
                            0f,
                            size.width * (newScale - 1f) / 2f
                        )

                        val maxY = max(
                            0f,
                            size.height * (newScale - 1f) / 2f
                        )

                        // Ограничиваем перемещение
                        newOffsetX = newOffsetX.coerceIn(-maxX, maxX)
                        newOffsetY = newOffsetY.coerceIn(-maxY, maxY)

                        offsetX.floatValue = newOffsetX
                        offsetY.floatValue = newOffsetY
                    }
                }
            }
    ) {
        imageBytes.value?.let { bytes ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(bytes)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale.floatValue
                        scaleY = scale.floatValue
                        translationX = offsetX.floatValue
                        translationY = offsetY.floatValue
                    }
            )
        }
    }
}
