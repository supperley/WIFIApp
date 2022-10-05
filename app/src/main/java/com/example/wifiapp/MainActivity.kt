package com.example.wifiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.DataOutputStream
import java.net.Socket
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var HOST = "192.168.43.232"
    private var PORT = 8000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.btn_send)
        val tvMain = findViewById<TextView>(R.id.tv_main)
        val edText = findViewById<EditText>(R.id.edtext)
        val edPort = findViewById<EditText>(R.id.edport)
        val edIP = findViewById<EditText>(R.id.edip)
        button.setOnClickListener {
            if (edIP.text.isNotEmpty() && edPort.text.isNotEmpty()) {
                Toast.makeText(applicationContext, "Try to send message...", Toast.LENGTH_SHORT).show()
                HOST = edIP.text.toString()
                PORT = edPort.text.toString().toInt()
                val str = edText.text.toString()
                val ans = myFunction(str) // do on IO thread
                tvMain.text = if (ans) "Data sent successfully!" else "Data sent with error!"
            } else {
                Toast.makeText(applicationContext, "Check your data!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun myFunction(str : String): Boolean = runBlocking {
        send(str).await()
    }

    private suspend fun send(str : String) : Deferred<Boolean>  = withContext(Dispatchers.IO) {
        Log.d("WifiAppLog", "Function send!")
        var ans : Boolean = true
        async {
//        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = Socket(HOST, PORT)
                Log.d("WifiAppLog", "Socket created!")
                val writer = DataOutputStream(socket.getOutputStream())
                Log.d("WifiAppLog", "Writer created!")
                writer.writeUTF(str)
                Log.d("WifiAppLog", "Text wrote!")
                /* Flush the output to commit */
                writer.flush()
                writer.close()
                socket.close()
            } catch (e: Exception) {
                Log.d("WifiAppLog", e.message.toString())
                ans = false
            }
            ans
        }
//  }
    }
}