package com.iltwats.splitflap

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val characters = " -1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ#$€:,."

@Composable
fun SplitFlapComposable(
    character: Char,
    color: Color,
    modifier: Modifier = Modifier
) {
    var pastChar by remember { mutableStateOf(characters.first()) }
    var finalChar by remember { mutableStateOf(character) }
    var indicatorRotation by remember { mutableStateOf(0f) }
    val animationDuration = 200
    val animationSpec =
        tween<Float>(animationDuration, easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.8f))

    LaunchedEffect(character) {
        val index = characters.indexOf(character)
        if (index >= 0) {
            (0..index).forEach {
                finalChar = characters[it]
                animate(
                    initialValue = 0f,
                    targetValue = -180f,
                    animationSpec = animationSpec
                ) { value, _ ->
                    indicatorRotation = value
                    if (value <= -175f) {
                        pastChar = characters[it]
                    }
                }
            }
        }
    }

    Box(
        modifier
            .background(Color(0xFF222222))
    ) {
        SplitFlapPiece(displayText = finalChar, color = color, modifier = Modifier
            .graphicsLayer {
                clip = true
                shape = object : Shape {
                    override fun createOutline(
                        size: Size,
                        layoutDirection: LayoutDirection,
                        density: Density
                    ): Outline {
                        return Outline.Rectangle(
                            size
                                .copy(height = size.height / 2 - 2, width = size.width)
                                .toRect()
                        )
                    }
                }
            }
            .background(Color.DarkGray)
            .border(BorderStroke(Dp.Hairline, Color(0xFF222222)))
        )

        SplitFlapPiece(displayText = pastChar, color = color, modifier = Modifier
            .graphicsLayer {
                clip = true
                rotationX = -180f
                rotationY = 180f
                rotationZ = 180f
                shape = object : Shape {
                    override fun createOutline(
                        size: Size,
                        layoutDirection: LayoutDirection,
                        density: Density
                    ): Outline {
                        return Outline.Rectangle(
                            size
                                .copy(height = size.height / 2, width = size.width)
                                .toRect()
                                .translate(0f, size.height / 2)
                        )
                    }
                }
            }
            .background(Color.DarkGray)
            .border(BorderStroke(Dp.Hairline, Color(0xFF222222)))
        )

        if (indicatorRotation >= -90f) {
            SplitFlapPiece(displayText = pastChar, color = color, modifier = Modifier
                .graphicsLayer {
                    rotationX = indicatorRotation
                    clip = true
                    shape = object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density
                        ): Outline {
                            return Outline.Rectangle(
                                size
                                    .copy(height = size.height / 2 - 2, width = size.width)
                                    .toRect()
                            )
                        }
                    }
                }
                .background(Color.DarkGray)
                .border(BorderStroke(Dp.Hairline, Color(0xFF222222)))
            )
        } else {
            SplitFlapPiece(displayText = finalChar, color = color, modifier = Modifier
                .graphicsLayer {
                    clip = true
                    rotationX = indicatorRotation
                    rotationY = 180f
                    rotationZ = 180f
                    shape = object : Shape {
                        override fun createOutline(
                            size: Size,
                            layoutDirection: LayoutDirection,
                            density: Density
                        ): Outline {
                            return Outline.Rectangle(
                                size
                                    .copy(height = size.height / 2, width = size.width)
                                    .toRect()
                                    .translate(0f, size.height / 2)
                            )
                        }
                    }
                    shadowElevation = 1f
                }
                .background(Color.DarkGray)
                .border(BorderStroke(Dp.Hairline, Color(0xFF222222)))
            )
        }
    }
}

@Composable
private fun SplitFlapPiece(displayText: Char, color: Color, modifier: Modifier) {
    val boltGradient = Brush.verticalGradient(
        colors = listOf(Color.DarkGray, Color.Black,Color.DarkGray, Color.Black,Color.DarkGray, Color.Black,Color.DarkGray, Color.Black,Color.DarkGray, Color.Black),
        tileMode = TileMode.Repeated,
    )
    BoxWithConstraints {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .then(modifier)
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {}
            Text(
                displayText.toString(),
                color = color,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 19.sp
            )
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {}
        }

        Box(
            Modifier
                .size(maxWidth / 15, maxHeight / 5)
                .background(Color(0xFF222222))
                .align(Alignment.CenterStart))
        Box(
            Modifier
                .size(maxWidth / 15 - 1.dp, maxHeight / 5 - 2.dp)
                .offset(x = 0.5.dp)
                .background(boltGradient)
                .align(Alignment.CenterStart))
        Box(
            Modifier
                .size(maxWidth / 15, maxHeight / 5)
                .background(Color(0xFF222222))
                .align(Alignment.CenterEnd))
        Box(
            Modifier
                .size(maxWidth / 15 - 1.dp, maxHeight / 5 - 2.dp)
                .offset(x = -0.5.dp)
                .background(boltGradient)
                .align(Alignment.CenterEnd))

    }

}

@Preview
@Composable()
fun TrainBoard() {
    val text = "04 DELHI-BERLIN"
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.DarkGray)
        .padding(12.dp)) {
        Row{
            Text("VIA", color = Color.White, fontSize = 10.sp)
            Spacer(modifier = Modifier.width(56.dp))
            Text("Route", color = Color.White, fontSize = 10.sp)
        }
        Row() {
            text.forEach {
                SplitFlapComposable(
                    character = it,
                    color = Color.White,
                    modifier = Modifier
                        .size(24.dp, 32.dp)
                )
            }
        }
    }
}

