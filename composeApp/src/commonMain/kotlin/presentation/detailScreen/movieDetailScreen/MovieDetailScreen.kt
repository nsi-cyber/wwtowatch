package presentation.detailScreen.movieDetailScreen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Constants.IMAGE_URL
import data.Constants.IMAGE_URL_ORIGINAL
import data.Constants.IMAGE_URL_PROFILE
import data.model.creditsList.Cast
import data.model.creditsList.Crew
import data.model.imageList.ImageListResponseItem
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import presentation.components.shimmerEffect
import presentation.exploreScreen.MovieListContent
import primaryColor
import utils.formatDate


data class MovieDetailScreen(var movieId: Int?) : Screen {

    override val key: ScreenKey
        get() = "MovieDetailScreen"


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel: MovieDetailScreenModel = koinScreenModel()

        val state by screenModel.state.collectAsState()
        LaunchedEffect(Unit) {
            screenModel.getMovieDetail(movieId)
            screenModel.getMovieCredits(movieId)
            screenModel.getMovieImages(movieId)
            screenModel.getMovieSimilar(movieId)
            screenModel.getMovieProviders(movieId)
            screenModel.getMovieVideos(movieId)


        }

        when (state) {
            is MovieDetailState.Loading -> {}

            is MovieDetailState.Content -> {
                with((state as MovieDetailState.Content)) {

                    Column(
                        modifier = Modifier.background(primaryColor)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().aspectRatio(6 / 9f)
                        ) {
                            KamelImage(
                                animationSpec = tween(),
                                onLoading = {
                                    Box(
                                        modifier = Modifier.fillMaxSize().shimmerEffect(),
                                    )
                                },

                                resource = asyncPainterResource(data = "${IMAGE_URL}${data?.details?.poster_path}"),
                                contentDescription = data?.details?.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().drawWithCache {
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
                        }


                        Column(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp, end = 16.dp
                            )
                        ) {
                            Text(
                                text = "About Movie",
                                color = Color.Gray,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                lineHeight = 30.sp,
                                text = data?.details?.title ?: "",
                                color = Color.White,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = data?.details?.overview ?: "",
                                color = Color.White,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Column(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            )
                        ) {
                            Text(
                                text = "Release Date",
                                color = Color.Gray,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = data?.details?.release_date?.formatDate() ?: "",
                                color = Color.White,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )

                        }



                        Column(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            )
                        ) {
                            Text(
                                text = "Genre",
                                color = Color.Gray,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.fillMaxWidth()
                            )


                            var genreText = ""

                            data?.details?.genres?.forEach {
                                genreText += it.name + ", "
                            }

                            genreText = genreText.take(genreText.length - 2)


                            Text(
                                text = genreText,
                                color = Color.White,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )

                        }


                        data?.images?.let {
                            ImagesListContent(title = "Images",
                                list = data.images,
                                onItemClick = { id ->
                                    //    navigator.push(MovieDetailScreen(id))
                                })
                        }


                        data?.credits?.let {
                            CastListContent(it.cast)
                            CrewListContent(it.crew)
                        }


                        data?.similars?.let {
                            MovieListContent(title = "Similar", subtitle = "Movies", rowCount = 1,
                                list = data.similars,
                                onItemClick = { id ->
                                    navigator.push(MovieDetailScreen(id))
                                })
                        }

                    }


                }

            }
        }


    }

}


