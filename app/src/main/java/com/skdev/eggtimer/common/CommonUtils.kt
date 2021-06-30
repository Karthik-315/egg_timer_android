package com.skdev.quizapp.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate

fun Activity.switchTheme(){
    // AppCompatDelegate.getDefaultNightMode() returns 1 if current theme is light or 2 if dark.
    var currentTheme = AppCompatDelegate.getDefaultNightMode()

    if(currentTheme == 1)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) //Dark Mode
    else
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //Light Mode
}

fun Activity.setFullscreen(){
    // Set fullscreen mode based on SDK version.
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        var controller = window.insetsController

        if(controller != null){
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    else{
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun Activity.goBack(context: Context, targetActivity: Class<Activity>){
    var parentClass = this.parentActivityIntent?.component!!
    val intent = Intent(context, targetActivity)
    startActivity(intent)
}

fun Activity.toggleView(view: View, _state: String){
    if(_state.toLowerCase() == "disable") {
        view.alpha = 0.3F
        view.isEnabled = false
    }
    else if(_state.toLowerCase() == "enable"){
        view.alpha = 1F
        view.isEnabled = true
    }
}
