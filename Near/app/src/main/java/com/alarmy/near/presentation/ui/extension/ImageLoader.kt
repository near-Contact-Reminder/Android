package com.alarmy.near.presentation.ui.extension

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alarmy.near.R

/**
 * 이미지 로딩 확장 함수
 * Coil 라이브러리를 사용하여 이미지를 비동기적으로 로드합니다.
 */
@Composable
fun ImageLoader(
    uri: String?,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Int = R.drawable.img_80_user1,
    error: Int = R.drawable.img_80_user1,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    if (!uri.isNullOrEmpty()) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(uri)
                    .crossfade(true)
                    .build(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            placeholder = painterResource(id = placeholder),
            error = painterResource(id = error),
        )
    } else {
        // URI가 null이거나 비어있을 경우 기본 이미지 표시
        Image(
            painter = painterResource(id = placeholder),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}
