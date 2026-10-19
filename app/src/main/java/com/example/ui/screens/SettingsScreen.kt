package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RiskWarningBanner
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.RadarBg
import com.example.ui.theme.RadarBorder
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.RadarSurface
import com.example.ui.theme.RadarSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .testTag("settings_screen")
            .fillMaxSize()
            .background(RadarBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = RadarCyan,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Strings.settingsHeader(currentLanguage),
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // 1. Language Preference Card (Persian Default vs English)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RadarSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RadarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = RadarCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Strings.languageSetting(currentLanguage),
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Persian Option (Default)
                    LanguageRow(
                        label = Strings.persianOption(currentLanguage),
                        subLabel = "چیدمان راست‌به‌چپ (RTL) و فونت وزیرمتن فارسی",
                        isSelected = currentLanguage == AppLanguage.PERSIAN,
                        onClick = { onLanguageChange(AppLanguage.PERSIAN) }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 0.5.dp,
                        color = RadarBorder
                    )

                    // English Option
                    LanguageRow(
                        label = Strings.englishOption(currentLanguage),
                        subLabel = "Left-to-Right (LTR) with standard international terms",
                        isSelected = currentLanguage == AppLanguage.ENGLISH,
                        onClick = { onLanguageChange(AppLanguage.ENGLISH) }
                    )
                }
            }
        }

        // 2. Data Source Architecture & Reliability Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RadarSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RadarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            tint = RadarCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.PERSIAN) "معماری تجمیع داده‌ها از ۳ منبع" else "Multi-Source Data Architecture",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (currentLanguage == AppLanguage.PERSIAN)
                            "• بایننس (Binance API): استعلام مستقیم قیمت‌های اسپات صرافی، دفتر سفارشات و کندل‌های OHLCV\n• کوین‌گکو (CoinGecko API): محاسبه ارزش کل بازار، حجم کل جهانی و رتبه‌بندی نقدینگی\n• کوین‌کپ (CoinCap API): مقایسه و تایید قیمت به عنوان مرجع مستقل برای کشف هرگونه مغایرت\n• اخبار موثق (CryptoCompare): نمایش تیترهای تاییدشده همراه با منبع و زمان رسمی"
                        else
                            "• Binance API: Real-time exchange spot prices, orderbook depth & OHLCV candles\n• CoinGecko API: Market capitalization, aggregate volumes and global rank\n• CoinCap API: Independent benchmark reference for discrepancy cross-checks\n• CryptoCompare: Verified news feeds with direct attribution and timestamps",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // 3. Security & Non-Custodial Architecture Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RadarSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RadarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = BullishGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.PERSIAN) "امنیت و حفظ حریم خصوصی" else "Security & Privacy",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (currentLanguage == AppLanguage.PERSIAN)
                            "این اپلیکیشن کاملاً مستقل و تحلیلی است. هیچ‌گونه تراکنش مالی، دسترسی به ولت، کلید خصوصی، کلمات بازیابی یا اتصال به حساب‌های صرافی در این سامانه وجود ندارد. داده‌ها تنها به منظور تجزیه و تحلیل ریاضی و بصری از وب‌سرویس‌های عمومی استعلام می‌شوند."
                        else
                            "This application is purely analytical. It never executes trades or requests exchange credentials, private keys, or seed phrases. Data is securely read from official public endpoints.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // 4. Installation & Preview Guide for Phone
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RadarSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, RadarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = RadarCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.PERSIAN) "نحوه پیش‌نمایش و نصب روی گوشی" else "How to Preview & Install on Phone",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (currentLanguage == AppLanguage.PERSIAN)
                            "۱. پیش‌نمایش زنده: در مرورگر رایانه می‌توانید امولاتور اندروید را به صورت استریم مشاهده و آزمایش نمایید.\n۲. نصب روی گوشی: از منوی بالای محیط AI Studio گزینه Export یا دانلود فایل APK/AAB را انتخاب کنید.\n۳. انتقال و نصب: فایل APK تولید شده را به گوشی اندروید خود منتقل نموده و گزینه Install را تایید نمایید."
                        else
                            "1. Live Preview: View and interact with the streaming Android emulator directly in your browser.\n2. Export APK: In AI Studio menu, select Export or Download APK.\n3. Install on Device: Transfer the APK file to your Android phone and install it directly.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // 5. Mandatory Investment Risk Warning
        item {
            RiskWarningBanner(language = currentLanguage)
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun LanguageRow(
    label: String,
    subLabel: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subLabel,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = RadarCyan,
                unselectedColor = RadarBorder
            )
        )
    }
}
