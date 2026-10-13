package br.com.ffperformancekey

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppScreen() }
    }
}

@Composable
private fun AppScreen() {
    val context = LocalContext.current
    var keyText by remember { mutableStateOf("") }
    var activated by remember { mutableStateOf(false) }
    var message by remember {
        mutableStateOf("Modo demonstração: configure um servidor para validar keys reais.")
    }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("FF PERFORMANCE KEY", style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)
                Text("Painel de desempenho • Android", color = MaterialTheme.colorScheme.secondary)
                HorizontalDivider()

                Text("1. Ativar acesso", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = keyText,
                    onValueChange = { keyText = it },
                    label = { Text("Sua key de demonstração") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        // DEMO ONLY: this is not secure license validation.
                        // Replace with HTTPS API validation before public release.
                        activated = keyText.trim() == "FF-DEMO-2026"
                        message = if (activated) "Key de demonstração aceita."
                                  else "Key inválida. Para produção, conecte a API de licenças."
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Validar key") }
                Text(message, style = MaterialTheme.typography.bodyMedium)

                HorizontalDivider()
                Text("2. Ajustes legítimos", style = MaterialTheme.typography.titleMedium)
                Text("• Feche apps em segundo plano antes de jogar.")
                Text("• Ative o modo de jogo/desempenho nas configurações do fabricante, se disponível.")
                Text("• Use uma taxa de atualização alta se seu aparelho e jogo oferecerem suporte.")
                Text("• Reduza os gráficos dentro do jogo se houver quedas de FPS.")
                Text("• Evite jogar enquanto o aparelho estiver superaquecido ou carregando sob calor intenso.")

                OutlinedButton(
                    onClick = {
                        runCatching {
                            context.startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS))
                        }.onFailure { message = "Não foi possível abrir as configurações de tela." }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Abrir configurações de tela") }

                OutlinedButton(
                    onClick = {
                        runCatching {
                            context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            })
                        }.onFailure { message = "Não foi possível abrir as configurações do app." }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Configurações deste aplicativo") }

                HorizontalDivider()
                Text("3. Depuração USB / ADB", style = MaterialTheme.typography.titleMedium)
                Text("A depuração USB é ativada nas Opções do desenvolvedor do Android. Conecte apenas a um computador confiável e confirme a impressão digital RSA no telefone.")
                Text("Este APK não executa comandos ADB por conta própria. Use o ADB no computador, com autorização explícita.")

                Text("Aviso", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("A key incluída é apenas demonstrativa e pode ser descoberta no código. Não use para vender acessos. A validação real deve ocorrer em um servidor HTTPS. O app não altera o Free Fire nem aumenta artificialmente a velocidade do personagem.")
            }
        }
    }
}
