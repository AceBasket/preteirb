package com.example.preteirb.common


import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomGlideImage(
    model: Any?,
    placeholder: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    loading: Placeholder? = null,
    failure: Placeholder? = null,
    colorFilter: ColorFilter? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val useGlideImage: Boolean =
        (model is String && model.isNotBlank()) || (model is Uri && model != Uri.EMPTY)
    if (useGlideImage) {
        GlideImage(
            model = model,
            contentDescription = contentDescription,
            loading = loading,
            failure = failure,
            contentScale = contentScale,
            modifier = modifier,
        )
    } else {
        Image(
            imageVector = placeholder,
            contentDescription = contentDescription,
            contentScale = contentScale,
            colorFilter = colorFilter,
            modifier = modifier
        )
    }
}