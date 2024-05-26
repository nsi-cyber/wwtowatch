import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import navigation.WWNavigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview


val primaryColor: Color = Color(0xFF0A071D)
val secondaryColor: Color = Color(0xFF2C2654)
@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme(colors = MaterialTheme.colors.copy(primary = primaryColor, background = primaryColor),content ={
        WWNavigator()
    } )
}

