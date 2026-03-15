package net.felipealafy.studentplanner.ui.components.color.chooser

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.ui.theme.DarkGray

@Composable
fun ButtonOpenColorSelectionDialog(
    selectedColor: Long,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(35.dp)
            .border(width = 2.dp, color = DarkGray, shape = RoundedCornerShape(40.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(selectedColor)
        )
    ) {}
}