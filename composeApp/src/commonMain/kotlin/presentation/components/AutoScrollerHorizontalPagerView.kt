package presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Constants
import data.model.CardViewData
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import primaryColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoScrollerHorizontalPagerView(
    modifier: Modifier = Modifier,
    list: List<CardViewData?>?,
    onItemClick: (data: CardViewData) -> Unit
) {

    val pagerState = rememberPagerState() { list?.size ?: 0 }

    Box(
        modifier = Modifier.fillMaxSize().aspectRatio(6 / 9f).background(primaryColor)

    ) {
        HorizontalPager(modifier = Modifier.padding(bottom = 25.dp), state = pagerState) { page ->

            list?.getOrNull(page)?.let { content ->
                PagerViewItem(content) {
                    onItemClick(it)
                }
            }
        }
    }


    LaunchedEffect(key1 = pagerState.currentPage, block = {
        launch {
            while (true) {
                delay(2500)
                withContext(NonCancellable) {
                    if (pagerState.currentPage + 1 in 0..(list?.size?.minus(1) ?: 0)) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        pagerState.animateScrollToPage(0)
                    }
                }
            }
        }
    })

}


@Composable
fun PagerViewItem(content: CardViewData, onItemClick: (data: CardViewData) -> Unit) {


    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable {
                onItemClick(content)
            }


    ) {


        KamelImage(animationSpec = tween(),
            onLoading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmerEffect(),
                )
            },

            resource = asyncPainterResource(data = "${Constants.IMAGE_URL}${content?.poster_path}"),
            contentDescription = content.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize().background(Color(0xFFB8B5B5))
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, primaryColor),
                        startY = size.height / 2,
                        endY = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient)
                    }
                }
        )

        Text(
            text =  content.title.orEmpty(),
            color = Color.White,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 38.dp).padding(horizontal = 24.dp)
        )


    }


}