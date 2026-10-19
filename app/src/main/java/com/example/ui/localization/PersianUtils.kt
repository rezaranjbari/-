package com.example.ui.localization

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object PersianUtils {

    private val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

    fun toPersianDigits(text: String): String {
        val sb = StringBuilder()
        for (char in text) {
            if (char in '0'..'9') {
                sb.append(persianDigits[char - '0'])
            } else {
                sb.append(char)
            }
        }
        return sb.toString()
    }

    fun formatPrice(price: Double, language: AppLanguage = AppLanguage.PERSIAN): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val pattern = when {
            price >= 1000.0 -> "$#,##0.00"
            price >= 1.0 -> "$#,##0.00"
            price >= 0.0001 -> "$0.0000"
            else -> "$0.000000"
        }
        val df = DecimalFormat(pattern, symbols)
        val formatted = df.format(price)
        return if (language == AppLanguage.PERSIAN) {
            toPersianDigits(formatted)
        } else {
            formatted
        }
    }

    fun formatPercentage(change: Double, language: AppLanguage = AppLanguage.PERSIAN): String {
        val sign = if (change > 0) "+" else ""
        val df = DecimalFormat("0.00", DecimalFormatSymbols(Locale.US))
        val text = "$sign${df.format(change)}%"
        return if (language == AppLanguage.PERSIAN) {
            toPersianDigits(text)
        } else {
            text
        }
    }

    fun formatCompactUsd(amount: Double, language: AppLanguage = AppLanguage.PERSIAN): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val formatted = when {
            amount >= 1_000_000_000_000.0 -> "${DecimalFormat("#,##0.00", symbols).format(amount / 1_000_000_000_000.0)} T$"
            amount >= 1_000_000_000.0 -> "${DecimalFormat("#,##0.00", symbols).format(amount / 1_000_000_000.0)} B$"
            amount >= 1_000_000.0 -> "${DecimalFormat("#,##0.00", symbols).format(amount / 1_000_000.0)} M$"
            amount >= 1_000.0 -> "${DecimalFormat("#,##0.00", symbols).format(amount / 1_000.0)} K$"
            else -> "$${DecimalFormat("#,##0.00", symbols).format(amount)}"
        }
        return if (language == AppLanguage.PERSIAN) {
            toPersianDigits(formatted)
                .replace("T$", " تریلیون دلار")
                .replace("B$", " میلیارد دلار")
                .replace("M$", " میلیون دلار")
                .replace("K$", " هزار دلار")
        } else {
            formatted
        }
    }

    fun formatTimeAgo(timestampMs: Long, language: AppLanguage = AppLanguage.PERSIAN): String {
        val diffMs = System.currentTimeMillis() - timestampMs
        val seconds = (diffMs / 1000).coerceAtLeast(0)
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return if (language == AppLanguage.PERSIAN) {
            when {
                seconds < 30 -> "لحظاتی پیش"
                seconds < 60 -> "${toPersianDigits(seconds.toString())} ثانیه پیش"
                minutes < 60 -> "${toPersianDigits(minutes.toString())} دقیقه پیش"
                hours < 24 -> "${toPersianDigits(hours.toString())} ساعت پیش"
                else -> "${toPersianDigits(days.toString())} روز پیش"
            }
        } else {
            when {
                seconds < 30 -> "Just now"
                seconds < 60 -> "${seconds}s ago"
                minutes < 60 -> "${minutes}m ago"
                hours < 24 -> "${hours}h ago"
                else -> "${days}d ago"
            }
        }
    }
}
