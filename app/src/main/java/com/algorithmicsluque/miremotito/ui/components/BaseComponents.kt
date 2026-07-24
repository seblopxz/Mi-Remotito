package com.algorithmicsluque.miremotito.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algorithmicsluque.miremotito.ui.theme.Dimens
import com.algorithmicsluque.miremotito.ui.theme.FullRoundedShape
import com.algorithmicsluque.miremotito.ui.theme.MiRemotitoTheme
import com.algorithmicsluque.miremotito.ui.theme.*

enum class ListItemPosition { FIRST, MIDDLE, LAST, SINGLE }

@Composable
fun ExpressiveListItem(
    position: ListItemPosition,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable RowScope.() -> Unit
) {
    val shape = when (position) {
        ListItemPosition.FIRST -> TopSegmentShape
        ListItemPosition.MIDDLE -> MiddleSegmentShape
        ListItemPosition.LAST -> BottomSegmentShape
        ListItemPosition.SINGLE -> FullSegmentShape
    }

    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}

@Composable
fun DeviceCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(Dimens.DeviceCardHeight),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RemoteButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    size: androidx.compose.ui.unit.Dp = Dimens.RemoteButtonSizeLarge
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.size(size),
        shape = FullRoundedShape,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(size * 0.4f)
        )
    }
}

@Composable
fun DPad(
    onUp: () -> Unit,
    onDown: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onOk: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(Dimens.DPadSize),
        contentAlignment = Alignment.Center
    ) {
        // Main Circle
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = DPadShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(Modifier.fillMaxSize()) {
                // Directional indicators (Dots from Figma)
                val dotColor = MaterialTheme.colorScheme.onPrimaryContainer
                Box(Modifier.size(5.dp).background(dotColor, FullRoundedShape).align(Alignment.TopCenter).offset(y = 15.dp))
                Box(Modifier.size(5.dp).background(dotColor, FullRoundedShape).align(Alignment.BottomCenter).offset(y = (-15).dp))
                Box(Modifier.size(5.dp).background(dotColor, FullRoundedShape).align(Alignment.CenterStart).offset(x = 15.dp))
                Box(Modifier.size(5.dp).background(dotColor, FullRoundedShape).align(Alignment.CenterEnd).offset(x = (-15).dp))

                // Clickable areas
                Column(Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1f).fillMaxWidth().clickable { onUp() })
                    Row(Modifier.weight(1f).fillMaxWidth()) {
                        Box(Modifier.weight(1f).fillMaxHeight().clickable { onLeft() })
                        Spacer(Modifier.size(Dimens.DPadCenterSize))
                        Box(Modifier.weight(1f).fillMaxHeight().clickable { onRight() })
                    }
                    Box(Modifier.weight(1f).fillMaxWidth().clickable { onDown() })
                }
            }
        }

        // Center OK Button
        Surface(
            modifier = Modifier.size(Dimens.DPadCenterSize),
            shape = DPadShape,
            color = MaterialTheme.colorScheme.surfaceTint,
            onClick = onOk
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "OK",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun RemotePillButton(
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(100.dp, 210.dp),
        shape = FullRoundedShape,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onPlusClick,
                modifier = Modifier.size(100.dp).weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Plus",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(40.dp)
                )
            }
            // Indicator bar
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(5.dp)
                    .background(MaterialTheme.colorScheme.secondary, FullRoundedShape)
            )
            IconButton(
                onClick = onMinusClick,
                modifier = Modifier.size(100.dp).weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = "Minus",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemotitoAppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (navigationIcon != null) {
                navigationIcon()
            } else if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DeviceCardPreview() {
    MiRemotitoTheme {
        Box(Modifier.padding(20.dp)) {
            DeviceCard(
                title = "Samsung TV",
                icon = Icons.Rounded.Tv,
                onClick = {},
                modifier = Modifier.width(Dimens.DeviceCardWidth)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RemoteButtonPreview() {
    MiRemotitoTheme {
        Row(Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RemoteButton(icon = Icons.Rounded.Add, onClick = {})
            RemoteButton(
                icon = Icons.Rounded.Add,
                onClick = {},
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DPadPreview() {
    MiRemotitoTheme {
        DPad(onUp = {}, onDown = {}, onLeft = {}, onRight = {}, onOk = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PillButtonPreview() {
    MiRemotitoTheme {
        RemotePillButton(onPlusClick = {}, onMinusClick = {})
    }
}
