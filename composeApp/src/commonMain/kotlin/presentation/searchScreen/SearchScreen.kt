package presentation.searchScreen

import SearchScreenModel
import SearchState
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Constants
import data.model.CardViewData
import data.model.genreKeywordList.Genre
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.components.shimmerEffect
import presentation.detailScreen.movieDetailScreen.MovieDetailScreen
import primaryColor
import secondaryColor
import ww_to_watch.composeapp.generated.resources.Res
import ww_to_watch.composeapp.generated.resources.ic_cross
import ww_to_watch.composeapp.generated.resources.ic_search
import ww_to_watch.composeapp.generated.resources.thumb_movie
import ww_to_watch.composeapp.generated.resources.thumb_tv

data object SearchScreen : Screen {

    override val key: ScreenKey
        get() = "SearchScreen"


    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel: SearchScreenModel = koinScreenModel()
        val textState = remember { mutableStateOf(TextFieldValue()) }

        val state by screenModel.state.collectAsState()
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(state) {
            if (state is SearchState.Empty) {
                focusRequester.requestFocus()
            }
        }


        LaunchedEffect(textState.value.text) {
            if (textState.value.text.length >= 3) {
                screenModel.searchWithDebounce(textState.value.text)
            } else if (textState.value.text.isEmpty()) {
                screenModel.setStateEmpty()
            }


        }

        LaunchedEffect(Unit) {
            screenModel.getMovieGenreList()
            screenModel.getTvGenreList()
        }

        when (state) {
            is SearchState.Loading -> Unit

            is SearchState.Content -> {
                Column(
                    modifier = Modifier
                        .background(primaryColor).fillMaxSize().padding(top = 16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                            .padding(horizontal = 24.dp).shadow(4.dp)
                            .clip(RoundedCornerShape(8.dp)).background(Color.White)
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            TextField(
                                maxLines = 1,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    backgroundColor = Color.White,
                                    cursorColor = Color.Black,
                                    focusedIndicatorColor = Color.White,
                                    disabledIndicatorColor = Color.White,
                                    unfocusedIndicatorColor = Color.White
                                ),
                                modifier = Modifier.wrapContentSize()
                                    .focusRequester(focusRequester).background(color = Color.White),
                                value = textState.value,
                                onValueChange = { value ->
                                    textState.value = value
                                },
                            )


                            Image(
                                modifier = Modifier.size(24.dp).clickable {
                                    textState.value =
                                        TextFieldValue()
                                },
                                painter = painterResource(Res.drawable.ic_cross),
                                contentDescription = "Search Icon",
                            )
                        }

                    }
                    if (state is SearchState.Content) {
                        (state as SearchState.Content).data?.searchResult?.let { searchList ->

                            LazyColumn(contentPadding = PaddingValues(start = 16.dp)) {
                                if (searchList.isEmpty()) {
                                    item {
                                        SearchCard(
                                            data = CardViewData(
                                                title = "Not Found",
                                            )
                                        )
                                    }
                                }
                                items(searchList) {
                                    SearchCard(data = it) {
                                        if (it?.media_type == "movie")
                                            navigator.push(MovieDetailScreen(it.id))
                                    }
                                }
                            }


                        }
                    }
                }

            }

            is SearchState.Empty -> {

                Column(
                    modifier = Modifier.fillMaxSize().background(primaryColor)
                        .verticalScroll(rememberScrollState())
                        .padding(top = 16.dp)
                ) {

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 24.dp).shadow(4.dp)
                            .clip(RoundedCornerShape(8.dp)).background(Color.White)
                    )
                    {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            TextField(
                                placeholder = {
                                    Text(
                                        text = "Discover Movies & Shows...",
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Start,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.wrapContentSize()
                                    )
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    backgroundColor = Color.White,
                                    cursorColor = Color.Black,
                                    focusedIndicatorColor = Color.White,
                                    disabledIndicatorColor = Color.White,
                                    unfocusedIndicatorColor = Color.White
                                ),
                                modifier = Modifier.wrapContentSize()
                                    .focusRequester(focusRequester)
                                    .background(color = Color.White),
                                value = textState.value,
                                onValueChange = { value ->
                                    textState.value = value
                                },
                            )


                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(Res.drawable.ic_search),
                                contentDescription = "Search Icon",
                            )
                        }

                    }



                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            .padding(top = 32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                                .shadow(5.dp, RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .aspectRatio(4 / 5f).clickable {

                                }


                        ) {


                            Image(
                                painter = painterResource(Res.drawable.thumb_movie),
                                contentDescription = "movie?.title",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().background(Color(0xFFB8B5B5))
                                    .drawWithCache {
                                        val gradient = Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, primaryColor),
                                            startY = size.height / 3,
                                            endY = size.height
                                        )
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(gradient)
                                        }
                                    })

                            Text(
                                text = "Movies",
                                color = Color.White,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp)
                            )


                        }

                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                                .shadow(5.dp, RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .aspectRatio(4 / 5f).clickable {

                                }


                        ) {


                            Image(
                                painter = painterResource(Res.drawable.thumb_tv),
                                contentDescription = "movie?.title",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().background(Color(0xFFB8B5B5))
                                    .drawWithCache {
                                        val gradient = Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, primaryColor),
                                            startY = size.height / 3,
                                            endY = size.height
                                        )
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(gradient)
                                        }
                                    })

                            Text(
                                text = "Tv-Series",
                                color = Color.White,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(Alignment.BottomCenter)
                                    .padding(bottom = 8.dp)
                            )


                        }
                    }


                    if ((state as SearchState.Empty).data?.movieGenreList == null ||
                        (state as SearchState.Empty).data?.movieGenreList?.isNotEmpty() == true
                    ) {
                        Column() {
                            Column(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 16.dp,
                                    bottom = 8.dp
                                )
                            ) {
                                Text(
                                    text = "Genres of",
                                    color = Color.Gray,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "Movies",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            HorizontalKeywordContent(data = (state as SearchState.Empty).data?.movieGenreList) {}

                        }
                    }

                    if ((state as SearchState.Empty).data?.tvGenreList == null ||
                        (state as SearchState.Empty).data?.tvGenreList?.isNotEmpty() == true
                    ) {
                        Column() {
                            Column(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 16.dp,
                                    bottom = 8.dp
                                )
                            ) {
                                Text(
                                    text = "Genres of",
                                    color = Color.Gray,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "Tv Series",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            HorizontalKeywordContent(data = (state as SearchState.Empty).data?.tvGenreList) {}

                        }
                    }


                }

            }


            else -> {

            }
        }


    }

}


