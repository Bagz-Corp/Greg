package com.gcorp.multirecherche3d.ui.designSystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.domain.model.Thumbnail
import com.gcorp.multirecherche3d.domain.model.getBigger
import com.gcorp.multirecherche3d.ui.shimmer
import com.gcorp.multirecherche3d.ui.theme.Charcoal
import com.gcorp.multirecherche3d.ui.theme.GregTheme
import com.gcorp.multirecherche3d.ui.theme.LuckyRed
import com.gcorp.multirecherche3d.ui.theme.OffWhite
import com.gcorp.multirecherche3d.ui.theme.SoftGrey
import com.gcorp.multirecherche3d.ui.theme.Typography

@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    cardData: ModelItem,
    onClick: (String) -> Unit,
    onFavorite: (Int, Boolean) -> Unit
) {
    Card(
        modifier = modifier
            .width(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick(cardData.url) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = OffWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.background(color = OffWhite)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
            ) {
                ResultCardImage(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = cardData.thumbnails.getBigger().url
                )
                
                // Favorite Button Overlay
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .size(36.dp),
                    shape = CircleShape,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = OffWhite)
                            .clickable { onFavorite(cardData.id, !cardData.isFavorite) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (cardData.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (cardData.isFavorite) LuckyRed else Charcoal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = cardData.title,
                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Charcoal
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = SoftGrey.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = cardData.sectionName,
                            style = Typography.labelSmall,
                            color = Charcoal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${cardData.likeCount}",
                            style = Typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    Box(modifier = modifier) {
        val painter = rememberAsyncImagePainter(model = imageUrl)
        val state by painter.state.collectAsStateWithLifecycle()

        when (state) {
            is AsyncImagePainter.State.Empty,
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer()
                        .background(SoftGrey)
                )
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }

            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SoftGrey),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Image non disponible",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 1)
@Composable
private fun ResultCardPreview() {
    GregTheme {
        ResultCard(
            modifier = Modifier,
            cardData = ModelItem(
                id = 1,
                sectionName = "section name",
                thumbnails = listOf(
                    Thumbnail(
                        url = "url"
                    )
                ),
                title = "title",
                likeCount = 42,
                url = "url",
                isFavorite = true
            ),
            onClick = {},
            onFavorite = {_, _ ->}
        )
    }
}