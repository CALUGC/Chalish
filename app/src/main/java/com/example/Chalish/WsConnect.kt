package com.example.Chalish

import android.content.Intent
import android.graphics.Color
import okhttp3.*
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.firebase.ui.auth.AuthUI
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_ws_connect.*
import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
import java.util.concurrent.TimeUnit
import java.security.MessageDigest

lateinit var lisVocab : List<VocabJsonObj>
var iAnsidx = -1
var iP1ChoseIdx = -1
var iP2ChoseIdx = -1
var webSocket : WebSocket?=null
var iTimeTick = 0
val lisBtn = mutableListOf<Button>()
var iP1Score = 0
var iP2Score = 0
var iRoundCnt = 0
var iVocabCnt = 7000

class WsConnect : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ws_connect)

        Toast.makeText(applicationContext, getSharedPreferences("DATA", 0).getString("StrUsername","str no fund!"), Toast.LENGTH_SHORT).show()

        funVarInit()
        funConnectInit()

        webSocket?.send("need_play_$iVocabCnt")

    }

    fun funVarInit(){
        lisBtn.add(btn_opt1)
        lisBtn.add(btn_opt2)
        lisBtn.add(btn_opt3)
        lisBtn.add(btn_opt4)
        for (i in lisBtn.indices){
            lisBtn[i].isClickable = false
            lisBtn[i].isEnabled = false
        }
        tvP1Score.text = iP1Score.toString()
        tvP2Score.text = iP2Score.toString()

        btnLogout.setOnClickListener {
            Toast.makeText(applicationContext, "我按了r幹", Toast.LENGTH_SHORT).show()
            AuthUI.getInstance()
                .signOut(this)
            val intent = Intent(this, thirdPartyLogin::class.java)
            startActivity(intent)
        }

        btnCloseConnection.setOnClickListener {
            webSocket?.close(1000,"btn_close_trigger!")
        }

        btn_opt1.setOnClickListener {
            iP1ChoseIdx = 0
            fun_chosen(iAnsidx)
        }
        btn_opt2.setOnClickListener {
            iP1ChoseIdx = 1
            fun_chosen(iAnsidx)
        }
        btn_opt3.setOnClickListener {
            iP1ChoseIdx = 2
            fun_chosen(iAnsidx)
        }
        btn_opt4.setOnClickListener {
            iP1ChoseIdx = 3
            fun_chosen(iAnsidx)
        }
    }

    fun funConnectInit(){
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()

        val request = okhttp3.Request.Builder()
            .url("ws://140.136.151.173:3001")
            .build()

        val wsListener = EchoWebSocketListener()

        webSocket = client.newWebSocket(request, wsListener)
    }

    val tTimer : CountDownTimer =
        object : CountDownTimer(6000, 1) {

            override fun onTick(millisUntilFinished: Long) {
                tvCountTime.text = ((millisUntilFinished)/1000).toString()
                iTimeTick = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                tvCountTime.text = "Time up!"
                for (i in lisBtn.indices){
                    lisBtn[i].isClickable = false
                    lisBtn[i].isEnabled = false
                }
                if (iP1ChoseIdx != -1){
                    lisBtn[iP1ChoseIdx].setTextColor(Color.rgb(255,0, 0))
                }
                if (iP2ChoseIdx != -1){
                    lisBtn[iP2ChoseIdx].setTextColor(Color.rgb(0,0, 255))
                }
                lisBtn[iAnsidx].setBackgroundColor(Color.rgb(0,255, 0))
                iRoundCnt++
            }
        }

    fun fun_chosen(iAnsidx: Int){
        for (i in lisBtn.indices){
            lisBtn[i].isClickable = false
            lisBtn[i].isEnabled = false
        }
        lisBtn[iP1ChoseIdx].setTextColor(Color.rgb(255,0, 0))
        lisBtn[iAnsidx].setBackgroundColor(Color.rgb(0,255, 0))

        var iTmpGetScore = if(iP1ChoseIdx == iAnsidx) iTimeTick/10 else 0
        iP1Score += iTmpGetScore
        tvP1Score.text = iP1Score.toString()
        webSocket?.send("ansOpt"+iP1ChoseIdx.toString()+"_"+ (iTmpGetScore).toString())

    }

    fun funPrepareRound(){

        runOnUiThread {
            for (i in lisBtn.indices){
                lisBtn[i].setBackgroundColor(Color.rgb(255,255,255))
                lisBtn[i].setText(lisVocab[i].ENG)
                lisBtn[i].setTextColor(Color.rgb(0,0,0))
                lisBtn[i].isClickable = true
                lisBtn[i].isEnabled = true
            }
            iP1ChoseIdx = -1
            iP2ChoseIdx = -1
            tvAnsCH.setText(lisVocab[iAnsidx].CH)
            tTimer.start()
        }

    }

    inner class EchoWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            webSocket.send("SD : start connencted!")
//        webSocket.send(ByteString.decodeHex("deadbeef"))
//        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
        }


        override fun onMessage(webSocket: WebSocket?, text: String?) {

            output("Receiving : " + text!!)
            try {
                var vocab = JSONArray(text)
                Log.d("WSS","Is Json")
                val gson = GsonBuilder().create()
                val items= gson.fromJson(text,Array<VocabJsonObj>::class.java).toList()
                lisVocab = items
                for (value in items) {
                    Log.d("Vocab","value:${value.CH}")
                }

                webSocket!!.send("recived vocab")

                runOnUiThread{
                    imageView.setVisibility(View.GONE)
                }


            }
            catch (e : JSONException){
                if(text.length==12 && text.substring(0,11)=="start_round"){
                    iAnsidx = Character.getNumericValue(text[11])
                    funPrepareRound()
                }
                if (text=="stop_timer"){
                    tTimer.cancel()
                    if (iP2ChoseIdx != -1){
                        runOnUiThread {
                            lisBtn[iP2ChoseIdx].setTextColor(Color.rgb(0, 0, 255))
                        }
                    }
                }
                if (text.length>12 && text.substring(0,12)=="ansAndScore:"){

                    iP2ChoseIdx = Character.getNumericValue(text[12])
                    Log.d("WSS","ip2 ========$iP2ChoseIdx")
                    iP2Score+=text.substring(13,text.length).toInt()

                    runOnUiThread{
                        tvP2Score.text = iP2Score.toString()
                    }
                }
            }
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
            output("Receiving bytes : " + bytes!!.hex())
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            webSocket!!.close(NORMAL_CLOSURE_STATUS, null)
            output("Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            output("Error : " + t.message)
        }

        private val NORMAL_CLOSURE_STATUS = 1000

        private fun output(txt: String) {
            Log.v("WSS", txt)
        }
    }

}



class VocabJsonObj {
    @SerializedName("ENG")
    var ENG: String? = null

    @SerializedName("CH")
    var CH : String? = null
}