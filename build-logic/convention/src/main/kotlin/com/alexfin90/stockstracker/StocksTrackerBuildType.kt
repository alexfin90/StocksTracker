package com.alexfin90.stockstracker

/**
 * This is shared between modules to provide configurations type safety.
 */
enum class StocksTrackerBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}