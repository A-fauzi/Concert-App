package com.example.concert_app.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.concert_app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Libs {
    fun clearText(inputText: EditText) {
        inputText.text?.clear()
    }

    fun dialogErrors(
        layoutInflater: LayoutInflater,
        context: Context,
        dialogText: String,
        rawRes: Int
    ) {
        val dialog = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_rounded)
        val dialogView: View = layoutInflater.inflate(R.layout.dialog_errors, null)
        val textDialog = dialogView.findViewById<TextView>(R.id.tv_dialog_error)
        val animationDialog = dialogView.findViewById<LottieAnimationView>(R.id.animationDialog)

        animationDialog.setAnimation(rawRes)
        textDialog.text = dialogText

        dialog.setView(dialogView)
        dialog.setTitle("Uppss..")
        dialog.show()

    }

}