package lib.tpstudio.swipereveal

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeRevealItem(
    modifier: Modifier = Modifier,
    contentRadius: Dp = 0.dp,
    startContent: @Composable (RowScope.() -> Unit)? = null,
    startContentWidth: Dp = 0.dp,
    endContent: @Composable (RowScope.() -> Unit)? = null,
    endContentWidth: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val maxOffset = with(density) { (if (startContent != null) startContentWidth else 0.dp).toPx()}
    val minOffset = -with(density) { (if (endContent != null) endContentWidth else 0.dp).toPx() }

    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(contentRadius))
            .clipToBounds()
    ) {

        if (offsetX.value > 0 && startContent != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(startContentWidth),
                verticalAlignment = Alignment.CenterVertically,
                content = startContent
            )
        }

        if (offsetX.value < 0 && endContent != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(endContentWidth),
                verticalAlignment = Alignment.CenterVertically,
                content = endContent
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                when {
                                    offsetX.value > maxOffset / 2 ->
                                        offsetX.animateTo(maxOffset)

                                    offsetX.value < minOffset / 2 ->
                                        offsetX.animateTo(minOffset)

                                    else ->
                                        offsetX.animateTo(0f)
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo(
                                (offsetX.value + dragAmount)
                                    .coerceIn(minOffset, maxOffset)
                            )
                        }
                    }
                },
            content = content
        )
    }
}

@Composable
fun <T> SwipeRevealItem(
    modifier: Modifier = Modifier,
    itemKey: T,
    openedItemKey: T?,
    onOpen: (T) -> Unit,
    onClose: () -> Unit,
    contentRadius: Dp = 0.dp,
    startContent: @Composable (RowScope.() -> Unit)? = null,
    startContentWidth: Dp = 0.dp,
    endContent: @Composable (RowScope.() -> Unit)? = null,
    endContentWidth: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val maxOffset = with(density) {
        (if (startContent != null) startContentWidth else 0.dp).toPx()
    }
    val minOffset = -with(density) {
        (if (endContent != null) endContentWidth else 0.dp).toPx()
    }

    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(openedItemKey) {
        if (openedItemKey != itemKey && offsetX.value != 0f) {
            offsetX.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(contentRadius))
            .clipToBounds()
    ) {

        if (offsetX.value > 0 && startContent != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(startContentWidth),
                verticalAlignment = Alignment.CenterVertically,
                content = startContent
            )
        }

        if (offsetX.value < 0 && endContent != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(endContentWidth),
                verticalAlignment = Alignment.CenterVertically,
                content = endContent
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                when {
                                    offsetX.value > maxOffset / 2 -> {
                                        offsetX.animateTo(maxOffset)
                                        onOpen(itemKey)
                                    }

                                    offsetX.value < minOffset / 2 -> {
                                        offsetX.animateTo(minOffset)
                                        onOpen(itemKey)
                                    }

                                    else -> {
                                        offsetX.animateTo(0f)
                                        onClose()
                                    }
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo(
                                (offsetX.value + dragAmount)
                                    .coerceIn(minOffset, maxOffset)
                            )
                        }
                    }
                },
            content = content
        )
    }
}

