package org.codebase.simplesendandreceivesms

import android.Manifest
import android.R.id.message
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSendSMS.setOnClickListener {
            sendSMSMessage()
        }
    }

    protected fun sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.SEND_SMS
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.SEND_SMS),
                    MY_PERMISSIONS_REQUEST_SEND_SMS
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    val smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phoneNoETId.text.toString(), null,
                        smsETId.text.toString(), null, null)
                    Toast.makeText(applicationContext, "SMS sent.", Toast.LENGTH_LONG).show()

                    smsETId.setText("")
                } else {
                    Toast.makeText(
                        applicationContext,
                        "SMS faild, please try again.", Toast.LENGTH_LONG).show()
                    return
                }
            }
        }
    }

}