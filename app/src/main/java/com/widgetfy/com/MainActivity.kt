package com.widgetfy.com

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.widgetfy.com.ui.theme.WidgetfyTheme
import com.widgetfy.com.ui.theme.LiquidGlass
import com.widgetfy.com.ui.theme.liquidGlass
import com.widgetfy.com.ui.theme.liquidGlassCard
import com.widgetfy.com.ui.theme.GlassEffectContainer
import com.widgetfy.com.util.QuoteProvider
import com.widgetfy.com.util.TimeUtils
import com.widgetfy.com.widget.*

data class WidgetInfo(
    val id: String,
    val name: String,
    val desc: String,
    val category: String,
    val icon: ImageVector,
    val gradient: List<Color>,
    val provider: Class<*>,
    val previewText: String,
    val stats: String,
    val isNew: Boolean = false,
    val isPopular: Boolean = false,
    val size: BentoSize = BentoSize.Normal
)

enum class BentoSize { Hero, Wide, Normal, Small }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WidgetfyTheme { WidgetfyApp() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetfyApp() {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf<WidgetInfo?>(null) }
    var showDetail by remember { mutableStateOf<WidgetInfo?>(null) }

    // 26 Widgets — Liquid Glass Edition (20 + 6 new)
    val widgets = remember {
        listOf(
            WidgetInfo("digital", "Digital Clock", "Live time, date & timezone. Updates every minute.", "Clock", Icons.Filled.Schedule, listOf(Color(0xFF6750A4), Color(0xFF9C88FF)), DigitalClockWidget::class.java, TimeUtils.currentTime(), "4.8 • 12k uses", isPopular = true, size = BentoSize.Hero),
            WidgetInfo("analog", "Analog Clock", "Classic analog with smooth hands & date.", "Clock", Icons.Filled.AccessTime, listOf(Color(0xFF0D9488), Color(0xFF14B8A6)), AnalogClockWidget::class.java, "◷ ${TimeUtils.currentTime()}", "4.7 • 8k uses", size = BentoSize.Normal),
            WidgetInfo("flip", "Flip Clock", "Retro flip typography, bold & elegant.", "Clock", Icons.Filled.Timer, listOf(Color(0xFFE11D48), Color(0xFFFB7185)), FlipClockWidget::class.java, "${TimeUtils.hour()}:${TimeUtils.minute()}", "4.9 • 15k uses", isNew = true, size = BentoSize.Normal),
            WidgetInfo("world", "World Clock", "Delhi • London • New York in one.", "Clock", Icons.Filled.Language, listOf(Color(0xFF2563EB), Color(0xFF60A5FA)), WorldClockWidget::class.java, "Delhi • London • NY", "4.6 • 5k uses", size = BentoSize.Wide),
            WidgetInfo("battery", "Battery Pro", "Real battery % + health & charging animation.", "System", Icons.Filled.BatteryChargingFull, listOf(Color(0xFF00B894), Color(0xFF00CEC9)), BatteryWidget::class.java, "87% • Charging", "4.8 • 10k uses", isPopular = true, size = BentoSize.Normal),
            WidgetInfo("weather", "Weather Glass", "Live temp, city & 3-day forecast glass.", "System", Icons.Filled.WbSunny, listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)), WeatherWidget::class.java, "28°C Sunny", "4.7 • 9k uses", size = BentoSize.Wide),
            WidgetInfo("search", "Search Bar", "Google search + voice, instant access.", "System", Icons.Filled.Search, listOf(Color(0xFF49454E), Color(0xFF79747E)), SearchWidget::class.java, "Search Google…", "4.5 • 6k uses", size = BentoSize.Wide),
            WidgetInfo("steps", "Step Tracker", "Daily steps, calories & goal progress.", "Health", Icons.Filled.DirectionsWalk, listOf(Color(0xFFDC2626), Color(0xFFFF6B6B)), StepCounterWidget::class.java, "7,842 steps", "4.8 • 7k uses", size = BentoSize.Normal),
            WidgetInfo("calendar", "Calendar Bento", "Month grid + events timeline.", "Productivity", Icons.Filled.CalendarMonth, listOf(Color(0xFF6750A4), Color(0xFF0D9488)), CalendarWidget::class.java, "Oct 2024 • 3 events", "4.9 • 11k uses", isPopular = true, size = BentoSize.Normal),
            WidgetInfo("todo", "Tasks & Notes", "Quick tasks on home screen, sync ready.", "Productivity", Icons.Filled.TaskAlt, listOf(Color(0xFF0891B2), Color(0xFF06B6D4)), TodoWidget::class.java, "3 tasks • Tap edit", "4.6 • 6k uses", size = BentoSize.Wide),
            WidgetInfo("photo", "Photo Frame", "Your favorite pic, live on home.", "Lifestyle", Icons.Filled.PhotoLibrary, listOf(Color(0xFFEA580C), Color(0xFFFB923C)), PhotoFrameWidget::class.java, "Your photo • Frame", "4.7 • 4k uses", size = BentoSize.Normal),
            WidgetInfo("quote", "Daily Zen", "Quotes that inspire, rotate daily.", "Lifestyle", Icons.Filled.FormatQuote, listOf(Color(0xFF7C3AED), Color(0xFFA78BFA)), QuoteWidget::class.java, QuoteProvider.ofDay().first.take(22)+"…", "4.8 • 8k uses", size = BentoSize.Normal),
            WidgetInfo("music", "Music Player", "Playback controls + album art widget.", "Lifestyle", Icons.Filled.PlayArrow, listOf(Color(0xFF1DB954), Color(0xFF1ED760)), MusicWidget::class.java, "♪ Now Playing", "4.9 • 14k uses", isNew = true, size = BentoSize.Wide),
            WidgetInfo("countdown", "Countdown", "Events, birthdays - days left tracker.", "Productivity", Icons.Filled.Timer, listOf(Color(0xFF0EA5E9), Color(0xFF38BDF8)), CountdownWidget::class.java, "Diwali: 5 days", "4.7 • 5k uses", isNew = true, size = BentoSize.Normal),
            WidgetInfo("finance", "Finance Pulse", "Budget, expenses & savings snapshot.", "Productivity", Icons.Filled.AccountBalance, listOf(Color(0xFF059669), Color(0xFF10B981)), FinanceWidget::class.java, "₹12,450 saved", "4.8 • 9k uses", size = BentoSize.Normal),
            WidgetInfo("health", "Health Ring", "Sleep, heart & hydration rings.", "Health", Icons.Filled.Favorite, listOf(Color(0xFFDC2626), Color(0xFFFECACA)), HealthWidget::class.java, "Sleep 7.5h • 78%", "4.9 • 13k uses", isNew = true, size = BentoSize.Hero),
            WidgetInfo("news", "News Headlines", "Top headlines, tap to read.", "Lifestyle", Icons.Filled.Description, listOf(Color(0xFF1E293B), Color(0xFF334155)), NewsWidget::class.java, "Breaking News • 5", "4.6 • 7k uses", size = BentoSize.Wide),
            WidgetInfo("crypto", "Crypto Ticker", "BTC, ETH live price & change.", "Finance", Icons.Filled.Paid, listOf(Color(0xFFF59E0B), Color(0xFFFACC15)), CryptoWidget::class.java, "BTC ₹58L +2.4%", "4.7 • 6k uses", size = BentoSize.Normal),
            WidgetInfo("habit", "Habit Streak", "Daily habits & streak counter.", "Productivity", Icons.Filled.Star, listOf(Color(0xFFF97316), Color(0xFFFDBA74)), HabitWidget::class.java, "12 day streak", "4.8 • 10k uses", isPopular = true, size = BentoSize.Normal),
            WidgetInfo("reminder", "Quick Reminder", "Upcoming tasks & alerts.", "Productivity", Icons.Filled.Notifications, listOf(Color(0xFF8B5CF6), Color(0xFFDDD6FE)), ReminderWidget::class.java, "3 reminders • Today", "4.5 • 4k uses", size = BentoSize.Normal),
            // NEW 6 Liquid Glass Widgets (v3.0)
            WidgetInfo("timer", "Focus Timer", "Pomodoro 25:00 timer with start/reset.", "Productivity", Icons.Filled.HourglassBottom, listOf(Color(0xFFFF6B6B), Color(0xFFFFB86C)), TimerWidget::class.java, "25:00 • Focus", "4.9 • 18k uses", isNew = true, size = BentoSize.Normal),
            WidgetInfo("savings", "Savings Goal", "Track savings towards your dream purchase.", "Finance", Icons.Filled.Savings, listOf(Color(0xFF6750A4), Color(0xFFB794F6)), SavingsWidget::class.java, "₹68k / ₹1L", "4.8 • 7k uses", isNew = true, size = BentoSize.Normal),
            WidgetInfo("breathe", "Breathe", "Guided breathing: inhale, hold, exhale.", "Health", Icons.Filled.Spa, listOf(Color(0xFF00B894), Color(0xFF74B9FF)), BreatheWidget::class.java, "Inhale • 4s", "4.9 • 9k uses", isNew = true, size = BentoSize.Normal),
            WidgetInfo("dial", "Quick Dial", "One-tap call to favorites.", "System", Icons.Filled.Call, listOf(Color(0xFF2980B9), Color(0xFF6DD5FA)), DialWidget::class.java, "Mom • Dad • Boss", "4.7 • 5k uses", isNew = true, size = BentoSize.Wide),
            WidgetInfo("vpn", "VPN Control", "One-tap VPN connect & location.", "System", Icons.Filled.VpnKey, listOf(Color(0xFF0F2027), Color(0xFF2BC0E4)), VpnWidget::class.java, "● Connected SG", "4.8 • 6k uses", isNew = true, size = BentoSize.Normal),
            WidgetInfo("screentime", "Screen Time", "Daily usage & top apps breakdown.", "System", Icons.Filled.PhoneAndroid, listOf(Color(0xFF232526), Color(0xFF414345)), ScreenTimeWidget::class.java, "5h 32m today", "4.6 • 4k uses", isNew = true, size = BentoSize.Wide),
        )
    }

    val categories = listOf("All", "Clock", "System", "Productivity", "Health", "Lifestyle", "Finance")
    val filtered = when {
        searchQuery.isNotBlank() -> widgets.filter { it.name.contains(searchQuery, true) || it.desc.contains(searchQuery, true) }
        selectedCategory == "All" -> widgets
        else -> widgets.filter { it.category == selectedCategory }
    }

    // Liquid Glass scaffold: gradient background with aurora orbs
    Box(modifier = Modifier.fillMaxSize()) {
        // BackgroundExtensionEffect equivalent: full-screen liquid glass gradient
        Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(
            listOf(Color(0xFFFDF7FF), Color(0xFFE9DDFF), Color(0xFFDAF8FF), Color(0xFFFFF8F7))
        )))
        // Aurora orbs for refraction
        Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(
            colors = listOf(Color(0xFF7B61FF).copy(alpha=0.20f), Color.Transparent), radius = 900f
        )))
        Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(
            colors = listOf(Color(0xFF00D1C1).copy(alpha=0.12f), Color.Transparent), radius = 700f
        )))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                GlassTopBar(searchQuery) { searchQuery = it }
            },
            bottomBar = { LiquidGlassBottomBar() }
        ) { padding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(padding).fillMaxSize().background(Color.Transparent),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // HERO STATS — liquid glass container group
                item(span = { GridItemSpan(2) }) {
                    GlassStatsRow(total = widgets.size, active = 10, rating = "4.8")
                }
                // FEATURED HERO — liquid glass bento (full width)
                item(span = { GridItemSpan(2) }) {
                    LiquidGlassFeaturedCard(
                        onAdd = { showAddDialog = widgets.first { it.id == "digital" } },
                        onPreview = { showDetail = widgets.first { it.id == "digital" } }
                    )
                }
                // Trending chips
                item(span = { GridItemSpan(2) }) {
                    TrendingRow()
                }
                // Category filters — glass style
                item(span = { GridItemSpan(2) }) {
                    CategoryFilterRow(categories, selectedCategory) { selectedCategory = it }
                }
                item(span = { GridItemSpan(2) }) {
                    GlassResultBar(count = filtered.size, onClear = { selectedCategory = "All"; searchQuery = "" })
                }
                // BENTO GRID — varied spans with liquid glass cards
                itemsIndexed(filtered, span = { _, w ->
                    when (w.size) {
                        BentoSize.Hero -> GridItemSpan(2)
                        BentoSize.Wide -> GridItemSpan(2)
                        else -> GridItemSpan(1)
                    }
                }) { _, w ->
                    LiquidGlassWidgetCard(widget = w, onAdd = { showAddDialog = w }, onDetail = { showDetail = w })
                }
                item(span = { GridItemSpan(2) }) { HowToGlassBento() }
                item(span = { GridItemSpan(2) }) { QuoteGlassBento() }
                item(span = { GridItemSpan(2) }) {
                    FooterGlassBento()
                    Spacer(Modifier.height(12.dp))
                }
            }

            showAddDialog?.let { w ->
                AddWidgetDialog(w,
                    onDismiss = { showAddDialog = null },
                    onConfirm = { showAddDialog = null; pinWidget(context, w) }
                )
            }
            showDetail?.let { w ->
                DetailBottomSheet(w,
                    onDismiss = { showDetail = null },
                    onAdd = { showDetail = null; showAddDialog = w }
                )
            }
        }
    }
}

