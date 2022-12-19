package com.example.concert_app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.concert_app.service.user.UserApiService
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.R
import com.example.concert_app.databinding.ActivityRegisterBinding
import com.example.concert_app.utils.FirebaseServiceInstance.user
import com.example.concert_app.view.main.MainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    companion object {
        const val TAG = "RegisterActivity"
    }

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var name: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var passwordConfirmation: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var radioGroup: RadioGroup

    private fun initView() {
        name = binding.etNameSignUp
        phone = binding.etPhoneSignUp
        email = binding.etEmailSignUp
        password = binding.etPasswordSignUp
        passwordConfirmation = binding.etPasswordConfirmSignUp
        progressBar = binding.progressInSignup
        radioGroup = binding.enableRadioGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        currentUser()
    }

    private fun currentUser() {
        user.let {
            if (it != null) {
                Log.i(TAG, "user logged in")
                Log.i(TAG, "name: ${it.displayName}")
                Log.i(TAG, "email: ${it.email}")
                Log.i(TAG, "photo profile: ${it.photoUrl}")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.i(TAG, "user not login")
            }
        }
    }

    private fun enableRadioGroup(): String {
        val selectOption: Int = radioGroup.checkedRadioButtonId
        val radioButton: RadioButton = findViewById(selectOption)
        val dataGender = radioButton.text
        Log.d("checkRadio", dataGender.toString())
        return dataGender.toString()
    }

    override fun onStart() {
        super.onStart()

        binding.btnRegisterSignUp.setOnClickListener {
            setFormEnable(false, R.color.input_disabled)
            createUser()
            binding.btnRegisterSignUp.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }

        binding.tvLinkToSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // =============================== Add Text Watcher On EditText ===========================================
        name.addTextChangedListener(GenericTextWatcher(name))
        phone.addTextChangedListener(GenericTextWatcher(phone))
        email.addTextChangedListener(GenericTextWatcher(email))
        password.addTextChangedListener(GenericTextWatcher(password))
        passwordConfirmation.addTextChangedListener(GenericTextWatcher(passwordConfirmation))
        // =============================== End ===========================================

    }

    inner class GenericTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(editable: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.et_name_sign_up -> {
                    when {
                        editable.length >= 5 -> {
                            inputValidate(
                                setCompDrawIsCorrect = name,
                                textWarnGone = binding.tvWarnUsername,
                                viewEnableTrue = email,
                                setBackBoxTextFieldActive = binding.outlinedTextFieldEmail
                            )
                        }
                        editable.length >= 0 -> {
                            inputValidate(
                                textWarnVisible = binding.tvWarnUsername,
                                setCompDrawNotCorrect = name,
                                viewEnableFalse = email,
                                setBackBoxTextFieldDisable = binding.outlinedTextFieldName,
                                requestFocus = name
                            )
                        }
                        else -> {
                            inputValidate(
                                textWarnGone = binding.tvWarnUsername,
                                setCompDrawNotCorrect = name,
                                viewEnableFalse = email,
                                setBackBoxTextFieldDisable = binding.outlinedTextFieldName
                            )

                        }
                    }
                }
                R.id.et_email_sign_up -> {
                    if (editable.contains("@gmail.com")) {
                        inputValidate(
                            setCompDrawIsCorrect = email,
                            textWarnGone = binding.tvWarnEmail,
                            viewEnableTrue = phone,
                            setBackBoxTextFieldActive = binding.outlinedTextFieldPhone
                        )
                    } else {
                        inputValidate(
                            setCompDrawNotCorrect = email,
                            textWarnVisible = binding.tvWarnEmail,
                            viewEnableFalse = phone,
                            setBackBoxTextFieldDisable = binding.outlinedTextFieldPhone,
                            requestFocus = email
                        )
                    }
                }
                R.id.et_phone_sign_up -> {
                    if (editable.length < 12 || editable.length > 13) {
                        inputValidate(
                            setCompDrawNotCorrect = phone,
                            textWarnVisible = binding.tvWarnPhone,
                            viewEnableFalse = password,
                            setBackBoxTextFieldDisable = binding.outlinedTextFieldPass,
                            requestFocus = phone
                        )
                    } else {
                        inputValidate(
                            setCompDrawIsCorrect = phone,
                            textWarnGone = binding.tvWarnPhone,
                            viewEnableTrue = password,
                            setBackBoxTextFieldActive = binding.outlinedTextFieldPass
                        )
                    }
                }
                R.id.et_password_sign_up -> {
                    if (editable.length < 8) {
                        inputValidate(
                            textWarnVisible = binding.tvWarnEmail,
                            setCompDrawNotCorrect = password,
                            viewEnableFalse = passwordConfirmation,
                            setBackBoxTextFieldDisable = binding.outlinedTextFieldPassConfirm,
                            requestFocus = password
                        )
                    } else {
                        inputValidate(
                            textWarnGone = binding.tvWarnPassword,
                            setCompDrawIsCorrect = password,
                            viewEnableTrue = passwordConfirmation,
                            setBackBoxTextFieldActive = binding.outlinedTextFieldPassConfirm
                        )
                    }
                }
                R.id.et_password_confirm_sign_up -> {
                    if (editable.toString() == password.text.toString()) {
                        inputValidate(
                            textWarnGone = binding.tvWarnPasswordConfirmation,
                            setCompDrawIsCorrect = passwordConfirmation,
                            viewEnableTrue = binding.btnRegisterSignUp
                        )
                    } else {
                        inputValidate(
                            textWarnVisible = binding.tvWarnPasswordConfirmation,
                            setCompDrawNotCorrect = passwordConfirmation,
                            viewEnableFalse = binding.btnRegisterSignUp,
                            requestFocus = passwordConfirmation
                        )
                    }
                }
            }
        }
    }

    private fun inputValidate(
        setCompDrawIsCorrect: EditText? = null,
        setCompDrawNotCorrect: EditText? = null,
        textWarnVisible: View? = null,
        textWarnGone: View? = null,
        viewEnableTrue: View? = null,
        viewEnableFalse: View? = null,
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
        viewEnableTrue?.isEnabled = true
        viewEnableFalse?.isEnabled = false
        setBackBoxTextFieldActive?.setBoxBackgroundColorResource(R.color.white)
        setBackBoxTextFieldDisable?.setBoxBackgroundColorResource(R.color.input_disabled)
        requestFocus?.requestFocus()
    }

    private fun setFormEnable(condition: Boolean, setBackBoxColor: Int) {
        name.isEnabled = condition
        binding.outlinedTextFieldName.setBoxBackgroundColorResource(setBackBoxColor)

        email.isEnabled = condition
        binding.outlinedTextFieldEmail.setBoxBackgroundColorResource(setBackBoxColor)

        password.isEnabled = condition
        binding.outlinedTextFieldPass.setBoxBackgroundColorResource(setBackBoxColor)

        passwordConfirmation.isEnabled = condition
        binding.outlinedTextFieldPassConfirm.setBoxBackgroundColorResource(setBackBoxColor)
    }

    private fun createUser() {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(TAG, "Success Create Email Password")

                    val uid = auth.currentUser!!.uid
                    val name = name.text.toString()
                    val phone = phone.text.toString()
                    val email = email.text.toString()
                    val gender = enableRadioGroup().lowercase()

                    val apiService = UserApiService(this)
                    apiService.postData(name, phone, email, uid, gender, layoutInflater)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    binding.btnRegisterSignUp.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Log.d(TAG, "Failure Create Email Password")
                }
            }.addOnFailureListener {
                binding.btnRegisterSignUp.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                Log.d(TAG, it.message.toString())
            }
    }
}