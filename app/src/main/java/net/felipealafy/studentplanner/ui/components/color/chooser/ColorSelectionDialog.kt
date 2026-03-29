package net.felipealafy.studentplanner.ui.components.color.chooser

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.ui.theme.Transparent
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.theme.colorutils.getContrastingColorForText

@Composable
fun ColorSelectionDialog(
    selectedColor: Long,
    onColorSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Transparent
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(selectedColor).copy(alpha = 0.7F)
                ),
                elevation = CardDefaults.cardElevation(20.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.color_chooser_dialog),
                        modifier = Modifier.fillMaxWidth(),
                        style = Typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        color = Color(selectedColor.getContrastingColorForText())
                    )
                    Row(
                        modifier = Modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        colorPallet.forEach { column ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                column.forEach { color ->
                                    IconButton(
                                        onClick = {
                                            onColorSelected(color)
                                            onDismissRequest()
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = Color(color)
                                        ),
                                        modifier = Modifier
                                            .size(35.dp)
                                            .border(
                                                width = 0.dp,
                                                color = Transparent,
                                                shape = RoundedCornerShape(40.dp)
                                            )
                                    ) {
                                        if (color == selectedColor) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = stringResource(R.string.check_color)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}