// ===== LIQUID GLASS COMPONENTS (Apple WWDC25 -> Android Compose) =====

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTopBar(query: String, onQueryChange: (String) -> Unit) {
    // Liquid glass top bar: .glassEffect(.regular) + .backgroundExtensionEffect style
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        color = Color.White.copy(alpha = 0.72f),
        tonalElevation = 0.dp,
        shadowElevation = 8.dp
    ) {
        Box(modifier = Modifier.background(
            Brush.linearGradient(listOf(Color.White.copy(alpha=0.35f), Color.White.copy(alpha=0.08f)))
        ).padding(start=16.dp, end=16.dp, top=12.dp, bottom=12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF6750A4), Color(0xFF0D9488))))
                    , contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Widgets, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier=Modifier.weight(1f)) {
                    Text("Widgetfy", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp))
                    Text("Liquid Glass • 26 Widgets", style = MaterialTheme.typography.labelSmall, color = Color(0xFF6750A4), fontWeight=FontWeight.Bold)
                }
                IconButton(onClick = {}) { Icon(Icons.Filled.Notifications, null, tint = Color(0xFF6750A4)) }
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE8DEF8)), contentAlignment = Alignment.Center) {
                    Text("V", color = Color(0xFF6750A4), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun LiquidGlassBottomBar() {
    // GlassEffectContainer: groups nav items with shared morphing spacing(like Apple WWDC25)
    Box(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.82f),
            shadowElevation = 12.dp,
            tonalElevation = 0.dp
        ) {
            Box(modifier = Modifier.background(Brush.linearGradient(listOf(Color.White.copy(alpha=0.45f), Color.White.copy(alpha=0.12f))))) {
                NavigationBar(containerColor = Color.Transparent, tonalElevation = 0.dp, modifier=Modifier.height(64.dp)) {
                    NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Filled.Home, null) }, label = { Text("Home", fontSize = 11.sp) })
                    NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.Category, null) }, label = { Text("Browse", fontSize = 11.sp) })
                    NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.Favorite, null) }, label = { Text("Fav", fontSize = 11.sp) })
                    NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Filled.Settings, null) }, label = { Text("Settings", fontSize = 11.sp) })
                }
            }
        }
    }
}

