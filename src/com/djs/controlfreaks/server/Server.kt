package com.djs.controlfreaks.server
import kotlinx.coroutines.*
import com.djs.controlfreaks.shared.Logger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

class Server(port: UShort) {
    private var shouldQuit = false
    private val serverScope = CoroutineScope(Dispatchers.IO)

    private lateinit var serverSocket: ServerSocket

    init {
        try {
            Logger.Print("Starting server on: $port")
            serverSocket = ServerSocket(port.toInt())
            Logger.Print("Server successfully started")
        } catch (e: IOException) {
            Logger.Fatal("failed to create server socket: $e")
        }
    }

    fun run() = runBlocking {
        while (!shouldQuit) {
            try {
                Logger.Print("Awaiting Connection")
                val clientSocket = serverSocket.accept()
                serverScope.launch {
                    handleConnection(clientSocket)
                }
            } catch (e: IOException) {
                Logger.Error("failed to accept connection: $e")
            }
        }
    }

    private suspend fun handleConnection(socket: Socket) = withContext(Dispatchers.IO) {
        Logger.Print("Successfully accepted connection: ${socket.inetAddress.hostAddress}")
        val input = BufferedReader(InputStreamReader(socket.getInputStream()))
        val remoteIp = socket.inetAddress
        var message: String?
        try {
            while (socket.isConnected) {
                message = input.readLine()
                if (message == null) {
                    break
                }
                Logger.Print("Received \"$message\" from: $remoteIp")
            }
        } catch (e: Exception) {
            Logger.Error("$e")
        } finally {
            socket.close()
            Logger.Print("Client $remoteIp disconnected")
        }
    }
}

