package presentation.exploreScreen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Constants.IMAGE_URL
import data.model.CardViewData
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Job
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.components.AutoScrollerHorizontalPagerView
import presentation.components.shimmerEffect
import presentation.detailScreen.movieDetailScreen.MovieDetailScreen
import presentation.searchScreen.SearchScreen
import primaryColor
import ww_to_watch.composeapp.generated.resources.Res
import ww_to_watch.composeapp.generated.resources.ic_search


data object ExploreScreen : Screen {

    override val key: ScreenKey
        get() = "ExploreScreen"


    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel: ExploreScreenModel = getScreenModel()

        val state by screenModel.state.collectAsState()
        LaunchedEffect(Unit) {
            screenModel.getPopularMovies()
            screenModel.getTopRatedShows()
            screenModel.getTrending()
            screenModel.getTopMovies()

        }

        when (state) {
            is ListState.Loading -> {}

            is ListState.Content -> {

                Column(
                    modifier = Modifier.background(primaryColor).verticalScroll(rememberScrollState())

                ) {


                    (state as ListState.Content).data?.trendingAll?.let { trending ->

                        Box(modifier = Modifier.padding(bottom = 24.dp)) {
                            AutoScrollerHorizontalPagerView(list = trending) {
                                if (it.media_type == "movie")
                                    navigator.push(MovieDetailScreen(it.id))
                            }

                            Box(contentAlignment = Alignment.Center,
                                modifier = Modifier.height(50.dp).fillMaxWidth()
                                    .padding(horizontal = 24.dp).shadow(4.dp)
                                    .clip(RoundedCornerShape(8.dp)).background(Color.White).align(
                                        Alignment.BottomCenter
                                    ).clickable {
                                        navigator.push(SearchScreen)
                                    }) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Discover Movies & Shows...",
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Start,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.wrapContentSize()
                                    )

                                    Image(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(Res.drawable.ic_search),
                                        contentDescription = "Search Icon",
                                    )
                                }

                            }
                        }
                    }




                    if ((state as ListState.Content).data?.popularMovies == null || (state as ListState.Content).data?.popularMovies?.isNotEmpty() == true) {

                        MovieListContent(
                            list = (state as ListState.Content).data?.popularMovies,
                            onItemClick = { id ->
                                navigator.push(MovieDetailScreen(id))
                            },
                            pagination = {
                                screenModel.getPopularMovies()
                            })

                    }

                    if ((state as ListState.Content).data?.topRatedShows == null || (state as ListState.Content).data?.topRatedShows?.isNotEmpty() == true) {


                        RankedListContent(
                            title = "Top 10 Shows",
                            subtitle = "This Week",
                            list = (state as ListState.Content).data?.topRatedShows,
                            onItemClick = { id ->
                                // navigator.push(DetailScreen(id))
                            })


                    }

                    if ((state as ListState.Content).data?.topRatedMovies == null || (state as ListState.Content).data?.topRatedMovies?.isNotEmpty() == true) {


                        RankedListContent(
                            title = "Top 10 Movies",
                            subtitle = "of All Time",
                            list = (state as ListState.Content).data?.topRatedMovies,
                            onItemClick = { id ->
                                navigator.push(MovieDetailScreen(id))
                            })


                    }


                }
            }


            else -> {}
        }
    }
}

@Composable
fun MovieListContent(
    title: String? = "Popular", subtitle: String? = "Movies", rowCount: Int = 2,
    list: List<CardViewData?>?, onItemClick: (Int) -> Unit, pagination: () -> Job? = { null }
) {
    val lazyGridState = rememberLazyGridState()
    val shimmerModifier: Modifier = Modifier.shimmerEffect()

    Column() {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
            Text(
                text = title ?: "",
                color = Color.Gray,
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = subtitle ?: "",
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyHorizontalGrid(
            state = lazyGridState,
            rows = GridCells.Fixed(rowCount),
            modifier = Modifier.height(if (rowCount == 2) 400.dp else 200.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (list.isNullOrEmpty()) {
                items(9) {
                    MovieCard(
                        modifier = shimmerModifier
                    )
                }
            } else {
                items(list) { movie ->
                    MovieCard(
                        movie = movie, onItemClick = onItemClick
                    )
                    lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index?.let {
                        if (it + 10 > (list.size)) {
                            LaunchedEffect(Unit) {
                                pagination()
                            }
                        }
                    }

                }
            }
        }
    }

}


@Composable
private fun RankedListContent(
    title: String?, subtitle: String?,
    list: List<CardViewData?>?, onItemClick: (Int) -> Unit
) {
    val shimmerModifier: Modifier = Modifier.shimmerEffect()

    val lazyGridState = rememberLazyGridState()
    Column(
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
            Text(
                text = title.orEmpty(),
                color = Color.Gray,
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = subtitle.orEmpty(),
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyHorizontalGrid(
            state = lazyGridState,
            rows = GridCells.Fixed(3),
            modifier = Modifier.height(425.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {


            if (list.isNullOrEmpty()) {
                items(10) {
                    RankedCard(
                        modifier = shimmerModifier,
                    )
                }

            } else {
                itemsIndexed(items = list.take(10)) { position, data ->
                    RankedCard(
                        position = position + 1, data = data, onItemClick = onItemClick
                    )/*
                lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index?.let {
                    if (it + 10 > (list.size)) {
                        LaunchedEffect(Unit) {
                            pagination()
                        }
                    }
                }*/

                }
            }
        }
    }

}


@Composable
fun RankedCard(
    modifier: Modifier = Modifier,
    position: Int? = null, data: CardViewData? = null, onItemClick: (Int) -> Unit = {}
) {

    Row(modifier = modifier.width(330.dp)

        .clickable {
            onItemClick(data?.id ?: -1)
        }


    ) {


        KamelImage(
            animationSpec = tween(),
            onLoading = {
                Box(
                    modifier = Modifier.fillMaxSize().shimmerEffect(),
                )
            },

            resource = asyncPainterResource(data = "${IMAGE_URL}${data?.poster_path}"),
            contentDescription = data?.title,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(width = 100.dp, height = 125.dp)
                .aspectRatio(4 / 5f)
                .shadow(5.dp, RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))


        )
        Column(
            modifier = Modifier.height(125.dp).padding(start = 8.dp).padding(vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "$position.",
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = modifier,
            )

            Text(
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                text = data?.title.orEmpty(),
                color = Color.White,
                fontSize = 25.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                modifier = modifier.width(190.dp)
            )

            Text(
                text = data?.date?.take(4).orEmpty(),
                color = Color.Gray,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                modifier = modifier,
            )

        }

    }

}


@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: CardViewData? = null, onItemClick: (Int) -> Unit = {}
) {

    Box(contentAlignment = Alignment.Center,
        modifier = modifier.shadow(5.dp, RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))
            .aspectRatio(4 / 5f).clickable {
                onItemClick(movie?.id ?: -1)
            }


    ) {


        KamelImage(animationSpec = tween(),
            onLoading = {
                Box(
                    modifier = Modifier.fillMaxSize().shimmerEffect(),
                )
            },

            resource = asyncPainterResource(data = "${IMAGE_URL}${movie?.poster_path}"),
            contentDescription = movie?.title,
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize().background(Color(0xFFB8B5B5)).drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, primaryColor),
                    startY = size.height / 2,
                    endY = size.height
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient)
                }
            })

        Text(
            text = movie?.title.orEmpty(),
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
        )


    }

}
