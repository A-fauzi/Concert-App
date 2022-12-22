package com.example.concert_app.view

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.R
import com.example.concert_app.apiConfig.NetworkConfig
import com.example.concert_app.data.user.UserModel
import com.example.concert_app.data.whatsapp.ServiceImplement
import com.example.concert_app.databinding.ActivityLoginBinding
import com.example.concert_app.utils.FirebaseServiceInstance
import com.example.concert_app.utils.Libs.clearText
import com.example.concert_app.utils.Libs.dialogMessageAnimate
import com.example.concert_app.utils.LocalKeys
import com.example.concert_app.utils.Preference.saveData
import com.example.concert_app.view.main.MainActivity
import com.example.concert_app.view.main.fragment.home.HomeFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    Toast.makeText(this, "Precise location access granted.", Toast.LENGTH_SHORT)
                        .show()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    Toast.makeText(
                        this,
                        "Only approximate location access granted.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    // No location access granted.
                    Toast.makeText(this, "No location access granted.", Toast.LENGTH_SHORT).show()

                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

    }

    override fun onStart() {
        super.onStart()

        btnLogin.setOnClickListener {
            setFormEnable(false, R.color.input_disabled)

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                val latitude = location?.latitude
                val longitude = location?.longitude
                val locationCoordinate = "$latitude, $longitude"

                if (latitude != null && longitude != null) {
                    signInUser(locationCoordinate)
                }

            }


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



    private fun signInUser(locatioCoordinate: String) {

        auth.signInWithEmailAndPassword(
            etEmail.text.toString().trim(),
            etPass.text.toString().trim()
        ).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {

                ServiceImplement().sendMessage(
                    "sign_in_log ",
                    auth.currentUser?.email!!,
                    "null",
                    locatioCoordinate,
                    "id"
                )

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

                clearText(etEmail)
                clearText(etPass)

            } else {

                setFormEnable(true, R.color.white)

                dialogMessageAnimate(
                    layoutInflater,
                    this,
                    authResult.exception?.localizedMessage!!,
                    R.raw.auth_failure,
                    "Uuppss"
                )
                btnLogin.visibility = View.VISIBLE
                progressBar.visibility = View.GONE

            }

        }.addOnFailureListener { authFailure ->

            setFormEnable(true, R.color.white)
            dialogMessageAnimate(
                layoutInflater,
                this,
                authFailure.localizedMessage!!,
                R.raw.auth_failure,
                "Uuppss"
            )
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