package com.djs.controlfreaks.shared

import kotlin.system.exitProcess

object Logger {

    fun Print(s: String) {
        println("[INFO]: $s")
    }

    fun Error(s: String) {
        println("[ERROR]: $s")
    }

    fun Fatal(s: String) {
        println("[FATAL ERROR]: $s")
        exitProcess(1)
    }
}