@Composable
fun GlassStatsRow(total: Int, active: Int, rating: String) {
    GlassEffectContainer(spacing=10.dp) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            GlassStatCard("$total", "Widgets", Icons.Filled.Widgets, listOf(Color(0xFF6750A4), Color(0xFF9C88FF)), Modifier.weight(1f))
            GlassStatCard("$active", "Active", Icons.Filled.CheckCircle, listOf(Color(0xFF0D9488), Color(0xFF5BD8CC)), Modifier.weight(1f))
            GlassStatCard(rating, "Rating", Icons.Filled.Star, listOf(Color(0xFFE11D48), Color(0xFFFB7185)), Modifier.weight(1f))
        }
    }
}

@Composable
fun GlassStatCard(value: String, label: String, icon: ImageVector, gradient: List<Color>, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent), elevation = CardDefaults.cardElevation(4.dp)) {
        Box(modifier = Modifier.background(Brush.linearGradient(gradient)).padding(14.dp).fillMaxWidth()) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).clip(RoundedCornerShape(10.dp)).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(label, color = Color.White.copy(alpha = 0.9f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
                Text(value, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun LiquidGlassFeaturedCard(onAdd: () -> Unit, onPreview: () -> Unit) {
    Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF6750A4), Color(0xFF0D9488), Color(0xFFE11D48))))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(shape = RoundedCornerShape(99.dp), color = Color.White.copy(alpha = 0.18f)) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF00FF94)))
                            Spacer(Modifier.width(6.dp))
                            Text("NEW • 26 LIQUID GLASS WIDGETS", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Liquid Glass\nfor Home Screen", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 24.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("iOS 26 Liquid Glass → Android\nTranslucent • Refractive • Premium", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, lineHeight = 14.sp)
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onAdd, shape = RoundedCornerShape(99.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF6750A4)), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                            Icon(Icons.Filled.Add, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Add Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        OutlinedButton(onClick = onPreview, shape = RoundedCornerShape(99.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)) {
                            Icon(Icons.Filled.PlayArrow, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Demo", fontSize = 12.sp)
                        }
                    }
                }
                Box(
                    modifier = Modifier.size(92.dp).clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.15f)).border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(TimeUtils.currentTime(), color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Spacer(Modifier.height(2.dp))
                        Text(TimeUtils.currentDayMonth(), color = Color.White.copy(alpha = 0.8f), fontSize = 9.sp)
                        Spacer(Modifier.height(6.dp))
                        Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Widgets, null, tint = Color(0xFF6750A4), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrendingRow() {
    val tags = listOf("Trending 🔥", "New ✨", "Popular ⭐", "Clock", "Productivity", "Health", "Liquid Glass")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tags.size) { i ->
            Surface(shape = RoundedCornerShape(99.dp), color = if (i == 0) Color(0xFF6750A4) else Color.White.copy(alpha=0.78f), shadowElevation = 2.dp) {
                Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(tags[i], color = if (i == 0) Color.White else Color(0xFF6750A4), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun CategoryFilterRow(categories: List<String>, selected: String, onSelect: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories.size) { idx ->
            val cat = categories[idx]
            val isSelected = cat == selected
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(cat) },
                label = { Text(cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                leadingIcon = if (isSelected) ({ Icon(Icons.Filled.Check, null, Modifier.size(16.dp)) }) else null,
                shape = RoundedCornerShape(99.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF6750A4),
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White,
                    containerColor = Color.White.copy(alpha=0.75f),
                    labelColor = Color(0xFF49454F)
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color.White.copy(alpha=0.5f),
                    selectedBorderColor = Color(0xFF6750A4),
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.dp
                )
            )
        }
    }
}

@Composable
fun GlassResultBar(count: Int, onClear: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = Color.White.copy(alpha=0.65f), shadowElevation = 2.dp) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal=12.dp, vertical=8.dp).fillMaxWidth()) {
            Text("$count widgets", style = MaterialTheme.typography.labelMedium, color = Color(0xFF6750A4), fontWeight=FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier.height(1.dp).weight(1f).background(Color(0xFFE8DEF8)))
            Spacer(Modifier.width(8.dp))
            AssistChip(onClick = onClear, label = { Text("Clear", fontSize=11.sp) }, leadingIcon = { Icon(Icons.Filled.Clear, null, Modifier.size(12.dp)) }, colors=AssistChipDefaults.assistChipColors(containerColor=Color.White))
        }
    }
}

