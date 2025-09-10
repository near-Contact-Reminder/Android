package com.alarmy.near.presentation.ui.extension

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.alarmy.near.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions

/**
 * 이미지 로딩 확장 함수
 * TODO Glide 라이브러리에서 Coilㄹ로 마이그레이션 필요
 */
@OptIn(ExperimentalGlideComposeApi::class)
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
        GlideImage(
            model = uri,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
        ) {
            it
                .placeholder(placeholder)
                .error(error)
                .apply(
                    when (contentScale) {
                        ContentScale.Crop -> RequestOptions.centerCropTransform()
                        ContentScale.Fit -> RequestOptions.fitCenterTransform()
                        ContentScale.FillBounds -> RequestOptions.centerInsideTransform()
                        ContentScale.FillHeight -> RequestOptions.centerInsideTransform()
                        ContentScale.FillWidth -> RequestOptions.centerInsideTransform()
                        ContentScale.Inside -> RequestOptions.centerInsideTransform()
                        else -> RequestOptions.centerCropTransform()
                    },
                )
        }
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
