package com.happy.limiter

import com.happy.alarms.Alarm
import kotlinx.coroutines.*
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

class AlarmLimiter(
    private val time: Long,
    private val unit: TimeUnit,
    private val alarm: Alarm
) {

    private var lastAlarmTime = DateTime().withYear(1996).withMonthOfYear(12).withDayOfMonth(14)

    private val retention = ArrayList<String>()

    private var autoSendJob: Job = GlobalScope.launch {}


    fun restrictedDelivery(title: String, content: String, tos: List<String>) {
        autoSendJob.cancel()
        val now = DateTime.now()
        retention.add("${now.toString("HH:mm:ss.SSS")}: [$title] $content\n")
        if (lastAlarmTime.plusSeconds(unit.toSeconds(time).toInt()).isBefore(now)) {
            lastAlarmTime = now
            alarm.send(title, retention.toString(), tos)
            retention.clear()
        } else {
            autoSendJob = autoSend(title, retention.toString(), tos)
        }
    }

    fun restrictedDelivery(title: String, content: String, to: String) {
        restrictedDelivery(title, content, listOf(to))
    }

    private fun autoSend(title: String, content: String, tos: List<String>): Job {
        return GlobalScope.launch {
            delay(unit.toMillis(time))
            if (retention.isEmpty()) {
                return@launch
            }
            alarm.send(title, content, tos)
            retention.clear()
        }
    }
}