package navigation


import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideOrientation
import cafe.adriel.voyager.transitions.SlideTransition
import presentation.exploreScreen.ExploreScreen

@Composable
fun WWNavigator() {
    Navigator(ExploreScreen) { navigator ->
        SlideTransition(navigator = navigator, orientation = SlideOrientation.Horizontal)
    }
}




