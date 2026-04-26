import androidx.compose.runtime.Composable
import com.sinasamaki.chromadecks._003_ChromaDial.ChromaDialPresentation
import com.sinasamaki.chromadecks.ui.theme.ChromaContainer
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    ChromaContainer {
//                MeshGradientPresentation()
//        PathAnimationPresentation()
        ChromaDialPresentation()
    }
}