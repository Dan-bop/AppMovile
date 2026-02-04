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
import com.hbb20.CountryCodePicker
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class CelularActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_numero)

        auth = FirebaseAuth.getInstance()

        val etPhoneNumber = findViewById<EditText>(R.id.etPhoneNumber)
        val btnRecibirCodigo = findViewById<Button>(R.id.btnRecibirCodigo)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val ccp = findViewById<CountryCodePicker>(R.id.ccp)

        ccp.registerCarrierNumberEditText(etPhoneNumber)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // --- ESTA ES LA SECCIÓN DE FIREBASE PARA EL SMS ---
        btnRecibirCodigo.setOnClickListener {
            val numeroCompleto = ccp.fullNumberWithPlus // Obtiene el +51999...

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(numeroCompleto)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        // El SMS se envió con éxito. Saltamos a la pantalla para poner el código.
                        Toast.makeText(this@CelularActivity, "Código enviado", Toast.LENGTH_SHORT).show()

                        // Aquí deberías crear una actividad llamada "OtpActivity" para recibir los 6 dígitos
                        // val intent = Intent(this@CelularActivity, OtpActivity::class.java)
                        // intent.putExtra("verificacionId", verificationId)
                        // startActivity(intent)
                    }

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Esto ocurre si el teléfono verifica el SMS automáticamente
                        Toast.makeText(this@CelularActivity, "Verificación automática exitosa", Toast.LENGTH_SHORT).show()
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // Si hay error (ej: número mal escrito o falta configurar SHA-1)
                        Toast.makeText(this@CelularActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
        // ---------------------------------------------------

        etPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnRecibirCodigo.visibility = if (ccp.isValidFullNumber) View.VISIBLE else View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}