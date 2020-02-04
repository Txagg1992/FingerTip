package com.example.fingertip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class NextActivity : AppCompatActivity() {

    private lateinit var mBiometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var mBiometricManager: BiometricManager
    val TAG: String = "NextActivity"

    private lateinit var executor: Executor
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        mBiometricPrompt = initView()
    }

    private fun initView(): androidx.biometric.BiometricPrompt {

        executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext, "Authentication Succeeded!",
                    Toast.LENGTH_LONG).show()
                doLogin()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG, "$errorCode :: $errString")
                Toast.makeText(applicationContext, "Authentication Error: $errString",
                    Toast.LENGTH_LONG).show()
                loginWithPassword()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication Failed.",
                    Toast.LENGTH_LONG).show()
            }
        }
        return BiometricPrompt(this@NextActivity, executor, callback)
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Log into your Phone")
            .setSubtitle("This is the login subTitle")
            .setDeviceCredentialAllowed(false)
            .setConfirmationRequired(true)
            .setDescription("We can place a short description here")
            .setNegativeButtonText("Cancel/Login with Password")
            .build()
    }

    fun openBio(view: View) {
        Log.d(TAG, "<<< BiometricPrompt opened >>>")
        promptInfo = createPromptInfo()
        mBiometricManager = BiometricManager.from(applicationContext)

        when (mBiometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Can Authenticate")
                mBiometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "No biometric features on Device", Toast.LENGTH_LONG).show()
                Log.d(TAG, "No biometric features on Device")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Biometric features currently unavailable", Toast.LENGTH_LONG)
                    .show()
                Log.d(TAG, "Biometric features currently unavailable")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "User must set biometric print, etc.", Toast.LENGTH_LONG)
                    .show()
                Log.d(TAG, "User must set biometric print, etc.")
                //TODO take user to settings to set up biometrics
            }
        }
    }

    private fun loginWithPassword(){
        //TODO add password login activity here
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun doLogin(){
        //TODO add login process here
    }
}
