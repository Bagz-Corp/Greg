package com.gcorp.multirecherche3d.ui.designSystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.gcorp.multirecherche3d.domain.model.ModelItem
import com.gcorp.multirecherche3d.ui.shimmer
import com.gcorp.multirecherche3d.ui.theme.GregTheme
import com.gcorp.multirecherche3d.ui.theme.Pink80
import com.gcorp.multirecherche3d.ui.theme.SageGreen
import com.gcorp.multirecherche3d.ui.theme.SlateBlue
import com.gcorp.multirecherche3d.ui.theme.SoftGrey
import com.gcorp.multirecherche3d.ui.theme.Typography

@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    cardData: ModelItem,
    onClick: (String) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = cardData.thumbnails.firstOrNull()?.url?.toString()
    )
    val state by painter.state.collectAsStateWithLifecycle()

    Card(
        modifier = modifier
            .padding(4.dp)
            .width(200.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                if (state is AsyncImagePainter.State.Success) {
                    onClick(cardData.url)
                } else {
                    // Maybe show an error through a SnackBar for the user to wait
                }
            },
        colors = CardDefaults.cardColors().copy(
            containerColor = SlateBlue.copy(alpha = 0.30f),
            contentColor = SoftGrey
        )
    ) {
        Text(
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 4.dp),
            text = cardData.title,
            style = Typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        when(state) {
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 18.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .fillMaxWidth(),
                    painter = painter,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
            }
            is AsyncImagePainter.State.Error -> {
                // Show some error UI.
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 18.dp)
                        .background(color = Pink80.copy(alpha = 0.50f)),
                    text = "Some Error happened"
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier
                .padding(start = 18.dp, bottom = 4.dp),
            text = "Likes : ${cardData.likeCount}",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun ResultCardPreview() {
    GregTheme {
        ResultCard(
            modifier = Modifier.background(color = SageGreen),
            cardData = ModelItem(
                thumbnails = listOf(),
                title = "Preview Title Superlong",
                likeCount = 42,
                url = "Preview URI"
            ),
            onClick = {}
        )
    }
}