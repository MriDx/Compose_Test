package com.example.composetest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.composetest.ui.theme.ComposeTestTheme


@Preview
@Composable
fun ExpandedBoxDemo() {
    ComposeTestTheme(darkTheme = true) {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier
                        .wrapContentHeight()
                        .background(color = Color.Red)) {
                        Text(text = "Hello World !")
                    }
                    ExpandedBox(modifier = Modifier
                        .fillMaxSize()
                        .weight(1.0f).background(color = Color.Green)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Expanded Box will take all available space !", style = TextStyle(color = Color.Black))
                        }
                    }
                    Box(modifier = Modifier
                        .wrapContentSize()
                        .background(color = Color.Red)) {
                        Text(text = "Hello Jetpack !")
                    }
                }
            }
        }
    }

}

@Composable
inline fun ExpandedBox(modifier: Modifier, content: @Composable () -> Unit) {
    Layout(
        content = { content.invoke() },
        modifier = modifier
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}