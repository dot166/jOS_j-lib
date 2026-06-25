package io.github.dot166.jlib.compose

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import coil.compose.AsyncImage
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.dot166.jlib.R
import io.github.dot166.jlib.compose.model.MediaViewModel
import io.github.dot166.jlib.utils.DateUtils.formatTime

@Composable
fun MediaBottomSheet(viewModel: MediaViewModel, context: Context) {
    val controller = viewModel.controller

    LaunchedEffect(Unit) {
        viewModel.connectController(context)
        viewModel.pollPosition()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val art = viewModel.mediaMetadata.artworkData
            ?: viewModel.mediaMetadata.artworkUri
            ?: R.drawable.def_art
        AsyncImage(
            model = art,
            contentDescription = stringResource(R.string.cover_art),
            contentScale = ContentScale.Crop,
            placeholder = rememberDrawablePainter(
                AppCompatResources.getDrawable(
                    LocalContext.current,
                    R.drawable.def_art
                )
            ),
            error = rememberDrawablePainter(
                AppCompatResources.getDrawable(
                    LocalContext.current,
                    R.drawable.def_art
                )
            ),
            modifier = Modifier
                .size(300.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                        3.dp
                    )
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = viewModel.mediaMetadata.title?.toString()
                ?: stringResource(
                    R.string.unknown
                ),
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(iterations = Int.MAX_VALUE)
                .padding(horizontal = 24.dp)
        )
        Text(
            text = viewModel.mediaMetadata.artist?.toString()
                ?: stringResource(
                    R.string.unknown
                ), style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(iterations = Int.MAX_VALUE)
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Slider(
            value = viewModel.currentPosition.toFloat(),
            valueRange = 0f..viewModel.duration.toFloat().coerceAtLeast(1f),
            onValueChange = { controller?.seekTo(it.toLong()) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatTime(viewModel.currentPosition))
            Text(formatTime(viewModel.duration))
        }

        if (viewModel.isRss) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { controller?.seekBack() }) {
                    Icon(
                        painterResource(androidx.media3.session.R.drawable.media3_icon_rewind),
                        stringResource(R.string.rewind)
                    )
                }

                FilledIconButton(
                    onClick = {
                        if (viewModel.isPlaying) controller?.pause() else controller?.play()
                    },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        if (viewModel.isPlaying) painterResource(androidx.media3.session.R.drawable.media3_icon_pause) else painterResource(
                            androidx.media3.session.R.drawable.media3_icon_play
                        ),
                        stringResource(R.string.play_pause)
                    )
                }

                IconButton(onClick = { controller?.seekForward() }) {
                    Icon(
                        painterResource(androidx.media3.session.R.drawable.media3_icon_fast_forward),
                        stringResource(R.string.fast_forward)
                    )
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val isRepeatActive = (controller?.repeatMode ?: Player.REPEAT_MODE_OFF) != Player.REPEAT_MODE_OFF
                val repeatIconRes = when (controller?.repeatMode ?: Player.REPEAT_MODE_OFF) {
                    Player.REPEAT_MODE_OFF -> androidx.media3.session.R.drawable.media3_icon_repeat_off
                    Player.REPEAT_MODE_ALL -> androidx.media3.session.R.drawable.media3_icon_repeat_all
                    Player.REPEAT_MODE_ONE -> androidx.media3.session.R.drawable.media3_icon_repeat_one
                    else -> androidx.media3.session.R.drawable.media3_icon_repeat_off
                }

                IconToggleButton(
                    checked = isRepeatActive,
                    onCheckedChange = { viewModel.toggleRepeatMode() },
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        checkedContentColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        painter = painterResource(id = repeatIconRes),
                        contentDescription = stringResource(R.string.repeat)
                    )
                }

                IconButton(onClick = { controller?.seekToPrevious() }) {
                    Icon(
                        painterResource(androidx.media3.session.R.drawable.media3_icon_previous),
                        stringResource(R.string.skip_previous)
                    )
                }

                FilledIconButton(
                    onClick = { if (viewModel.isPlaying) controller?.pause() else controller?.play() },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        if (viewModel.isPlaying) painterResource(androidx.media3.session.R.drawable.media3_icon_pause) else painterResource(
                            androidx.media3.session.R.drawable.media3_icon_play
                        ),
                        stringResource(R.string.play_pause)
                    )
                }

                IconButton(onClick = { controller?.seekToNext() }) {
                    Icon(
                        painterResource(androidx.media3.session.R.drawable.media3_icon_next),
                        stringResource(R.string.skip_next)
                    )
                }

                val shuffleIconRes =
                    if (controller?.shuffleModeEnabled ?: false) {
                        androidx.media3.session.R.drawable.media3_icon_shuffle_on
                    } else {
                        androidx.media3.session.R.drawable.media3_icon_shuffle_off
                    }

                IconToggleButton(
                    checked = controller?.shuffleModeEnabled ?: false,
                    onCheckedChange = { viewModel.toggleShuffleMode() },
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        checkedContentColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        painter = painterResource(id = shuffleIconRes),
                        contentDescription = stringResource(R.string.shuffle)
                    )
                }
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun MediaBottomSheetScaffold(
    viewModel: MediaViewModel,
    context: Context,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    sheetShape: Shape = BottomSheetDefaults.ExpandedShape,
    sheetContainerColor: Color = BottomSheetDefaults.ContainerColor,
    sheetContentColor: Color = contentColorFor(sheetContainerColor),
    sheetTonalElevation: Dp = 0.dp,
    sheetShadowElevation: Dp = BottomSheetDefaults.Elevation,
    sheetSwipeEnabled: Boolean = true,
    topBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit,
) {
    BottomSheetScaffold(
        sheetContent = { MediaBottomSheet(viewModel, context) },
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = BottomSheetDefaults.SheetPeekHeight,
        sheetMaxWidth = sheetMaxWidth,
        sheetShape = sheetShape,
        sheetContainerColor = sheetContainerColor,
        sheetContentColor = sheetContentColor,
        sheetTonalElevation = sheetTonalElevation,
        sheetShadowElevation = sheetShadowElevation,
        sheetDragHandle = { BottomSheetDefaults.DragHandle() },
        sheetSwipeEnabled = sheetSwipeEnabled,
        topBar = topBar,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content
    )
}