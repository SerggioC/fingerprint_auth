package com.an.biometric.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.an.biometric.BiometricCallback
import com.an.biometric.BiometricManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BiometricCallback {

    private lateinit var mBiometricManager: BiometricManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authenticateButton.setOnClickListener {
            mBiometricManager = BiometricManager.BiometricBuilder(this@MainActivity)
                .setTitle(getString(R.string.biometric_title))
                .setSubtitle(getString(R.string.biometric_subtitle))
                .setDescription(getString(R.string.biometric_description))
                .setNegativeButtonText(getString(android.R.string.cancel))
                .build()

            //start authentication
            mBiometricManager.authenticate(this@MainActivity)
        }
        authenticateButton.callOnClick()

    }

    override fun onSdkVersionNotSupported() {
        Toast.makeText(
            applicationContext,
            getString(R.string.biometric_error_sdk_not_supported),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBiometricAuthenticationNotSupported() {
        Toast.makeText(
            applicationContext,
            getString(R.string.biometric_error_hardware_not_supported),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBiometricAuthenticationNotAvailable() {
        Toast.makeText(
            applicationContext,
            getString(R.string.biometric_error_fingerprint_not_available),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(
            applicationContext,
            getString(R.string.biometric_error_permission_not_granted),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBiometricAuthenticationInternalError(error: String) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }

    override fun onAuthenticationFailed() {
        Toast.makeText(applicationContext, getString(R.string.biometric_failure), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onAuthenticationCancelled() {
        Toast.makeText(
            applicationContext,
            getString(R.string.biometric_cancelled),
            Toast.LENGTH_SHORT
        ).show()
        mBiometricManager.cancelAuthentication()
    }

    override fun onAuthenticationSuccessful() {
        Toast.makeText(applicationContext, getString(R.string.biometric_success), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
        Toast.makeText(applicationContext, helpString, Toast.LENGTH_SHORT).show()
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        Toast.makeText(applicationContext, errString, Toast.LENGTH_SHORT).show()
    }
}
