package com.bangkit.capstone.fitness.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bangkit.capstone.fitness.R

fun showOKDialog(context: Context, title: String, message: String) {
    AlertDialog.Builder(context).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(context.getString(R.string.label_positive_reply)) { dialog, _ ->
            dialog.dismiss()
        }
    }.create().show()
}
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}
fun View.show() {
    visibility = View.VISIBLE
}