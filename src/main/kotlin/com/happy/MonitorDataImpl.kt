package com.happy

import java.util.*

class MonitorDataImpl : Observable(), MonitorData {

    private var data: Long = 0

    override fun setValue(data: Long) {
        this.data = data;
        setChanged()
        notifyObservers(data)
    }
}