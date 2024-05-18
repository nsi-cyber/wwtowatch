package navigation


import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import presentation.exploreScreen.ExploreScreen
import presentation.searchScreen.SearchScreen

@Composable
fun WWNavigator() {
    Navigator(ExploreScreen)
}