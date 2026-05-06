package com.dualclock.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import java.util.Timer
import java.util.TimerTask

class ClockUpdateService : Service() {

    private var timer: Timer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val manager = AppWidgetManager.getInstance(this@ClockUpdateService)
                val ids = manager.getAppWidgetIds(
                    ComponentName(this@ClockUpdateService, DualClockWidget::class.java)
                )
                ids.forEach { id ->
                    DualClockWidget.updateWidget(this@ClockUpdateService, manager, id)
                }
            }
        }, 0, 60_000L) // update every minute
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
