package com.amadeus.android.demo

import android.app.Application
import com.amadeus.android.Amadeus
import com.jakewharton.threetenabp.AndroidThreeTen

class SampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        amadeus = Amadeus.Builder(this)
            .setClientId(getString(R.string.amadeus_client_id))
            .setClientSecret(getString(R.string.amadeus_client_secret))
            .setCustomAppIdAndVersion("com.amadeus.android.demo", "1.0.0")
            .setLogLevel(Amadeus.Builder.LogLevel.BODY)
            .setHostName(Amadeus.Builder.Hosts.TEST)
            .build()
    }

    companion object {
        lateinit var amadeus: Amadeus
    }
}