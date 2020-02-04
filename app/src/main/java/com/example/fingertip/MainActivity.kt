package com.example.fingertip

import android.content.DialogInterface
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initViews(){

        nextButton.setOnClickListener {
            startActivity(Intent(this, NextActivity::class.java))
        }
        val executor = Executors.newSingleThreadExecutor()

        val activity = this

        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Log In to MyAccount")
            .setDescription("LogIn Description")
            .setSubtitle("LogIn")
            .setNegativeButton("Use Password", executor,
                DialogInterface.OnClickListener{dialog, which ->  })
            .build()

        bioButton.setOnClickListener(View.OnClickListener {
            Log.d("BioButton", "Clicked")
            biometricPrompt.authenticate(CancellationSignal(),
                executor, object : BiometricPrompt.AuthenticationCallback(){
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        activity.runOnUiThread{
                            Log.d(TAG, "Authenticate success $result")
                            Toast.makeText(activity, "Authenticated", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        super.onAuthenticationError(errorCode, errString)
                        activity.runOnUiThread{
                            Log.d(TAG, "ErrorCode: $errorCode :: Error: $errString")
                            Toast.makeText(activity, "You Fucked Up", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        activity.runOnUiThread {
                            Log.d(TAG, "Authentication Failed")
                            Toast.makeText(activity, "Not Again...", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                        super.onAuthenticationHelp(helpCode, helpString)
                        activity.runOnUiThread {
                            Toast.makeText(activity, "Hep Me, Hep me!", Toast.LENGTH_LONG).show()
                        }
                    }
                })
        })
    }
}