@Composable
fun LiquidGlassWidgetCard(widget: WidgetInfo, onAdd: () -> Unit, onDetail: () -> Unit) {
    val isWide = widget.size == BentoSize.Wide || widget.size == BentoSize.Hero
    // Liquid Glass card: translucent surface + gradient header + glass border
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth().clickable { onDetail() },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha=0.82f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.background(Brush.linearGradient(listOf(Color.White.copy(alpha=0.28f), Color.White.copy(alpha=0.06f))))) {
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth().height(if (isWide) 110.dp else 96.dp)
                        .clip(RoundedCornerShape(topStart=24.dp, topEnd=24.dp))
                        .background(Brush.linearGradient(widget.gradient))
                        .padding(14.dp)
                ) {
                    Row(modifier = Modifier.align(Alignment.TopStart).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(widget.icon, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(widget.category.uppercase(), color = Color.White.copy(alpha = 0.9f), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                Text(widget.stats, color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                            }
                        }
                        if (widget.isNew) {
                            Surface(shape = RoundedCornerShape(99.dp), color = Color(0xFFE11D48)) {
                                Text("NEW", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        } else if (widget.isPopular) {
                            Surface(shape = RoundedCornerShape(99.dp), color = Color.White.copy(alpha = 0.2f)) {
                                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Star, null, tint = Color.White, modifier = Modifier.size(10.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("HOT", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                        Text(widget.previewText, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(widget.name, color = Color.White.copy(alpha = 0.9f), fontSize = 10.sp)
                    }
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd).size(80.dp).clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                    )
                }
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(widget.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(4.dp))
                    Text(widget.desc, style = MaterialTheme.typography.bodySmall, color = Color(0xFF49454F), maxLines = 2, lineHeight = 14.sp, overflow = TextOverflow.Ellipsis, modifier = Modifier.heightIn(min = 28.dp))
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = onAdd,
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(99.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
                        ) {
                            Icon(Icons.Filled.Add, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Add", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = onDetail,
                            modifier = Modifier.height(40.dp),
                            shape = RoundedCornerShape(99.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp),
                            border = BorderStroke(1.dp, Color(0xFFE8DEF8))
                        ) {
                            Icon(Icons.Filled.Visibility, null, Modifier.size(16.dp), tint=Color(0xFF6750A4))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HowToGlassBento() {
    Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha=0.75f), shadowElevation = 4.dp) {
        Row(modifier = Modifier.padding(16.dp).background(Brush.linearGradient(listOf(Color.White.copy(alpha=0.25f), Color.Transparent))).padding(0.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFF6750A4)), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.TouchApp, null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("How to add widgets?", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(Modifier.height(4.dp))
                Text("Long press home → Widgets → Widgetfy → Drag. Or tap Add for instant pin (Android 8+). Liquid glass on every widget!", style = MaterialTheme.typography.bodySmall, color = Color(0xFF49454F))
            }
        }
    }
}

@Composable
fun QuoteGlassBento() {
    val quote = QuoteProvider.ofDay()
    Surface(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth(), color = Color.White.copy(alpha=0.72f), shadowElevation = 4.dp) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE8DEF8)), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.FormatQuote, null, tint = Color(0xFF6750A4), modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Quote of the Day", style = MaterialTheme.typography.labelMedium, color = Color(0xFF6750A4))
                Spacer(Modifier.height(6.dp))
                Text("\"${quote.first}\"", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))
                Spacer(Modifier.height(4.dp))
                Text("— ${quote.second}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF49454F))
            }
        }
    }
}

