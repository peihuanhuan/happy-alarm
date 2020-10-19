package com.happy

import com.happy.alarms.EmailAlarm
import com.happy.limiter.AlarmLimiter
import com.happy.watcher.ThresholdWatcher
import java.util.concurrent.TimeUnit


fun main() {

    // 定义一种报警方式，配个默认的邮箱
    val emailAlarm = EmailAlarm.buildQQDefaultEmailSender()
    // 定义一个报警限流器， 每五秒最多通知一次。 如果五秒内多次报警，攒着等待下一个五秒一起发送
    val limiter = AlarmLimiter(5, TimeUnit.SECONDS, emailAlarm)
    // 设置某业务值报警阈值为 10
    val threshold = 10
    val watcher = ThresholdWatcher(threshold.toLong()) {
        limiter.restrictedDelivery(
            "报警主题", "超过设定阈值 $threshold 当前值为$it",
            listOf("475616534@qq.com", "1678167835@qq.com")
        )
    }

    // 业务数据源
    val data = MonitorDataImpl()
    // 添加一个监听者
    data.addObserver(watcher)

    // 模拟业务值变化，从 0 到 100
    repeat(100) {
        data.setValue(it.toLong())
        if (it % threshold == 0) {
            // 暂时等一会再变化
            Thread.sleep(1200)
        }
    }

    Thread.sleep(10000)
}