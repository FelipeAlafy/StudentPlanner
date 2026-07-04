package net.felipealafy.studentplanner.ui.components.color.chooser

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.ui.theme.colorPallet

@Composable
fun ButtonOpenColorSelectionDialog(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(35.dp)
            .border(width = 2.dp, color = DarkGray, shape = RoundedCornerShape(40.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = PlannerTheme.colors.primary
        )
    ) {}
}

@Preview
@Composable
private fun ButtonCOpenColorSelectionDialogPreview() {
    ButtonOpenColorSelectionDialog { }
}