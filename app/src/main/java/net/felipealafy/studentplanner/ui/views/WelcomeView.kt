package net.felipealafy.studentplanner.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.ui.theme.Blue
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.LightGray
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.White
import net.felipealafy.studentplanner.ui.theme.colorPallet


@Composable
fun WelcomeView(onStartClick: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize().background(
            color = Color(colorPallet[0][1]).copy(alpha = 0.7F)
        )
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(
                color = Color(colorPallet[0][1]).copy(alpha = 0.7F)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Title()
            TextComponent()
            Row (
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ImportButton { }
                StartButton(onClick = onStartClick)
            }
        }
    }
}

@Composable
fun Title() {
    Text(
        text = stringResource(R.string.welcome),
        style = Typography.displayLarge,
        color = DarkGray
    )
}

@Composable
fun TextComponent() {
    Text(
        text = stringResource(R.string.advise_message),
        style = Typography.bodyLarge,
        color = DarkGray
    )
}

@Composable
fun ImportButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(60.dp, 60.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Blue,
            contentColor = White
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_open_24),
                contentDescription = stringResource(R.string.search_icon),
                modifier = Modifier
                    .weight(0.2f)
                    .size(16.dp)
            )
        }
    }
}

@Composable
fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(60.dp).padding(start = 15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            disabledContainerColor = LightGray,
            disabledContentColor = DarkGray,
            contentColor = White
        )
    ) {
        Text(
            text = stringResource(R.string.start_button),
            style = Typography.labelSmall,
            color = White
        )
    }
}