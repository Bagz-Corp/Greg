package com.gcorp.multirecherche3d.ui.designSystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gcorp.multirecherche3d.domain.model.Thumbnail

data class CarouselItem(
    val id: Int,
    val imageUrl: String,
    val contentDescription: String,
)

/** Will be used once we've got enough pictures to display to justify a Carousel */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroCarousel(
    modifier: Modifier = Modifier,
    thumbnails: List<Thumbnail>
) {

    val items = thumbnails.mapIndexed { index, thumbnail ->
        CarouselItem(
            id = index,
            imageUrl = thumbnail.url,
            contentDescription = ""
        )
    }

    val carouselState = rememberCarouselState { items.count() }

    HorizontalCenteredHeroCarousel(
        state = carouselState,
        modifier = modifier.fillMaxHeight(),
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(16.dp)
    ) {i ->
        val item = items[i]

        println("HeroCarousel - item $item")

        AsyncImage(
            model = item.imageUrl,
            // placeholder = painterResource(Color.LightGray.toArgb()),
            contentDescription = item.contentDescription,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .maskClip(MaterialTheme.shapes.extraLarge),
        )

//        when (item.painterState) {
//            is AsyncImagePainter.State.Empty,
//            is AsyncImagePainter.State.Loading -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .shimmer()
//                )
//            }
//
//            is AsyncImagePainter.State.Success -> {
//                Image(
//                    modifier = Modifier.fillMaxSize(),
//                    painter = item.painter,
//                    contentScale = ContentScale.FillBounds,
//                    contentDescription = null
//                )
//            }
//
//            is AsyncImagePainter.State.Error -> {
//                // Show some error UI.
//                Text(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(color = Color.LightGray)
//                        .padding(8.dp),
//                    text = "Some Error happened"
//                )
//            }
//        }
    }
}