package com.gcorp.multirecherche3d.ui.designSystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.ui.shimmer
import com.gcorp.multirecherche3d.ui.theme.SoftGrey
import com.gcorp.multirecherche3d.ui.theme.Typography

@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    cardData: ModelItem,
    onClick: (String) -> Unit
) {

    ElevatedCard(
        modifier = modifier
            .width(300.dp)
            .height(300.dp)
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors().copy(
            containerColor = SoftGrey,
            contentColor = Color.Black
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            ResultCardImage(
                modifier = Modifier.weight(1f),
                imageUrl = cardData.thumbnails.maxBy { it.width }.url.toString()
            )

// For the moment, the Carousel is not used because we do not have enough pic url to display,
//            HeroCarousel(
//                modifier = Modifier
//                    .weight(1f),
//                thumbnails = cardData.thumbnails
//            )

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = cardData.title,
                style = Typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = "Likes : ${cardData.likeCount}",
                style = MaterialTheme.typography.labelSmall,
            )

            ResultCardBottom(
                cardUrl = cardData.url,
                onClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(20.dp))
    ) {
        val painter = rememberAsyncImagePainter(
            model = imageUrl
        )
        val state by painter.state.collectAsStateWithLifecycle()

        when (state) {
            is AsyncImagePainter.State.Empty,
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer()
                )
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
            }

            is AsyncImagePainter.State.Error -> {
                // Show some error UI.
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.LightGray)
                        .padding(8.dp),
                    text = "Some Error happened"
                )
            }
        }
    }
}

@Composable
fun ResultCardBottom(
    modifier: Modifier = Modifier,
    cardUrl: String,
    onClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        ElevatedButton(
            onClick = {
                onClick(cardUrl)
            }
        ) {
            Text(
                text = "Voir modéle"
            )
        }

        IconButton(
            onClick = { /* Like */ }
        ) {
            Icon(
                Icons.Outlined.FavoriteBorder,
                contentDescription = "Ajouter aux favoris"
            )
        }
    }
}

// Preview is commented because of a conflict of JVM target used in Coil previewHandler (11)
// and compiled one (8)
//@OptIn(ExperimentalCoilApi::class)
//@Preview
//@Composable
//private fun ResultCardPreview() {
//    // Needed to override preview behavior to disable network
//    val previewHandler = AsyncImagePreviewHandler {
//        ColorImage(Color.LightGray.toArgb())
//    }
//
//    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
//        GregTheme {
//            ResultCard(
//                cardData = ModelItem(
//                    thumbnails = listOf(
//                        Thumbnail(
//                            url = URI("someUrl"),
//                            width = 200,
//                            height = 200
//                        ),
//                        Thumbnail(
//                            url = URI("someUrl"),
//                            width = 200,
//                            height = 200
//                        ),
//                        Thumbnail(
//                            url = URI("someUrl"),
//                            width = 200,
//                            height = 200
//                        ),
//                    ),
//                    title = "Preview Title Superlong",
//                    likeCount = 42,
//                    url = "Preview URI"
//                ),
//                onClick = {}
//            )
//        }
//    }
//}