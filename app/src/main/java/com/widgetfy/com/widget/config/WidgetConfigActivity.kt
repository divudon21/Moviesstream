package com.widgetfy.com.widget.config

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.widgetfy.com.ui.theme.WidgetfyTheme

class WidgetConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appWidgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        setContent {
            WidgetfyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var style by remember { mutableStateOf("Dark") }
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Configure Widget", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(16.dp))
                        Text("Choose widget style")
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilterChip(selected = style=="Dark", onClick = {style="Dark"}, label={Text("Dark")})
                            FilterChip(selected = style=="Light", onClick = {style="Light"}, label={Text("Light")})
                            FilterChip(selected = style=="Transparent", onClick = {style="Transparent"}, label={Text("Transparent")})
                        }
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = {
                            val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                            setResult(Activity.RESULT_OK, resultValue)
                            finish()
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text("Add Widget to Home")
                        }
                        OutlinedButton(onClick = { setResult(Activity.RESULT_CANCELED); finish() }, modifier = Modifier.fillMaxWidth().padding(top=8.dp)) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
