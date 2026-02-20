package com.maysu.maysuapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class CorreoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correo)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvRegister = findViewById<TextView>(R.id.tvRegister) // Agregado el TextView

        btnBack.setOnClickListener { finish() }

        // Si el usuario hace clic directamente en "Regístrate"
        tvRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            abrirRegistro(email, password)
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Intentar Iniciar Sesión
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        irAlHome()
                    } else {
                        val exception = task.exception
                        // Caso 1: El correo no está registrado
                        if (exception is FirebaseAuthInvalidUserException) {
                            abrirRegistro(email, password)
                        }
                        // Caso 2: Contraseña incorrecta
                        else if (exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                        // Otros errores (Red, etc.)
                        else {
                            Toast.makeText(this, "Error: ${exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    private fun abrirRegistro(email: String, pass: String) {
        // Usamos la función estática 'newInstance' para pasar datos de forma segura
        val dialog = RegisterDialogFragment.newInstance(email, pass)
        dialog.show(supportFragmentManager, "registerDialog")
    }

    private fun irAlHome() {
        val intent = Intent(this, HomeActivity::class.java)
        // Limpiamos el historial para que no pueda volver atrás al login
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}