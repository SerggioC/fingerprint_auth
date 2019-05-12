package com.an.biometric

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sergiocruz.fingerprint.FingerPrintView

class BiometricDialogV23(context: Context, biometricCallback: BiometricCallback) :
    BottomSheetDialog(context, R.style.BottomSheetDialogTheme), View.OnClickListener {

    private var btnCancel: Button? = null
    private var imgLogo: ImageView? = null
    private var itemTitle: TextView? = null
    private var itemDescription: TextView? = null
    private var itemSubtitle: TextView? = null
    private var itemStatus: TextView? = null
    private var biometricCallback: BiometricCallback? = biometricCallback
    private var fingerprintView: FingerPrintView? = null

    init {
        setDialogView()
    }

    private fun setDialogView() {
        val bottomSheetLayout = layoutInflater.inflate(R.layout.authenticate_bottom_sheet_layout, null)
        setContentView(bottomSheetLayout)

        fingerprintView = findViewById(R.id.fingerprint)
        imgLogo = findViewById(R.id.appLogo)
        itemTitle = findViewById(R.id.title)
        itemStatus = findViewById(R.id.currentStatus)
        itemSubtitle = findViewById(R.id.subtitle)
        itemDescription = findViewById(R.id.description)

        btnCancel = findViewById(R.id.cancelButton)
        btnCancel?.setOnClickListener(this)

        updateLogo()
    }

    fun setTitle(title: String?) {
        itemTitle?.text = title
    }

    fun updateStatus(status: String?, state: FingerPrintView.State) {
        itemStatus?.text = status
        fingerprintView?.setState(state)
    }

    fun setSubtitle(subtitle: String?) {
        itemSubtitle?.text = subtitle
    }

    fun setDescription(description: String?) {
        itemDescription?.text = description
    }

    fun setButtonText(negativeButtonText: String?) {
        btnCancel?.text = negativeButtonText
    }

    private fun updateLogo() {
        try {
            val drawable = context.packageManager.getApplicationIcon(context.packageName)
            imgLogo?.setImageDrawable(drawable)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onClick(view: View) {
        dismiss()
        biometricCallback?.onAuthenticationCancelled()
    }
}
