import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._002_PathAnimations.PathAnimationPresentation
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    ChromaContainer {
//                MeshGradientPresentation()
        PathAnimationPresentation()
    }
}