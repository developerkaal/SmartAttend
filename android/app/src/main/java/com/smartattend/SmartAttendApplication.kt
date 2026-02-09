package com.smartattend

import android.app.Application
import com.smartattend.data.ServiceLocator
import com.smartattend.data.SessionManager

class SmartAttendApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
        SessionManager.init(this)
    }
}
