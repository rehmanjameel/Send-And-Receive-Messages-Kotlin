package org.codebase.simplesendandreceivesms.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.codebase.simplesendandreceivesms.appglobals.AppGlobals

@RequiresApi(Build.VERSION_CODES.M)

class SmsReceiver: BroadcastReceiver() {
    private val TAG: String = SmsReceiver::class.java.simpleName
    val pdu_type = "pdus"

    private val appGlobals = AppGlobals()
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TAG", "onReceive:")

        val bundle : Bundle = intent?.extras!!
        val msgs: Array<SmsMessage?>
        var strMessage = ""
        var strMessageBody = ""
        val format = bundle.getString("format")

        val pdus = bundle[pdu_type] as Array<*>?

        if (pdus != null) {
            // Check the Android version.
            // Check the Android version.
            val isVersionM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            msgs = arrayOfNulls(pdus.size)

            for (i in msgs.indices) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray?, format)
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray?)
                }
                // Build the message to show.
                strMessage += "SMS from " + msgs[i]?.originatingAddress
                strMessageBody += """ :${msgs[i]?.messageBody}"""
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: $strMessage $strMessageBody")
                Toast.makeText(context, "$strMessage $strMessageBody", Toast.LENGTH_LONG).show()
                appGlobals.saveString("MessageBody", strMessageBody)
            }
        }
    }
}