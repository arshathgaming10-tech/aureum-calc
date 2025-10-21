package com.glassmorphism.calculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import com.glassmorphism.calculator.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.glassmorphism.calculator.ui.theme.*

@Composable
fun HistoryPanel(
    entries: List<String>,
    themeType: ThemeType,
    onClear: () -> Unit,
    onExport: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val backgroundBrush = when (themeType) {
        ThemeType.GLASSMORPHISM -> Brush.verticalGradient(listOf(GlassWhite.copy(0.25f), GlassWhite.copy(0.1f)))
        ThemeType.AUTOMOTIVE -> Brush.verticalGradient(listOf(AureumCard, AureumDarkGrey))
    }
    val borderColor = when (themeType) {
        ThemeType.GLASSMORPHISM -> GlassBorder
        ThemeType.AUTOMOTIVE -> AureumAmber
    }
    val textColor = when (themeType) {
        ThemeType.GLASSMORPHISM -> TextPrimary
        ThemeType.AUTOMOTIVE -> AureumTextPrimary
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundBrush)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(id = R.string.history), color = textColor)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (onExport != null) {
                    Button(
                        onClick = onExport,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (themeType) {
                                ThemeType.GLASSMORPHISM -> GlassCyan
                                ThemeType.AUTOMOTIVE -> AureumAmber
                            }
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = stringResource(id = R.string.export))
                    }
                }
                Button(
                    onClick = onClear,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (themeType) {
                            ThemeType.GLASSMORPHISM -> GlassCyan
                            ThemeType.AUTOMOTIVE -> AureumAmber
                        }
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = stringResource(id = R.string.clear))
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(entries) { item ->
                Text(text = item, color = textColor)
            }
        }
    }
}


