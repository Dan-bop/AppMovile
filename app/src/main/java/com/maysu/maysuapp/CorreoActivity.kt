package com.maysu.maysuapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth

class CorreoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Forzar el modo claro siempre
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_correo)

        // 2. Declarar los componentes
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnSiguiente = findViewById<ImageButton>(R.id.btnSiguienteCorreo)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // 3. Configurar el botón de atrás
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // --- ESTA ES LA SECCIÓN QUE AGREGAMOS ---
        // 4. Lógica para enviar el correo al presionar la flecha verde
        btnSiguiente.setOnClickListener {
            val email = etEmail.text.toString().trim()

            // Configuración para que el enlace abra tu App directamente
            val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://maysuapp.firebaseapp.com")
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                    "com.maysu.maysuapp",
                    true, /* installIfNotAvailable */
                    "12"  /* minimumVersion */
                )
                .build()

            FirebaseAuth.getInstance().sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Enlace enviado a $email. Revisa tu bandeja.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        // ----------------------------------------

        // 5. Lógica de escucha para que el botón aparezca/desaparezca
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val correo = s.toString()
                val esValido = correo.contains("@") && correo.contains(".")
                btnSiguiente.visibility = if (esValido) View.VISIBLE else View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}