package com.widgetfy.com

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.widgetfy.com.ui.theme.WidgetfyTheme
import com.widgetfy.com.util.QuoteProvider
import com.widgetfy.com.util.TimeUtils
import com.widgetfy.com.util.WidgetPrefs
import com.widgetfy.com.widget.*
import java.text.SimpleDateFormat
import java.util.*

data class WidgetInfo(
    val id: String,
    val name: String,
    val desc: String,
    val category: String,
    val icon: ImageVector,
    val gradient: List<Color>,
    val provider: Class<*>,
    val previewText: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WidgetfyTheme {
                WidgetfyApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetfyApp() {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf<WidgetInfo?>(null) }

    val widgets = remember {
        listOf(
            WidgetInfo("digital", "Digital Clock", "Minimal digital clock with date. Updates every minute.", "Clock", Icons.Filled.Schedule, listOf(Color(0xFF6C5CE7), Color(0xFFA29BFE)), DigitalClockWidget::class.java, TimeUtils.currentTime()),
            WidgetInfo("analog", "Analog Clock", "Classic analog clock with smooth hands.", "Clock", Icons.Filled.AccessTime, listOf(Color(0xFF00CEC9), Color(0xFF55EFC4)), AnalogClockWidget::class.java, "◷ ${TimeUtils.currentTime()}"),
            WidgetInfo("flip", "Flip Clock", "Retro flip-style bold typography.", "Clock", Icons.Filled.Timer, listOf(Color(0xFFFD79A8), Color(0xFFFDCB6E)), FlipClockWidget::class.java, "${TimeUtils.hour()}:${TimeUtils.minute()}"),
            WidgetInfo("battery", "Battery Status", "Live battery % with charging animation and health.", "System", Icons.Filled.BatteryChargingFull, listOf(Color(0xFF00B894), Color(0xFF55EFC4)), BatteryWidget::class.java, "87% • Charging"),
            WidgetInfo("weather", "Weather", "Real-time weather with location and forecast.", "System", Icons.Filled.WbSunny, listOf(Color(0xFFFDCB6E), Color(0xFFE17055)), WeatherWidget::class.java, "28°C Sunny"),
            WidgetInfo("calendar", "Calendar", "Month view and upcoming events.", "Productivity", Icons.Filled.CalendarMonth, listOf(Color(0xFF6C5CE7), Color(0xFF00CEC9)), CalendarWidget::class.java, SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())),
            WidgetInfo("todo", "To-Do & Notes", "Quick notes and tasks right on home screen.", "Productivity", Icons.Filled.TaskAlt, listOf(Color(0xFF00CEC9), Color(0xFF0984E3)), TodoWidget::class.java, "3 tasks • Tap to edit"),
            WidgetInfo("photo", "Photo Frame", "Show your favorite photo as widget.", "Lifestyle", Icons.Filled.PhotoLibrary, listOf(Color(0xFFFDCB6E), Color(0xFFFF7675)), PhotoFrameWidget::class.java, "🖼️ Your photo"),
            WidgetInfo("search", "Search Bar", "Google search in one tap, with voice support.", "System", Icons.Filled.Search, listOf(Color(0xFF636E72), Color(0xFFB2BEC3)), SearchWidget::class.java, "Search Google…"),
            WidgetInfo("quote", "Daily Quote", "Inspirational quote that refreshes daily.", "Lifestyle", Icons.Filled.FormatQuote, listOf(Color(0xFF6C5CE7), Color(0xFFFD79A8)), QuoteWidget::class.java, QuoteProvider.ofDay().first.take(24)+"…"),
            WidgetInfo("world", "World Clock", "Multiple timezones in one widget.", "Clock", Icons.Filled.Language, listOf(Color(0xFF00CEC9), Color(0xFF6C5CE7)), WorldClockWidget::class.java, "Delhi • London • NY"),
            WidgetInfo("steps", "Step Counter", "Track your daily steps.", "Lifestyle", Icons.Filled.DirectionsWalk, listOf(Color(0xFFD63031), Color(0xFFFF7675)), StepCounterWidget::class.java, "7,842 steps")
        )
    }

    val categories = listOf("All", "Clock", "System", "Productivity", "Lifestyle")
    val filtered = if (selectedCategory == "All") widgets else widgets.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Widgetfy", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                        Text("Beautiful widgets for your home", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = { Toast.makeText(context, "Widgetfy v1.0.0 • Made with ❤️", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Filled.Info, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {

            // Hero Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(Color(0xFF6C5CE7), Color(0xFF00CEC9))), shape = RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("✨ 12 Premium Widgets", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text("Add stunning widgets\nto your home screen", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 22.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Long press home screen → Widgets → Widgetfy", color = Color.White.copy(alpha=0.8f), fontSize = 11.sp)
                        }
                        Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha=0.2f)), contentAlignment = Alignment.Center) {
                            Text(TimeUtils.currentTime(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Live preview row
            Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoChip("12 Widgets", Icons.Filled.Widgets)
                InfoChip("100% Real", Icons.Filled.Verified)
                InfoChip("No Ads", Icons.Filled.Block)
                InfoChip("Free Forever", Icons.Filled.Favorite)
            }

            Spacer(Modifier.height(16.dp))

            // Categories
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) },
                        leadingIcon = if (selectedCategory == cat) { { Icon(Icons.Filled.Check, null, Modifier.size(16.dp)) } } else null
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.heightIn(max = 2000.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(filtered) { w ->
                    WidgetCard(widget = w, onAdd = { showAddDialog = w })
                }
            }

            Spacer(Modifier.height(20.dp))

            // How to add
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("📲 How to add widgets?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("1. Long press on home screen\n2. Tap Widgets\n3. Find Widgetfy section\n4. Drag any widget to home screen\n\nOr tap 'Add to Home' below - works on Android 8+!", fontSize = 12.sp, lineHeight = 16.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Quote of day
            val quote = QuoteProvider.ofDay()
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💡 Quote of the Day", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    Text("\"${quote.first}\"", fontSize = 13.sp, fontWeight = FontWeight.Medium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    Spacer(Modifier.height(4.dp))
                    Text("— ${quote.second}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(80.dp))
        }

        // Add dialog
        showAddDialog?.let { w ->
            AlertDialog(
                onDismissRequest = { showAddDialog = null },
                title = { Text("Add ${w.name}?") },
                text = { Text("This will pin '${w.name}' widget to your home screen. Your launcher must support pinned widgets (Android 8+).") },
                confirmButton = {
                    Button(onClick = {
                        showAddDialog = null
                        pinWidget(context, w)
                    }) { Text("Add to Home") }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = null }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun InfoChip(text: String, icon: ImageVector) {
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(6.dp))
            Text(text, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun WidgetCard(widget: WidgetInfo, onAdd: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(90.dp)
                    .background(Brush.linearGradient(widget.gradient), shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(alpha=0.2f)) {
                            Icon(widget.icon, null, tint = Color.White, modifier = Modifier.padding(6.dp).size(16.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(widget.category, color = Color.White.copy(alpha=0.9f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(widget.previewText, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(widget.name, color = Color.White.copy(alpha=0.85f), fontSize = 10.sp)
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(widget.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                Spacer(Modifier.height(2.dp))
                Text(widget.desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, lineHeight = 13.sp)
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = onAdd,
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Filled.Add, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add to Home", fontSize = 12.sp)
                }
                TextButton(onClick = {
                    Toast.makeText(context, "${widget.name}: ${widget.desc}", Toast.LENGTH_SHORT).show()
                }, modifier = Modifier.fillMaxWidth().height(28.dp), contentPadding = PaddingValues(0.dp)) {
                    Text("Preview", fontSize = 11.sp)
                }
            }
        }
    }
}

fun pinWidget(context: android.content.Context, info: WidgetInfo) {
    val mgr = AppWidgetManager.getInstance(context)
    val provider = ComponentName(context, info.provider as Class<android.appwidget.AppWidgetProvider>)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (mgr.isRequestPinAppWidgetSupported) {
            val successCallback = android.app.PendingIntent.getBroadcast(context, 0, Intent(context, PinResultReceiver::class.java), android.app.PendingIntent.FLAG_IMMUTABLE)
            mgr.requestPinAppWidget(provider, null, successCallback)
            Toast.makeText(context, "Drag to place '${info.name}' on home screen", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Pin widget not supported on this launcher. Try long press Home → Widgets", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Android 8+ required for pin. Long press Home → Widgets → Widgetfy", Toast.LENGTH_LONG).show()
    }
}

class PinResultReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: android.content.Context, intent: Intent) {
        Toast.makeText(context, "Widget added! Check home screen ✨", Toast.LENGTH_SHORT).show()
    }
}