@Composable
fun ImagesListContent(
    title: String? = "Popular",
    list: List<ImageListResponseItem?>?, onItemClick: (ImageListResponseItem?) -> Unit
) {
    val lazyGridState = rememberLazyStaggeredGridState()
    val shimmerModifier: Modifier = Modifier.shimmerEffect()

    Column() {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {

            Text(
                text = title ?: "",
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyHorizontalStaggeredGrid(
            state = lazyGridState,
            rows = StaggeredGridCells.Fixed(1),
            modifier = Modifier.height(300.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalItemSpacing = 8.dp
        ) {
            if (list.isNullOrEmpty()) {
                items(9) {
                    ImageCard(
                        modifier = shimmerModifier
                    )
                }
            } else {
                items(list.size) { image ->
                    ImageCard(
                        image = list.get(image), onItemClick = onItemClick
                    )


                }
            }
        }
    }

}

@Composable
fun ImageCard(
    modifier: Modifier = Modifier,
    image: ImageListResponseItem? = null, onItemClick: (image: ImageListResponseItem?) -> Unit = {}
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp)).clip(RoundedCornerShape(10.dp)).fillMaxSize()
            .aspectRatio(((image?.aspect_ratio ?: 1.0)).toFloat() ?: (4 / 3f))


    ) {


        KamelImage(
            contentAlignment = Alignment.Center,
            animationSpec = tween(),
            onLoading = { Box(modifier = Modifier.fillMaxSize().shimmerEffect()) },
            resource = asyncPainterResource(data = "${IMAGE_URL_ORIGINAL}${image?.file_path}"),
            contentDescription = image?.file_path,
            modifier = Modifier.fillMaxSize().background(Color(0xFFB8B5B5))
                .clickable {
                    onItemClick(image)
                },
            contentScale = ContentScale.Fit,
        )


    }

}


@Composable
fun CrewListContent(
    list: List<Crew?>?
) {
    val lazyGridState = rememberLazyGridState()
    val shimmerModifier: Modifier = Modifier.shimmerEffect()

    Column() {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
            Text(
                text = "Crew",
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyHorizontalGrid(
            state = lazyGridState,
            rows = GridCells.Fixed(1),
            modifier = Modifier.height(200.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (list.isNullOrEmpty()) {
                items(9) {
                    CrewCard(
                        modifier = shimmerModifier
                    )
                }
            } else {
                items(list.sortedByDescending { it?.popularity }) { crew ->
                    CrewCard(
                        crew = crew
                    )


                }
            }
        }
    }

}

@Composable
fun CrewCard(
    modifier: Modifier = Modifier,
    crew: Crew? = null
) {

    Column(
        modifier = Modifier.width(140.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        KamelImage(
            animationSpec = tween(),
            onLoading = {
                Box(
                    modifier = Modifier.fillMaxSize().shimmerEffect(),
                )
            },

            resource = asyncPainterResource(data = "${IMAGE_URL_PROFILE}${crew?.profile_path}"),
            contentDescription = crew?.name,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(80.dp)
                .shadow(5.dp, RoundedCornerShape(99.dp)).clip(RoundedCornerShape(99.dp))
                .background(Color(0xFFB8B5B5))
                .aspectRatio(1f)
        )


        Text(
            text = crew?.name ?: "",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = crew?.job ?: "",
            color = Color.LightGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )


    }

}


@Composable
fun CastListContent(
    list: List<Cast?>?
) {
    val lazyGridState = rememberLazyGridState()
    val shimmerModifier: Modifier = Modifier.shimmerEffect()

    Column() {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)) {
            Text(
                text = "Cast",
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        }

        LazyHorizontalGrid(
            state = lazyGridState,
            rows = GridCells.Fixed(1),
            modifier = Modifier.height(200.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (list.isNullOrEmpty()) {
                items(9) {
                    CastCard(
                        modifier = shimmerModifier
                    )
                }
            } else {
                items(list.sortedByDescending { it?.popularity }) { cast ->
                    CastCard(
                        cast = cast
                    )


                }
            }
        }
    }

}

@Composable
fun CastCard(
    modifier: Modifier = Modifier,
    cast: Cast? = null
) {

    Column(
        modifier = Modifier.width(140.dp).padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        KamelImage(
            animationSpec = tween(),
            onLoading = {
                Box(
                    modifier = Modifier.fillMaxSize().shimmerEffect(),
                )
            },

            resource = asyncPainterResource(data = "${IMAGE_URL_PROFILE}${cast?.profile_path}"),
            contentDescription = cast?.name,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(80.dp)
                .shadow(5.dp, RoundedCornerShape(20.dp)).clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFB8B5B5))
                .aspectRatio(1f)
        )


        Text(
            text = cast?.name ?: "",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = cast?.character ?: "",
            color = Color.LightGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )


    }

}