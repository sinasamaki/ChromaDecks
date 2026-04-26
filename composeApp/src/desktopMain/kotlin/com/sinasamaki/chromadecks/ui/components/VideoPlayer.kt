package com.sinasamaki.chromadecks.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.sinasamaki.chromadecks.ui.theme.Zinc100
import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.scene.SnapshotParameters
import javafx.scene.image.WritableImage
import javafx.scene.layout.Background
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer as JFXMediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.paint.Paint
import kotlinx.coroutines.delay

private const val RESOURCE_PREFIX =
    "composeResources/chromadecks.composeapp.generated.resources/files/"

// Single-video player
@Composable
fun VideoPlayer(
    fileName: String,
    modifier: Modifier = Modifier
) {
    VideoPlayer(fileNames = listOf(fileName), modifier = modifier)
}

// Playlist player — pre-loads all JFXMediaPlayer instances once,
// cycles to the next video on end, loops back to the start of the list.
@Composable
fun VideoPlayer(
    fileNames: List<String>,
    modifier: Modifier = Modifier
) {
    var error by remember { mutableStateOf<String?>(null) }
    var aspectRatio by remember { mutableFloatStateOf(9f / 16f) }
    var currentFrame by remember { mutableStateOf<ImageBitmap?>(null) }

    // Initialize the JavaFX toolkit once; kept alive for the composition lifetime.
    remember { JFXPanel().also { Platform.setImplicitExit(false) } }

    DisposableEffect(fileNames) {
        var animationTimer: AnimationTimer? = null
        val players = mutableListOf<JFXMediaPlayer>()

        // Capture classloader on the composition thread before handing off to FX thread.
        val classLoader = Thread.currentThread().contextClassLoader

        Platform.runLater {
            // Pre-load all media players up front so switching is instant.
            fileNames.forEach { fileName ->
                val url = classLoader.getResource("$RESOURCE_PREFIX$fileName")
                if (url != null) {
                    players.add(JFXMediaPlayer(Media(url.toExternalForm())).apply {
                        cycleCount = 1 // Each video plays once; we handle cycling ourselves.
                    })
                }
            }

            if (players.isEmpty()) {
                error = "No videos found"
                return@runLater
            }

            // A hidden scene so MediaView nodes have a layout context for snapshot().
            val root = StackPane()
            root.background = Background.fill(Paint.valueOf("red"))
            JFXPanel().also {
                it.scene = Scene(root, 1.0, 1.0).apply {
                    fill = Paint.valueOf("red")
                }
                it.background = java.awt.Color.BLACK

            }

            val snapshotParams = SnapshotParameters()
            var writableImage: WritableImage? = null

            fun playAt(index: Int) {
                animationTimer?.stop()
                root.children.clear()

                val player = players[index]
                val mediaView = MediaView(player).apply { isPreserveRatio = true }
                root.children.add(mediaView)

                fun startCapture() {
                    val w = player.media.width.toInt()
                    val h = player.media.height.toInt()
                    if (h > 0) {
                        aspectRatio = w / h.toFloat()
                        mediaView.fitWidth = w.toDouble()
                        mediaView.fitHeight = h.toDouble()
                    }

                    player.setOnEndOfMedia {
                        // Reset player so it can be replayed when the playlist loops back.
                        player.seek(javafx.util.Duration.ZERO)
                        player.stop()
                        playAt((index + 1) % players.size)
                    }

                    animationTimer = object : AnimationTimer() {
                        override fun handle(now: Long) {
                            val w = mediaView.fitWidth.toInt()
                            val h = mediaView.fitHeight.toInt()
                            if (w <= 0 || h <= 0) return

                            // Reuse WritableImage unless dimensions changed.
                            if (writableImage == null ||
                                writableImage!!.width.toInt() != w ||
                                writableImage!!.height.toInt() != h
                            ) {
                                writableImage = WritableImage(w, h)
                            }

                            val snapshot = mediaView.snapshot(snapshotParams, writableImage)
                            val buffered = SwingFXUtils.fromFXImage(snapshot, null) ?: return
                            currentFrame = buffered.toComposeImageBitmap()
                        }
                    }.also { it.start() }

                    player.play()
                }

                // Media may already be loaded on subsequent loops.
                if (player.status == JFXMediaPlayer.Status.UNKNOWN) {
                    player.setOnReady { startCapture() }
                } else {
                    startCapture()
                }
            }

            playAt(0)
        }

        onDispose {
            Platform.runLater {
                animationTimer?.stop()
                players.forEach { p -> runCatching { p.stop(); p.dispose() } }
            }
        }
    }

    AnimatedVisibility(
        visible = currentFrame != null,
        modifier = modifier.aspectRatio(aspectRatio),
        enter = fadeIn(tween(durationMillis = 300, delayMillis = 200)),
        exit = fadeOut(),
    ) {
        currentFrame?.let { frame ->
            Image(
                bitmap = frame,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
