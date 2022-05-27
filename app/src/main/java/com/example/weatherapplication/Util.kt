package com.example.weatherapplication

import android.icu.text.DecimalFormat
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).into(this)
}

fun LottieAnimationView.setAnimatedIcon(mainDescription: String, additionalDescription: String) {
    this.setAnimation(
        when (mainDescription) {
            "Clouds" ->
                when (additionalDescription) {
                    "few clouds" -> R.raw.clouds_with_sun
                    else -> R.raw.scattered_clouds
                }
            "Atmosphere" -> R.raw.fog
            "Snow" -> R.raw.snow
            "Rain" -> when {
                additionalDescription.contains("shower rain") -> {
                    R.raw.shower_rain
                }
                additionalDescription == "freezing rain" -> {
                    R.raw.snow
                }
                else -> R.raw.rain
            }
            "Drizzle" -> R.raw.rain
            "Thunderstorm" -> R.raw.thunderstorm
            else -> R.raw.clear_sky
        }
    )
    this.playAnimation()
    this.repeatCount = LottieDrawable.INFINITE
}

fun getDay(time: Int, timeZone: String): String {
    val zone = ZoneId.of(timeZone)
    val instant = Instant.ofEpochSecond(time.toLong()).atZone(zone)
    return instant.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
}

fun formatedDouble(number:Double) = DecimalFormat("#0.00").format(number)