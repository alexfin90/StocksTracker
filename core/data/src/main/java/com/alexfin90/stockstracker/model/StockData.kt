package com.alexfin90.stockstracker.model

//Static 25 Stocks data
data class StockData(
    val symbol: String,
    val name: String,
    val description: String,
    val basePriceUsd: Double
)

val STOCK_CATALOG = listOf(
    StockData("AAPL", "Apple Inc.", "Consumer electronics and software company.", 178.50),
    StockData("GOOG", "Alphabet Inc.", "Search, ads, cloud, and internet services.", 141.80),
    StockData("TSLA", "Tesla Inc.", "Electric vehicles and energy systems.", 245.30),
    StockData("AMZN", "Amazon.com Inc.", "E-commerce and cloud computing.", 185.60),
    StockData("MSFT", "Microsoft Corp.", "Software, cloud, and enterprise services.", 420.10),
    StockData("NVDA", "NVIDIA Corp.", "GPUs and AI computing hardware.", 880.50),
    StockData("META", "Meta Platforms Inc.", "Social platforms and immersive products.", 505.75),
    StockData("NFLX", "Netflix Inc.", "Streaming entertainment platform.", 625.40),
    StockData("AMD", "Advanced Micro Devices", "Semiconductor company for CPUs and GPUs.", 165.20),
    StockData("INTC", "Intel Corp.", "Semiconductor and processor manufacturer.", 31.45),
    StockData("ORCL", "Oracle Corp.", "Enterprise software and cloud services.", 125.80),
    StockData("CRM", "Salesforce Inc.", "Customer relationship management software.", 275.90),
    StockData("ADBE", "Adobe Inc.", "Creative, document, and marketing software.", 530.60),
    StockData("PYPL", "PayPal Holdings Inc.", "Digital payments platform.", 63.15),
    StockData("UBER", "Uber Technologies Inc.", "Mobility and delivery platform.", 78.40),
    StockData("SPOT", "Spotify Technology", "Music and podcast streaming platform.", 295.80),
    StockData("SQ", "Block Inc.", "Payments and fintech ecosystem.", 82.30),
    StockData("SHOP", "Shopify Inc.", "Commerce platform for merchants.", 78.65),
    StockData("SNAP", "Snap Inc.", "Social media and camera company.", 11.20),
    StockData("PINS", "Pinterest Inc.", "Visual discovery platform.", 35.70),
    StockData("ROKU", "Roku Inc.", "Streaming devices and TV platform.", 62.85),
    StockData("TWLO", "Twilio Inc.", "Communication APIs platform.", 60.40),
    StockData("ZM", "Zoom Video Comm.", "Video communication software.", 68.90),
    StockData("DOCU", "DocuSign Inc.", "Electronic signature platform.", 58.75),
    StockData("PLTR", "Palantir Technologies", "Data analytics software company.", 24.50),
)