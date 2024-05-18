package presentation.searchScreen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Constants
import data.model.CardViewData
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.components.AutoScrollerHorizontalPagerView
import presentation.components.shimmerEffect
import presentation.exploreScreen.ExploreScreenModel
import presentation.exploreScreen.ListState
import ww_to_watch.composeapp.generated.resources.Res
import ww_to_watch.composeapp.generated.resources.ic_search

data object SearchScreen : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel: SearchScreenModel = getScreenModel()
        val textState = remember { mutableStateOf(TextFieldValue()) }

        val state by screenModel.state.collectAsState()
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(state) {
            if((state is SearchState.ShowError)==false)
                focusRequester.requestFocus()
        }

        LaunchedEffect(textState.value.text) {
            if (textState.value.text.length >= 3) {
                screenModel.searchWithDebounce(textState.value.text)
            } else if (textState.value.text.length == 0) {
                screenModel.setStateEmpty()
            }

        }

        when (state) {
            SearchState.Loading, is SearchState.ShowError -> Unit

            is SearchState.Content
            -> {
                Column(
                    modifier = Modifier
                        .background(Color.White).padding(top = 16.dp)
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
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(Res.drawable.ic_search),
                                contentDescription = "Search Icon",
                            )
                        }

                    }
                    if (state is SearchState.Content) {
                        (state as SearchState.Content).searchResult?.let { searchList ->

                            LazyColumn(contentPadding = PaddingValues(start = 16.dp)) {
                                items(searchList) {
                                    SearchCard(data = it) {

                                    }
                                }
                            }


                        }
                    }
                }

            }

            is SearchState.Empty -> {
                Column(
                    modifier = Modifier
                        .background(Color.White).padding(top = 16.dp)
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
                                placeholder = {
                                    Text(
                                        text = "Search for Movies & Shows...",
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
                                    .focusRequester(focusRequester).background(color = Color.White),
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
                }

            }


            else -> {}
        }


    }

}


@Composable
fun SearchCard(
    data: CardViewData?, onItemClick: (Int) -> Unit
) {

    Row(
        modifier = Modifier.height(100.dp).fillMaxWidth().clickable {
            onItemClick(data?.id ?: -1)
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
            modifier = Modifier.background(Color.White)
                .aspectRatio(4 / 5f).shadow(5.dp, RoundedCornerShape(10.dp)).clip(
                    RoundedCornerShape(10.dp)
                )

        )
        Column(
            modifier = Modifier.fillMaxHeight().padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = data?.title.orEmpty() + " (" + data?.date?.take(4) + ")",
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = if (data?.media_type == "movie") "Movie" else "Tv Show",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )
        }


    }

}
