package br.edu.ifsp.scl.sc3047792.trucoscoreboardcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            TeamScore(
                nome = "Time A",
                pontos = pontosTimeA,
                onAdicionarPonto = { pontosTimeA = adicionarPontos(pontosTimeA, 1) },
                onTruco = { pontosTimeA = adicionarPontos(pontosTimeA, 3) },
                modifier = Modifier.weight(1f)
            )
            TeamScore(
                nome = "Time B",
                pontos = pontosTimeB,
                onAdicionarPonto = { pontosTimeB = adicionarPontos(pontosTimeB, 1) },
                onTruco = { pontosTimeB = adicionarPontos(pontosTimeB, 3) },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            onClick = {
                pontosTimeA = 0
                pontosTimeB = 0
            }
        ) {
            Text(text = "Reiniciar")
        }
    }
}

@Composable
fun TeamScore(
    nome: String,
    pontos: Int,
    onAdicionarPonto: () -> Unit,
    onTruco: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = nome,
            fontSize = 22.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = pontos.toString(),
            fontSize = 48.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Button(modifier = Modifier.fillMaxWidth(), onClick = onAdicionarPonto
        ) {
            Text(text = "+1")
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = onTruco
        ) {
            Text(text = "Truco (+3)")
        }
    }
}
private fun adicionarPontos(pontosAtuais: Int, incremento: Int): Int {
    val novoTotal = pontosAtuais + incremento
    return if (novoTotal > PONTUACAO_MAXIMA) PONTUACAO_MAXIMA else novoTotal
}

@Preview(showBackground = true)
@Composable
fun TrucoScreenPreview() {
    TrucoScoreBoardComposeTheme {
        TrucoScreen(modifier = Modifier)
    }
}