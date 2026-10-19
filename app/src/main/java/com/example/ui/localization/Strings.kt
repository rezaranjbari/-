package com.example.ui.localization

object Strings {
    // App Brand
    fun appTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "رادار کریپتو پرو" else "Crypto Radar Pro"
    fun appSubtitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سامانه جامع پایش و اسکن چندمنبعی کریپتو" else "Multi-Source Crypto Scanner & Radar"

    // Navigation Tabs
    fun tabMarket(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "داشبورد" else "Dashboard"
    fun tabScanner(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "رادار فرصت‌ها" else "Opportunity Radar"
    fun tabWatchlist(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "دیده‌بان" else "Watchlist"
    fun tabNews(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اخبار و رویدادها" else "News & Events"
    fun tabSettings(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تنظیمات" else "Settings"

    // Market Dashboard
    fun marketPulse(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "نبض بازار کریپتو" else "Crypto Market Pulse"
    fun totalMarketCap(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ارزش کل بازار" else "Total Market Cap"
    fun volume24h(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "حجم معاملات ۲۴ ساعته" else "24h Total Volume"
    fun btcDominance(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سلطه بیت‌کوین (BTC.D)" else "BTC Dominance"
    fun marketRegime(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "وضعیت کلان بازار" else "Market Regime"
    fun topGainers(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "بیشترین صعود ۲۴ ساعته" else "Top Gainers (24h)"
    fun topLosers(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "بیشترین نزول ۲۴ ساعته" else "Top Losers (24h)"
    fun radarPicksQuick(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "فرصت‌های رصد شده امروز" else "Radar Signals Today"
    fun sourcesHealth(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "وضعیت اتصال منابع داده" else "Data Sources Status"
    fun sourceBinance(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "صرافی بایننس (Binance)" else "Binance Exchange"
    fun sourceCoinGecko(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "کوین‌گکو (CoinGecko)" else "CoinGecko Aggregate"
    fun sourceCoinCap(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "کوین‌کپ (CoinCap)" else "CoinCap Benchmark"

    // Scanner
    fun scannerHeader(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اسکنر پیشرفته فرصت‌های کریپتو" else "Advanced Crypto Opportunity Scanner"
    fun searchPlaceholder(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "جستجوی نماد یا نام ارز..." else "Search symbol or coin name..."
    fun timeframeFilter(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تایم‌فریم تحلیل" else "Timeframe"
    fun strategyFilter(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "الگوی رادار" else "Radar Pattern"
    fun allStrategies(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "همه الگوها" else "All Patterns"
    fun stratBreakout(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "شکست مقاومت (Breakout)" else "Breakout Momentum"
    fun stratVolumeSpike(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "جهش غیرعادی حجم" else "Volume Spike"
    fun stratReversal(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "چرخش صعودی (Reversal)" else "Trend Reversal"
    fun stratOversold(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اشباع فروش (Oversold)" else "Oversold Recovery"
    fun stratSqueeze(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "فشردگی باندها (Squeeze)" else "Volatility Squeeze"

    fun tf15m(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "۱۵ دقیقه" else "15m"
    fun tf1h(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "۱ ساعته" else "1h"
    fun tf4h(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "۴ ساعته" else "4h"
    fun tf1d(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "روزانه" else "1D"

    fun radarScore(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "امتیاز رادار" else "Radar Score"
    fun confidenceLevel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "اعتبار سیگنال" else "Signal Confidence"
    fun confHigh(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "بالا (تایید چندگانه)" else "High (Multi-Confirmed)"
    fun confMedium(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "متوسط (در حال تایید)" else "Medium (Forming)"
    fun confCaution(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "احتیاط (پرریسک)" else "Caution (Volatile)"

    fun viewAnalysis(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تحلیل جامع و سناریوها" else "View Scenarios & Analysis"
    fun noCandidatesFound(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "هیچ ارزی با فیلترهای انتخابی مطابقت ندارد." else "No assets matched the current filters."

    // Coin Details & Analysis
    fun multiSourcePriceComparison(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "مقایسه زنده قیمت از ۳ منبع مستقل" else "Live Multi-Source Price Comparison"
    fun exchangePrice(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "قیمت صرافی (Orderbook)" else "Exchange Spot Price"
    fun aggregatePrice(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "قیمت میانگین وزنی" else "Weighted Aggregate Price"
    fun benchmarkPrice(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "قیمت مرجع مستقل" else "Independent Benchmark"
    fun discrepancyLabel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "میزان مغایرت قیمتی" else "Price Discrepancy"
    fun discrepancyWarning(percent: String, lang: AppLanguage) =
        if (lang == AppLanguage.PERSIAN)
            "هشدار: مغایرت قیمتی $percent بین صرافی و بازار جهانی مشاهده شده است. ممکن است به دلیل اختلاف عمق دفتر سفارشات یا تأخیر شبکه باشد."
        else
            "Warning: Price discrepancy of $percent detected across data sources. Likely due to exchange spread or order book depth."

    fun singleSourceWarning(lang: AppLanguage) =
        if (lang == AppLanguage.PERSIAN)
            "هشدار منبع داده: در حال حاضر فقط یک منبع پاسخگو است؛ مقایسه تاییدیه چندگانه موقتاً در دسترس نیست."
        else
            "Single Source Warning: Currently only one provider is responding; multi-source cross-verification is temporarily unavailable."

    fun technicalIndicatorsTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "شاخص‌های تکنیکال محاسبه شده" else "Calculated Technical Indicators"
    fun rsiLabel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "شاخص قدرت نسبی (RSI-14)" else "Relative Strength (RSI-14)"
    fun macdLabel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "مکدی (MACD 12,26,9)" else "MACD (12,26,9)"
    fun atrLabel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "دامنه نوسان واقعی (ATR-14)" else "Average True Range (ATR-14)"
    fun volumeRatioLabel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "نسبت حجم به میانگین ۷ روزه" else "Volume / 7d Average Ratio"
    fun emaTrends(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "آرایش میانگین‌های متحرک نمایی" else "EMA Trend Alignment (9, 21, 50, 200)"

    // Scenarios
    fun scenariosHeader(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سناریوهای محتمل و شروط ابطال" else "Potential Scenarios & Invalidation"
    fun bullishScenario(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سناریوی صعودی (Bullish)" else "Bullish Scenario"
    fun bearishScenario(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سناریوی نزولی (Bearish)" else "Bearish Scenario"
    fun neutralScenario(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سناریوی رنج و خنثی (Consolidation)" else "Neutral / Range Scenario"

    fun entryZone(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "محدوده ورود بهینه" else "Optimal Entry Zone"
    fun target1(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "هدف سود اول (TP1)" else "Target 1 (TP1)"
    fun target2(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "هدف سود دوم (TP2)" else "Target 2 (TP2)"
    fun stopLoss(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "حد ضرر پیشنهادی (Stop Loss)" else "Suggested Stop Loss (SL)"
    fun invalidationLevel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "سطح ابطال سناریو" else "Invalidation Level"
    fun riskReward(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "نسبت ریسک به ریوارد (R/R)" else "Risk / Reward (R/R)"

    fun deepReportHeader(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "گزارش تحلیلی تفصیلی رادار" else "Comprehensive Radar Thesis Report"

    // Watchlist
    fun watchlistHeader(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "ارزهای تحت نظر (دیده‌بان)" else "Watchlist"
    fun addToWatchlist(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "افزودن به دیده‌بان" else "Add to Watchlist"
    fun removeFromWatchlist(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "حذف از دیده‌بان" else "Remove from Watchlist"
    fun emptyWatchlist(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "دیده‌بان شما در حال حاضر خالی است. ارزهای مورد علاقه خود را اضافه کنید." else "Your watchlist is empty. Add coins to track them closely."

    // News
    fun newsHeader(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "رویدادها و اخبار موثق کریپتو" else "Verified Crypto News & Events"
    fun readFullArticle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "مشاهده متن کامل خبر در منبع" else "Read Full Article at Source"
    fun newsSourceLabel(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "منبع خبر:" else "Source:"

    // Settings & Risk
    fun settingsHeader(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تنظیمات رادار کریپتو پرو" else "Settings & Preferences"
    fun languageSetting(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "زبان برنامه (Language)" else "App Language"
    fun persianOption(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "فارسی (پیش‌فرض)" else "Persian (Default)"
    fun englishOption(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "انگلیسی (English)" else "English"
    fun autoRefreshSetting(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "به‌روزرسانی خودکار نرخ‌ها" else "Auto-Refresh Rate"
    fun refreshInterval30s(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "هر ۳۰ ثانیه" else "Every 30s"
    fun refreshInterval1m(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "هر ۱ دقیقه" else "Every 1 min"
    fun refreshInterval5m(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "هر ۵ دقیقه" else "Every 5 min"
    fun manualRefresh(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "به‌روزرسانی دستی" else "Manual Refresh"

    // Mandatory Risk Disclaimer
    fun riskWarningTitle(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "هشدار مهم ریسک سرمایه‌گذاری" else "Crucial Investment Risk Warning"
    fun riskWarningBody(lang: AppLanguage) =
        if (lang == AppLanguage.PERSIAN)
            "بازار ارزهای دیجیتال دارای نوسانات شدید و غیرقابل پیش‌بینی است. تمامی تحلیل‌ها، سطوح ورود، حد ضرر و امتیازات ارائه شده در رادار کریپتو پرو صرفاً خروجی محاسبات الگوریتمی و اندیکاتورهای ریاضی هستند و به هیچ عنوان سیگنال قطعی، تضمین سود یا توصیه خرید و فروش تلقی نمی‌شوند. این اپلیکیشن هرگز کلیدهای خصوصی، رمز عبور یا وجوه شما را دریافت نمی‌کند. مسئولیت کامل هرگونه تصمیم‌گیری مالی بر عهده خود کاربر است."
        else
            "Cryptocurrency markets are subject to extreme volatility and risk. All analyses, entry zones, stop losses, and radar scores generated by Crypto Radar Pro are purely algorithmic indicators and mathematical models. They DO NOT constitute financial advice or guaranteed trading signals. This app never requests private keys or funds. Always manage your risk responsibly."

    fun retryButton(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "تلاش مجدد" else "Retry"
    fun loadingData(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "در حال استعلام نرخ‌ها از بایننس، کوین‌گکو و کوین‌کپ..." else "Fetching prices from Binance, CoinGecko & CoinCap..."
    fun offlineModeNote(lang: AppLanguage) = if (lang == AppLanguage.PERSIAN) "حالت آفلاین: نمایش آخرین داده‌های ذخیره شده در حافظه محلی" else "Offline Mode: Showing cached data from local database"
}
