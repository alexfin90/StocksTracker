package com.alexfin90.stockstracker.logging

import timber.log.Timber

object StocksTrackerDebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        return "${super.createStackElementTag(element)}:${element.lineNumber}"
    }
}
