package com.happy.watcher

import java.util.*

class ThresholdWatcher(
    private val threshold: Long,
    private val limiter: (arg: Any) -> Unit
) : Watcher() {

    override fun update(o: Observable?, arg: Any) {
        if (arg !is Long) {
            return
        }
        if (arg > threshold) {
            limiter(arg)
        }
    }

}

