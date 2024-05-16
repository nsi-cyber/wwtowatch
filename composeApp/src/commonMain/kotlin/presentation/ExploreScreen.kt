package presentation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.unit.sp
import data.Constants.IMAGE_URL
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.jetbrains.compose.resources.painterResource
import presentation.components.shimmerEffect


data object ExploreScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel: ExploreScreenModel = getScreenModel()

        val state by screenModel.state.collectAsState()
        LaunchedEffect(Unit) {
            screenModel.getPopularMovies()
            screenModel.getTopRatedShows()

        }

        when (state) {
            ListState.Loading,
            ListState.Empty,
            is ListState.ShowError -> Unit

            is ListState.Content -> {

                LazyColumn {
                    item {
                        (state as ListState.Content).popularMovies?.let { movieList ->
                            MovieListContent(
                                list = movieList,
                                onCharacterClick = { id ->
                                    // navigator.push(DetailScreen(id))
                                }, pagination = {
                                    screenModel.getPopularMovies()
                                }
                            )

                        }
                    }
                    item {
                        (state as ListState.Content).topRatedShows?.let { showList ->
                            ShowListContent(
                                list = showList,
                                onCharacterClick = { id ->
                                    // navigator.push(DetailScreen(id))
                                }, pagination = {
                                    screenModel.getTopRatedShows()
                                }
                            )

                        }
                    }


                }
            }
        }
    }

    @Composable
    private fun MovieListContent(
        list: List<data.model.popularMoviesList.Result?>?,
        onCharacterClick: (Int) -> Unit,
        pagination: () -> Job?
    ) {
        val lazyGridState = rememberLazyGridState()
        Column {
            Text(
                text = "Popular Movies",
                color = Color.Black,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp).fillMaxWidth()
            )
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
                        movie = movie,
                        onClick = onCharacterClick
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
    private fun ShowListContent(
        list: List<data.model.topRatedShowsList.Result?>?,
        onCharacterClick: (Int) -> Unit,
        pagination: () -> Job?
    ) {
        val lazyGridState = rememberLazyGridState()
        Column(
            modifier = Modifier.fillMaxHeight() // Bu satır eklendi
        ) {
            Text(
                text = "Top Rated Shows",
                color = Color.Black,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp).fillMaxWidth()
            )
            LazyVerticalGrid(
                userScrollEnabled = false,
                state = lazyGridState,
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                items(list!!) { show ->
                    ShowCard(
                        show = show,
                        onClick = onCharacterClick
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
fun ShowCard(
    show: data.model.topRatedShowsList.Result?,
    onClick: (Int) -> Unit
) {

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(6 / 9f)
            .clickable {
                onClick(show?.id ?: -1)
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

            resource = asyncPainterResource(data = "${IMAGE_URL}${show?.poster_path}"),
            contentDescription = show?.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize().background(Color(0xFFB8B5B5))
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = size.height / 2,
                        endY = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient, blendMode = BlendMode.Multiply)
                    }
                }
        )

        Text(
            text = show?.name.orEmpty(),
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )


    }

}


@Composable
fun MovieCard(
    movie: data.model.popularMoviesList.Result?,
    onClick: (Int) -> Unit
) {

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(6 / 9f)
            .clickable {
                onClick(movie?.id ?: -1)
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

            resource = asyncPainterResource(data = "${IMAGE_URL}${movie?.poster_path}"),
            contentDescription = movie?.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize().background(Color(0xFFB8B5B5))
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = size.height / 2,
                        endY = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient, blendMode = BlendMode.Multiply)
                    }
                }
        )

        Text(
            text = movie?.title.orEmpty(),
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )


    }

}
