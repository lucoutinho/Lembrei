package com.example.lembrei

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvLembrete: TextView
    private val channelId = "lembrete_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnNotificacao = findViewById<Button>(R.id.btnNotificacao)
        tvLembrete = findViewById(R.id.tvLembrete)

        criarCanalNotificacao()

        btnAdd.setOnClickListener {
            val intent = Intent(this, AddLembreteActivity::class.java)
            startActivity(intent)
        }

        btnNotificacao.setOnClickListener {
            mostrarNotificacao()
        }
    }

    override fun onResume() {
        super.onResume()

        val preferencias = getSharedPreferences("lembretes", MODE_PRIVATE)
        val titulo = preferencias.getString("titulo", "")
        val data = preferencias.getString("data", "")
        val local = preferencias.getString("local", "")

        if (!titulo.isNullOrEmpty() && !data.isNullOrEmpty()) {
            tvLembrete.text = "Lembrete: $titulo\nData/Hora: $data\nLocal: $local"
        } else {
            tvLembrete.text = "Nenhum lembrete salvo"
        }
    }

    private fun criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal de Lembretes"
            val descriptionText = "Canal para notificações do app Lembrei"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun mostrarNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
                return
            }
        }

        val preferencias = getSharedPreferences("lembretes", MODE_PRIVATE)
        val titulo = preferencias.getString("titulo", "Você tem um lembrete")
        val data = preferencias.getString("data", "")

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Lembrei!")
            .setContentText("$titulo - $data")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}