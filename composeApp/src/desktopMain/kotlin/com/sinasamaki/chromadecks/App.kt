import com.sinasamaki.chromadecks._001_MeshGradients.MeshGradientPresentation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sinasamaki.chromadecks.ui.theme.Black
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.sinasamaki.chromadecks.ui.theme.ChromaTheme


@Composable
@Preview
fun App() {
    ChromaTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Black),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                Modifier
                    .padding(16.dp)
                    .aspectRatio(4 / 3f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                MeshGradientPresentation()
            }
        }
    }
}