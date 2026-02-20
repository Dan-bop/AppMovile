package com.maysu.maysuapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class CelularActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Desactiva el modo noche para mantener tu diseño blanco
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_numero)

        auth = FirebaseAuth.getInstance()

        // Inicialización de vistas
        val etPhoneNumber = findViewById<EditText>(R.id.etPhoneNumber)
        val btnRecibirCodigo = findViewById<Button>(R.id.btnRecibirCodigo)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val ccp = findViewById<CountryCodePicker>(R.id.ccp)

        // Configuración del CountryCodePicker
        ccp.registerCarrierNumberEditText(etPhoneNumber)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Lógica para enviar el SMS
        btnRecibirCodigo.setOnClickListener {
            val numeroCompleto = ccp.fullNumberWithPlus // Ejemplo: +51999888777

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(numeroCompleto)       // Número a verificar
                .setTimeout(60L, TimeUnit.SECONDS)     // Tiempo de espera
                .setActivity(this)                    // Activity para el callback
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        // El código se envió con éxito
                        Toast.makeText(this@CelularActivity, "Código enviado a $numeroCompleto", Toast.LENGTH_SHORT).show()

                        // Abrimos el diálogo para que el usuario ingrese el código
                        val dialog = SmsCodeDialogFragment.newInstance(verificationId)
                        dialog.show(supportFragmentManager, "SmsCodeDialog")
                    }

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Verificación automática (si el sistema detecta el SMS)
                        signInWithPhone(credential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // Error (Número inválido, falta SHA-1, etc.)
                        Toast.makeText(this@CelularActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        // Escuchador para mostrar/ocultar el botón verde según si el número es válido
        etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnRecibirCodigo.visibility = if (ccp.isValidFullNumber) View.VISIBLE else View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Función que realiza el login final en Firebase
    fun signInWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val intent = Intent(this, HomeActivity::class.java)
                // Limpia el historial para que no pueda volver al login con el botón atrás
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al iniciar sesión: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}