@Composable
fun SearchCard(
    data: CardViewData?, onItemClick: (data: CardViewData?) -> Unit = {}
) {

    Row(
        modifier = Modifier.height(100.dp).fillMaxWidth().clickable {
            onItemClick(data)
        }.padding(bottom = 16.dp)


    ) {


        KamelImage(
            animationSpec = tween(),
            onLoading = {
                Box(
                    modifier = Modifier.fillMaxSize().shimmerEffect(),
                )
            },
            resource = asyncPainterResource(data = "${Constants.IMAGE_URL}${data?.poster_path}"),
            contentDescription = data?.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(4 / 5f).shadow(5.dp, RoundedCornerShape(10.dp)).clip(
                    RoundedCornerShape(10.dp)
                ).background(Color.Gray)

        )
        Column(
            modifier = Modifier.fillMaxHeight().padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = data?.title.orEmpty() + " (" + data?.date?.take(4) + ")",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = if (data?.media_type == "movie") "Movie" else "Tv Show",
                color = Color.Gray,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )
        }


    }

}


@Composable
fun HorizontalKeywordContent(data: List<Genre?>?, onClick: (id: Int?) -> Unit) {
    LazyHorizontalStaggeredGrid(
        modifier = Modifier.height(86.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
        rows = StaggeredGridCells
            .Fixed(2) // number of columns
    ) {
        if (data.isNullOrEmpty()) {
            items(9) {
                KeywordCardItem(modifier = Modifier.shimmerEffect())

            }
        }
        items(
            data?.size ?: 0
        ) { item ->

            KeywordCardItem(data = data?.get(item)) { id -> onClick(id) }

        }
    }

}

@Composable
fun KeywordCardItem(
    modifier: Modifier = Modifier,
    data: Genre? = null,
    onClick: (id: Int?) -> Unit = {}
) {
    Card(
        backgroundColor = secondaryColor,
        modifier = modifier.clip(RoundedCornerShape(50.dp)).clickable { onClick(data?.id) }
            .padding(4.dp),
        shape = RoundedCornerShape(50.dp),
        elevation = 4.dp,
    ) {
        Text(
            text = data?.name ?: "------------",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White
        )
    }
}
