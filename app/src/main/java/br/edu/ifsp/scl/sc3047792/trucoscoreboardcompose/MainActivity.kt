package br.edu.ifsp.scl.sc3047792.trucoscoreboardcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifsp.scl.sc3047792.trucoscoreboardcompose.ui.theme.TrucoScoreBoardComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrucoScoreBoardComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TrucoScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

private const val PONTUACAO_MAXIMA = 12

@Composable
fun TrucoScreen(modifier: Modifier = Modifier) {
    var pontosTimeA by remember { mutableIntStateOf(0) }
    var pontosTimeB by remember { mutableIntStateOf(0) }
    var incrementoAtual by remember { mutableIntStateOf(1) }

    val maoDeOnze = pontosTimeA == 11 || pontosTimeB == 11

    val vencedor = when {
        pontosTimeA >= PONTUACAO_MAXIMA -> "Time A"
        pontosTimeB >= PONTUACAO_MAXIMA -> "Time B"
        else -> null
    }

    val textoBotaoTruco = when (incrementoAtual) {
        1 -> "TRUCO! (+3)"
        3 -> "SEIS! (+6)"
        6 -> "NOVE! (+9)"
        9 -> "DOZE! (+12)"
        else -> "VALE 12!"
    }

    fun reiniciar() {
        pontosTimeA = 0
        pontosTimeB = 0
        incrementoAtual = 1
    }

    fun somarPontos(timeA: Boolean) {
        val pontosASomar = if (maoDeOnze) 1 else incrementoAtual
        if (timeA) {
            pontosTimeA = (pontosTimeA + pontosASomar).coerceAtMost(PONTUACAO_MAXIMA)
        } else {
            pontosTimeB = (pontosTimeB + pontosASomar).coerceAtMost(PONTUACAO_MAXIMA)
        }
        incrementoAtual = 1
    }

    fun pedirTruco() {
        if (!maoDeOnze) {
            incrementoAtual = if (incrementoAtual < 3) 3 else (incrementoAtual + 3).coerceAtMost(12)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Marcador de Truco",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TeamScore(
                    nome = "Time A",
                    pontos = pontosTimeA,
                    incremento = incrementoAtual,
                    onAdicionarPonto = { somarPontos(true) },
                    modifier = Modifier.weight(1f)
                )
                TeamScore(
                    nome = "Time B",
                    pontos = pontosTimeB,
                    incremento = incrementoAtual,
                    onAdicionarPonto = { somarPontos(false) },
                    modifier = Modifier.weight(1f)
                )
            }

            if (maoDeOnze) {
                Text(
                    text = "MÃO DE ONZE!",
                    color = Color(0xFFFF8F00),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00)),
                enabled = !maoDeOnze && incrementoAtual < 12,
                onClick = { pedirTruco() }
            ) {
                Text(
                    text = textoBotaoTruco,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            border = BorderStroke(1.dp, Color.White),
            onClick = { reiniciar() }
        ) {
            Text(text = "Reiniciar")
        }
    }

    vencedor?.let {
        AlertDialog(
            onDismissRequest = { reiniciar() },
            title = {
                Text(
                    text = "Fim de Jogo!",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = "$it venceu a partida! \uD83E\uDD86",
                    fontSize = 18.sp,
                    color = Color.White
                )
            },
            confirmButton = {
                TextButton(onClick = { reiniciar() }) {
                    Text(
                        text = "OK",
                        color = Color(0xFFFF8F00),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = Color(0xFF1E1E1E)
        )
    }
}

@Composable
fun TeamScore(
    nome: String,
    pontos: Int,
    incremento: Int,
    onAdicionarPonto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = nome,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = pontos.toString(),
            color = Color.White,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            textAlign = TextAlign.Center
        )

        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00)),
            onClick = onAdicionarPonto
        ) {
            Text(
                text = "+$incremento",
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrucoScreenPreview() {
    TrucoScoreBoardComposeTheme {
        TrucoScreen(modifier = Modifier)
    }
}