package org.codebase.simplesendandreceivesms

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.codebase.simplesendandreceivesms.appglobals.AppGlobals


@RequiresApi(Build.VERSION_CODES.M)

class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 1
    private val appGlobals = AppGlobals()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkForSmsPermission()

        btnSendSMS.setOnClickListener {
            sendSms()
//            receivemsg()
        }
    }

    private fun checkForSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_MMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS),
                MY_PERMISSIONS_REQUEST_SEND_SMS)
        } else {
            enableSmsButton()
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
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableSmsButton()
                } else {
                    Log.d(TAG, "Denied")
                    Toast.makeText(applicationContext, "SMS faild, please try again.", Toast.LENGTH_LONG).show()
                    disableSmsButton()
//                    return
                }
            }
        }
    }

    private fun sendSms() {
        val phoneNo = phoneNoETId.text.toString()
        val smsText = smsETId.text.toString()

        val scAddress : String? = null
        val sentIntent : PendingIntent? = null
        val intentDelivery: PendingIntent? = null

        val smsManager = SmsManager.getDefault()
        if (phoneNo.isEmpty() && smsText.isEmpty()) {
            phoneNoETLayoutId.error = "Phone No required"
            smsEditTextLayoutId.error = "Sms text required"
        } else if (phoneNo.isEmpty()) {
            phoneNoETLayoutId.error = "Phone No required"

        } else if (smsText.isEmpty()) {
            smsEditTextLayoutId.error = "Sms text required"

        } else {
            checkForSmsPermission()

            smsManager.sendTextMessage(phoneNo, scAddress, smsText, sentIntent, intentDelivery)
            Toast.makeText(applicationContext, "SMS sent.", Toast.LENGTH_LONG).show()

            Log.d("Sms", "Sms Sent")
            val messageBody = appGlobals.getValueString("MessageBody")
            receivedSmsTextId.text = messageBody
            smsETId.setText("")

        }
    }

    private fun enableSmsButton() {
        btnSendSMS.visibility = View.VISIBLE
    }

    private fun disableSmsButton() {
        btnSendSMS.visibility = View.INVISIBLE

        button_retry.visibility = View.VISIBLE
    }

    fun retryApp(view: View) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }

//    private fun receivemsg() {
//        Log.e("here", "here is")
//        val br  = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    Log.e("if", "here is")
//
//                    for (sms: SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)){
//                        Log.e("for", "here is")
//
//                        Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_LONG).show()
//                        receivedSmsTextId.text = sms.displayMessageBody
//                    }
//                }
//            }
//        }
//        Log.e("heref", "here is")
//
//        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
//    }

}