@Composable
fun FooterGlassBento() {
    Surface(shape = RoundedCornerShape(16.dp), color = Color.White.copy(alpha=0.70f), shadowElevation = 2.dp) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Made with ❤️ in India • Liquid Glass Edition", style = MaterialTheme.typography.labelMedium, color = Color(0xFF49454F), fontWeight=FontWeight.Bold)
            Text("26 Widgets • Liquid Glass on All • No Ads • Free Forever", style = MaterialTheme.typography.bodySmall, color = Color(0xFF49454F))
            Spacer(Modifier.height(8.dp))
            Text("com.widgetfy.com • v3.0.0 • Liquid Glass + Bento", style = MaterialTheme.typography.labelSmall, color = Color(0xFF6750A4))
        }
    }
}

@Composable
fun AddWidgetDialog(widget: WidgetInfo, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(Brush.linearGradient(widget.gradient)), contentAlignment = Alignment.Center) { Icon(widget.icon, null, tint = Color.White) } },
        title = { Text("Add ${widget.name}?") },
        text = { Text("'${widget.name}' ko home screen par pin karega. Launcher supported hona chahiye (Android 8+). Nahi toh long press home → Widgets se manual add karo. Liquid glass design inside!") },
        confirmButton = { Button(onClick = onConfirm, shape = RoundedCornerShape(99.dp)) { Text("Add to Home") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        shape = RoundedCornerShape(24.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomSheet(widget: WidgetInfo, onDismiss: () -> Unit, onAdd: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) {
        Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(widget.gradient)).padding(16.dp)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                            Icon(widget.icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(widget.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(widget.category + " • " + widget.stats, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(widget.previewText, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(widget.desc, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onAdd, modifier = Modifier.weight(1f), shape = RoundedCornerShape(99.dp)) {
                    Icon(Icons.Filled.Add, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Add to Home")
                }
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(99.dp)) {
                    Text("Close")
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Features", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(8.dp))
            Text("• Real working widget with Liquid Glass background\n• Updates automatically every minute\n• Resizable & reconfigurable\n• Light/Dark theme aware • Glass borders\n• Battery optimized (WorkManager)", style = MaterialTheme.typography.bodySmall, color = Color(0xFF49454F), lineHeight = 18.sp)
            Spacer(Modifier.height(32.dp))
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
            Toast.makeText(context, "Pin not supported on this launcher. Long press Home → Widgets", Toast.LENGTH_LONG).show()
        }
    } else {
        Toast.makeText(context, "Android 8+ required. Long press Home → Widgets → Widgetfy", Toast.LENGTH_LONG).show()
    }
}

class PinResultReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: android.content.Context, intent: Intent) {
        Toast.makeText(context, "Widget added! Check home screen ✨", Toast.LENGTH_SHORT).show()
    }
}
