package com.example.lembrei

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddLembreteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lembrete)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etData = findViewById<EditText>(R.id.etData)
        val etLocal = findViewById<EditText>(R.id.etLocal)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        btnSalvar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val data = etData.text.toString()
            val local = etLocal.text.toString()

            if (titulo.isEmpty() || data.isEmpty()) {
                Toast.makeText(this, "Preencha título e data", Toast.LENGTH_SHORT).show()
            } else {
                val preferencias = getSharedPreferences("lembretes", MODE_PRIVATE)
                val editor = preferencias.edit()

                editor.putString("titulo", titulo)
                editor.putString("data", data)
                editor.putString("local", local)
                editor.apply()

                Toast.makeText(this, "Lembrete salvo com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}