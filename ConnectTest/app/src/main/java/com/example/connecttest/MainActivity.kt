package com.example.connecttest

import android.accounts.NetworkErrorException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

const val SERVER_RUN = "SERVER_RUN"
const val CLIENT_RUN = "CLIENT_RUN"

class MainActivity : AppCompatActivity() {
    private val serverSocket = Runnable {   //Not sure on sub thread
        kotlin.run {
            Log.d(SERVER_RUN, "Server run.")
            val serverSocket = ServerSocket(8080)
        }
    }

    private val clientSocket = Runnable {
        run {
            Log.d(CLIENT_RUN, "Client run.")
            try {
                val localHost = InetAddress.getLocalHost()
                Log.d(CLIENT_RUN, "Host: $localHost")
                //TODO: Create one client socket
                val socket = Socket("127.0.0.2", 8080)
                if (socket.isConnected) {
                    Log.d(CLIENT_RUN, "Client socket is connected: ${socket.isConnected}")
                    Log.d(CLIENT_RUN, "inputStream: ${socket.getInputStream()}, outputStream: ${socket.getOutputStream()}")
                    val writer = "writer"
                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    var line: String = reader.readLine()
                    if (line != null) { //Get nothing???
                        Log.d(CLIENT_RUN, "Read line: $line")
                    } else Log.d(CLIENT_RUN, "Line is null: $line")
                }
            } catch (exception: Exception) {
                Log.d(CLIENT_RUN, "Get host failed: $exception")    //Get on main thread
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val thread = Thread()   //New thread
        this.findViewById<Button>(R.id.button1).setOnClickListener {
            thread { clientSocket.run() }
        }

        serverSocket.run()
    }
}