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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import data.Constants.IMAGE_URL
import data.model.CardViewData
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.Job
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.components.AutoScrollerHorizontalPagerView
import presentation.components.shimmerEffect
import presentation.searchScreen.SearchScreen
import ww_to_watch.composeapp.generated.resources.Res
import ww_to_watch.composeapp.generated.resources.ic_search


data object ExploreScreen : Screen {
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
            ListState.Loading, ListState.Empty, is ListState.ShowError -> Unit

            is ListState.Content -> {

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                        .background(Color.White)
                ) {
                    (state as ListState.Content).trendingAll?.let { trending ->

                        Box(modifier = Modifier.padding(bottom = 24.dp)) {
                            AutoScrollerHorizontalPagerView(list = trending) {
                                //detail
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
                                        text = "Search for Movies & Shows...",
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



                    (state as ListState.Content).popularMovies?.let { movieList ->
                        MovieListContent(list = movieList, onItemClick = { id ->
                            // navigator.push(DetailScreen(id))
                        }, pagination = {
                            screenModel.getPopularMovies()
                        })

                    }


                    (state as ListState.Content).topRatedShows?.let { showList ->

                        RankedListContent(
                            title = "Top 10 Shows",
                            subtitle = "This Week",
                            list = showList,
                            onItemClick = { id ->
                                // navigator.push(DetailScreen(id))
                            })


                    }


                    (state as ListState.Content).topRatedMovies?.let { showList ->

                        RankedListContent(
                            title = "Top 10 Movies",
                            subtitle = "of All Time",
                            list = showList,
                            onItemClick = { id ->
                                // navigator.push(DetailScreen(id))
                            })


                    }

                }
            }


            else -> {}
        }
    }
}

@Composable
private fun MovieListContent(
    list: List<CardViewData?>?, onItemClick: (Int) -> Unit, pagination: () -> Job?
) {
    val lazyGridState = rememberLazyGridState()
    Column() {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
            Text(
                text = "Popular",
                color = Color.Gray,
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Movies",
                color = Color.Black,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyHorizontalGrid(
            state = lazyGridState,
            rows = GridCells.Fixed(2),
            modifier = Modifier.height(400.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            items(list!!) { movie ->
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


@Composable
private fun RankedListContent(
    title: String?, subtitle: String?,
    list: List<CardViewData?>?, onItemClick: (Int) -> Unit
) {
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
                color = Color.Black,
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

            itemsIndexed(items = list!!.take(10)) { position, data ->
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


@Composable
fun RankedCard(
    position: Int, data: CardViewData?, onItemClick: (Int) -> Unit
) {

    Row(modifier = Modifier.width(330.dp)

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
            modifier = Modifier.size(width = 100.dp, height = 125.dp).background(Color.White)
                .aspectRatio(4 / 5f)
                .shadow(5.dp, RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))


        )
        Column(
            modifier = Modifier.height(125.dp).padding(start = 8.dp).padding(vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "$position.",
                color = Color.Black,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
            )

            Text(
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                text = data?.title.orEmpty(),
                color = Color.Black,
                fontSize = 25.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(190.dp)
            )

            Text(
                text = data?.date?.take(4).orEmpty(),
                color = Color.Gray,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
            )

        }

    }

}


@Composable
fun MovieCard(
    movie: CardViewData?, onItemClick: (Int) -> Unit
) {

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.shadow(5.dp, RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp))
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
            modifier = Modifier.fillMaxSize().background(Color(0xFFB8B5B5)).drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = size.height / 2,
                    endY = size.height
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.Multiply)
                }
            })

        Text(
            text = movie?.title.orEmpty(),
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
        )


    }

}
