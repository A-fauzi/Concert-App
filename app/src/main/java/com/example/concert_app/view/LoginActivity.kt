package com.example.concert_app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.R
import com.example.concert_app.databinding.ActivityLoginBinding
import com.example.concert_app.utils.Libs.clearText
import com.example.concert_app.utils.Libs.dialogErrors
import com.example.concert_app.view.main.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var textFieldEmail: TextInputLayout
    private lateinit var textFieldPass: TextInputLayout
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPass: TextInputEditText
    private lateinit var tvWarnEmail: TextView
    private lateinit var tvWarnPass: TextView
    private lateinit var btnLogin: MaterialButton
    private lateinit var chipToSignUp: Chip
    private lateinit var progressBar: ProgressBar

    private fun initView() {
        textFieldEmail = binding.outlinedTextFieldEmail
        textFieldPass = binding.outlinedTextFieldPassword
        etEmail = binding.etEmailSignIn
        etPass = binding.etPasswordSignIn
        tvWarnEmail = binding.tvWarnEmail
        tvWarnPass = binding.tvWarnPassword
        btnLogin = binding.btnLoginSignIn
        chipToSignUp = binding.tvLinkToSignUp
        progressBar = binding.progressInSignIn
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onStart() {
        super.onStart()

        btnLogin.setOnClickListener {
            setFormEnable(false, R.color.input_disabled)
            signInUser()
            btnLogin.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }

        chipToSignUp.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //========================================== |TextWatcher| ==================================================================
        etEmail.addTextChangedListener(GenericTextWatcher(etEmail))
        etPass.addTextChangedListener(GenericTextWatcher(etPass))
        //========================================== |END| ==================================================================

    }
    private fun signInUser() {
        auth.signInWithEmailAndPassword(
            etEmail.text.toString().trim(),
            etPass.text.toString().trim()
        ).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

                clearText(etEmail)
                clearText(etPass    )

            } else {

                setFormEnable(true, R.color.white)
                dialogErrors(layoutInflater, this, authResult.exception?.localizedMessage!!, R.raw.auth_failure)
                btnLogin.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

            }

        }.addOnFailureListener { authFailure ->

            setFormEnable(true, R.color.white)
            dialogErrors(layoutInflater, this, authFailure.localizedMessage!!, R.raw.auth_failure)
            btnLogin.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

        }

    }

    inner class GenericTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(editable: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (view.id) {
                R.id.et_email_sign_in -> {
                    if (text.contains('@')) {
                        inputValidate(
                            setCompDrawIsCorrect = etEmail,
                            textWarnGone = tvWarnEmail,
                            inputEnableTrue = etPass,
                            setBackBoxTextFieldActive = textFieldPass
                        )
                    } else {
                        inputValidate(
                            setCompDrawNotCorrect = etEmail,
                            textWarnVisible = tvWarnEmail,
                            inputEnableFalse = etPass,
                            setBackBoxTextFieldDisable = textFieldPass,
                            requestFocus = etEmail
                        )

                    }
                }
                R.id.et_password_sign_in -> {
                    if (text.length < 8) {
                        inputValidate(
                            textWarnVisible = tvWarnPass,
                            setCompDrawNotCorrect = etPass,
                            inputEnableFalse = btnLogin,
                            requestFocus = etPass
                        )
                    } else {
                        inputValidate(
                            textWarnGone = tvWarnPass,
                            setCompDrawIsCorrect = etPass,
                            inputEnableTrue = btnLogin
                        )
                    }
                }
            }
        }
        private fun inputValidate(
            setCompDrawIsCorrect: EditText? = null,
            setCompDrawNotCorrect: EditText? = null,
            textWarnVisible: View? = null,
            textWarnGone: View? = null,
            inputEnableTrue: View? = null,
            inputEnableFalse: View? = null,
            setBackBoxTextFieldActive: TextInputLayout? = null,
            setBackBoxTextFieldDisable: TextInputLayout? = null,
            requestFocus: View? = null
        ) {
            setCompDrawIsCorrect?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_correct,
                0
            )
            setCompDrawNotCorrect?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            textWarnVisible?.visibility = View.VISIBLE
            textWarnGone?.visibility = View.INVISIBLE
            inputEnableTrue?.isEnabled = true
            inputEnableFalse?.isEnabled = false
            setBackBoxTextFieldActive?.setBoxBackgroundColorResource(R.color.white)
            setBackBoxTextFieldDisable?.setBoxBackgroundColorResource(R.color.input_disabled)
            requestFocus?.requestFocus()
        }
    }
    private fun setFormEnable(condition: Boolean, setBackBoxColor: Int) {

        etEmail.isEnabled = condition
        textFieldEmail.setBoxBackgroundColorResource(setBackBoxColor)

        etPass.isEnabled = condition
        textFieldPass.setBoxBackgroundColorResource(setBackBoxColor)
    }
}