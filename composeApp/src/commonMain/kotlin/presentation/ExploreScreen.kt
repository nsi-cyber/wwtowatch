package presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import data.Constants.IMAGE_URL
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.painterResource


data object ExploreScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel: ExploreScreenModel = getScreenModel()

        val state by screenModel.state.collectAsState()

        screenModel.getMovies()

        when (state) {
            ListState.Loading,
            ListState.Empty,
            is ListState.ShowError -> Unit

            is ListState.Content -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {


                    MovieListContent(
                        list = (state as ListState.Content).popularMovies,
                        onCharacterClick = { id ->
                           // navigator.push(DetailScreen(id))
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun MovieListContent(
        list: List<data.model.popularMoviesList.Result?>?,
        onCharacterClick: (Int) -> Unit
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(200.dp)
        ) {
            items(list!!) {
                MovieItem(
                    movie = it,
                    onClick = onCharacterClick
                )
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: data.model.popularMoviesList.Result?,
    onClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick(movie?.id ?: 1)
            }
    ) {
        KamelImage(
            resource = asyncPainterResource(data = "${IMAGE_URL}${movie?.poster_path}"),
            contentDescription = "Character Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3 / 4f),
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp),
            text = movie?.title.orEmpty(),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
}