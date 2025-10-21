package com.glassmorphism.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONArray
import android.content.Context
import com.glassmorphism.calculator.data.HistoryRepository
import com.glassmorphism.calculator.data.room.HistoryEntry
import kotlinx.coroutines.flow.collect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glassmorphism.calculator.ui.components.*
import com.glassmorphism.calculator.ui.theme.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.glassmorphism.calculator.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = SplashScreen.installSplashScreen(this)
        var keep = true
        splash.setKeepOnScreenCondition { keep }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentTheme by remember { mutableStateOf(ThemeType.GLASSMORPHISM) }
            
            AureumCalculatorTheme(themeType = currentTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen(
                        currentTheme = currentTheme,
                        onThemeChange = { currentTheme = it }
                    )
                }
            }
        }

        MainScope().launch {
            delay(800)
            keep = false
        }
        // Cleanup old exported CSV files from cache
        try {
            val files = cacheDir.listFiles()
            files?.filter { it.name.endsWith(".csv") }?.forEach { it.delete() }
        } catch (e: Exception) {
            // ignore
        }
    }
}

@Composable
fun CalculatorScreen(
    currentTheme: ThemeType,
    onThemeChange: (ThemeType) -> Unit,
    viewModel: CalculatorState = viewModel()
) {
    val context = LocalContext.current
    // Load persisted history once and save history on changes
    LaunchedEffect(Unit) {
        // Try to use Room repository to load and persist history; fallback to SharedPreferences
        var repo: HistoryRepository? = null
        try {
            repo = HistoryRepository.getInstance(context)
            repo.allEntries().collect { entries: List<HistoryEntry> ->
                val list = entries.map { e -> "${e.expression} = ${e.result}" }
                viewModel.replaceHistory(list)
            }
        } catch (_: Exception) {
            // ignore - we'll fallback below
        }

        try {
            val prefs = context.getSharedPreferences("calculator_prefs", Context.MODE_PRIVATE)
            snapshotFlow { viewModel.history.toList() }
                .collectLatest { lst ->
                    try {
                        // Persist to Room if available (replaceAll in a single transaction)
                        if (repo != null) {
                            val roomEntries = lst.map { entry ->
                                val parts = entry.split(" = ")
                                val expr = parts.getOrNull(0) ?: entry
                                val res = parts.getOrNull(1) ?: ""
                                com.glassmorphism.calculator.data.room.HistoryEntry(
                                    expression = expr,
                                    result = res
                                )
                            }
                            repo.replaceAll(roomEntries)
                        }

                        // Always persist to SharedPreferences as fallback
                        prefs.edit().putString("history_json", JSONArray(lst).toString()).apply()
                    } catch (_: Exception) {}
                }
        } catch (_: Exception) {}
    }
    var showHistory by remember { mutableStateOf(false) }
    var showConverter by remember { mutableStateOf(false) }
    val backgroundColors = when (currentTheme) {
        ThemeType.GLASSMORPHISM -> listOf(DarkNavy, DarkerNavy)
        ThemeType.AUTOMOTIVE -> listOf(AureumBackground, AureumSurface)
    }
    // Snackbar host for export confirmation
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(colors = backgroundColors)
                )
                .padding(16.dp)
        ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar: Theme toggle + History toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // History toggle
                when (currentTheme) {
                    ThemeType.GLASSMORPHISM -> {
                        GlassmorphicButton(
                            text = if (showHistory) "🗂️" else "🕘",
                            onClick = { showHistory = !showHistory },
                            isSpecial = true
                        )
                    }
                    ThemeType.AUTOMOTIVE -> {
                        AutomotiveButton(
                            text = if (showHistory) "🗂️" else "🕘",
                            onClick = { showHistory = !showHistory },
                            isSpecial = true
                        )
                    }
                }
                
                when (currentTheme) {
                    ThemeType.GLASSMORPHISM -> {
                        GlassmorphicButton(
                            text = "🏎️",
                            onClick = { onThemeChange(ThemeType.AUTOMOTIVE) },
                            isSpecial = true
                        )
                    }
                    ThemeType.AUTOMOTIVE -> {
                        AutomotiveButton(
                            text = "💎",
                            onClick = { onThemeChange(ThemeType.GLASSMORPHISM) },
                            isSpecial = true
                        )
                    }
                }
                // Converter toggle button
                GlassmorphicButton(
                    text = "💱",
                    onClick = { showConverter = !showConverter },
                    isSpecial = true
                )
            }
            
            if (showHistory) {
                HistoryPanel(
                    entries = viewModel.history,
                    themeType = currentTheme,
                    onClear = {
                        // Take a snapshot for undo
                        val snapshot = viewModel.getHistorySnapshot()
                        viewModel.clearHistory()
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = stringResource(id = R.string.history_cleared),
                                actionLabel = stringResource(id = R.string.undo)
                            )
                            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                viewModel.replaceHistory(snapshot)
                            }
                        }
                    },
                    onExport = {
                        val csv = viewModel.exportHistoryCsv()
                        if (csv.isNotBlank()) {
                            // Show a snackbar that export started
                            scope.launch {
                                snackbarHostState.showSnackbar(stringResource(id = R.string.export_started))
                            }
                            try {
                                val fileName = "calc_history_${System.currentTimeMillis()}.csv"
                                val file = java.io.File(cacheDir, fileName)
                                file.writeText(csv)
                                val uri = androidx.core.content.FileProvider.getUriForFile(
                                    this@MainActivity,
                                    "${packageName}.fileprovider",
                                    file
                                )

                                val sendIntent = android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND
                                    putExtra(android.content.Intent.EXTRA_STREAM, uri)
                                    type = "text/csv"
                                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                val shareIntent = android.content.Intent.createChooser(sendIntent, "Share history as CSV")
                                startActivity(shareIntent)
                            } catch (e: Exception) {
                                // Fallback: share as plain text and show failure snackbar
                                scope.launch {
                                    snackbarHostState.showSnackbar(stringResource(id = R.string.export_failed))
                                }
                                val sendIntent = android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND
                                    putExtra(android.content.Intent.EXTRA_TEXT, csv)
                                    type = "text/plain"
                                }
                                val shareIntent = android.content.Intent.createChooser(sendIntent, "Share history")
                                startActivity(shareIntent)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .height(160.dp)
                )
            }
            if (showConverter) {
                CurrencyConverter(onClose = { showConverter = false }, modifier = Modifier.fillMaxWidth())
            }

            // Display Panel
            when (currentTheme) {
                ThemeType.GLASSMORPHISM -> {
                    DisplayPanel(
                        display = viewModel.display,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                ThemeType.AUTOMOTIVE -> {
                    AutomotiveDisplay(
                        display = viewModel.display,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Button Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttons = listOf(
                    ButtonData("C", true, false),
                    ButtonData("⌫", true, false),
                    ButtonData("√", false, true),
                    ButtonData("÷", false, true),
                    
                    ButtonData("7", false, false),
                    ButtonData("8", false, false),
                    ButtonData("9", false, false),
                    ButtonData("×", false, true),
                    
                    ButtonData("4", false, false),
                    ButtonData("5", false, false),
                    ButtonData("6", false, false),
                    ButtonData("−", false, true),
                    
                    ButtonData("1", false, false),
                    ButtonData("2", false, false),
                    ButtonData("3", false, false),
                    ButtonData("+", false, true),
                    
                    ButtonData("0", false, false),
                    ButtonData(".", false, false),
                    ButtonData("%", false, true),
                    ButtonData("=", false, true)
                )
                
                items(buttons) { button ->
                    when (currentTheme) {
                        ThemeType.GLASSMORPHISM -> {
                            GlassmorphicButton(
                                text = button.text,
                                onClick = { handleButtonClick(button.text, viewModel) },
                                isOperator = button.isOperator,
                                isSpecial = button.isSpecial
                            )
                        }
                        ThemeType.AUTOMOTIVE -> {
                            AutomotiveButton(
                                text = button.text,
                                onClick = { handleButtonClick(button.text, viewModel) },
                                isOperator = button.isOperator,
                                isSpecial = button.isSpecial
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun handleButtonClick(text: String, viewModel: CalculatorState) {
    when (text) {
        "C" -> viewModel.onClearClick()
        "⌫" -> viewModel.onDeleteClick()
        "=" -> viewModel.onEqualsClick()
        "." -> viewModel.onDecimalClick()
        "+" -> viewModel.onOperationClick(Operation.ADD)
        "−" -> viewModel.onOperationClick(Operation.SUBTRACT)
        "×" -> viewModel.onOperationClick(Operation.MULTIPLY)
        "÷" -> viewModel.onOperationClick(Operation.DIVIDE)
        "√" -> viewModel.onOperationClick(Operation.SQRT)
        "%" -> viewModel.onOperationClick(Operation.PERCENTAGE)
        else -> if (text.all { it.isDigit() }) {
            viewModel.onNumberClick(text)
        }
    }
}

data class ButtonData(
    val text: String,
    val isSpecial: Boolean,
    val isOperator: Boolean
)
