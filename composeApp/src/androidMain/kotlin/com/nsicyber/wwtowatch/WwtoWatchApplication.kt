package com.nsicyber.wwtowatch

import android.app.Application
import di.initKoin

class WwtoWatchApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }


}