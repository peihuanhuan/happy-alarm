package com.happy.alarms

interface Alarm {
    fun send(title: String, content: String, tos: List<String>)
    fun send(title: String, content: String, to: String) {
        send(title, content, listOf(to))
    }
}