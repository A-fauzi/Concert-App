package com.example.concert_app.utils

import android.app.ProgressDialog
import android.content.Context
import android.os.Build.VERSION_CODES.S
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.concert_app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Libs {
    fun clearText(inputText: EditText) {
        inputText.text?.clear()
    }

    fun dialogMessageAnimate(
        layoutInflater: LayoutInflater,
        context: Context,
        dialogText: String,
        rawRes: Int,
        setTitle: String
    ) {
        val dialog = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_rounded)
        val dialogView: View = layoutInflater.inflate(R.layout.dialog_errors, null)
        val textDialog = dialogView.findViewById<TextView>(R.id.tv_dialog_error)
        val animationDialog = dialogView.findViewById<LottieAnimationView>(R.id.animationDialog)

        animationDialog.setAnimation(rawRes)
        textDialog.text = dialogText

        dialog.setView(dialogView)
        dialog.setTitle(setTitle)
        dialog.setNegativeButton("Close") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()

    }

}