package com.maysu.maysuapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    // 1. Lanzador para la ventana de selección de cuenta de Google
    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                // Autenticar en Firebase con la cuenta de Google
                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bienvenido a MaysuApp", Toast.LENGTH_SHORT).show()
                        irAlHome()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error Firebase: ${it.message}", Toast.LENGTH_LONG)
                            .show()
                    }
            } catch (e: Exception) {
                Toast.makeText(this, "Error de conexión con Google", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // 2. Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Generado por el plugin de Google
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, gso)

        // 3. Botón Google
        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            googleLauncher.launch(googleClient.signInIntent)
        }
        // 3. Botón Celular
        findViewById<Button>(R.id.btnPhone).setOnClickListener {
            // Intent para saltar a la pantalla de número de celular
            val intent = Intent(this, CelularActivity::class.java)
            startActivity(intent)
        }

// 4. Botón Correo
        findViewById<Button>(R.id.btnCorreo).setOnClickListener {
            // Intent para saltar a la pantalla de agregar correo
            val intent = Intent(this, CorreoActivity::class.java)
            startActivity(intent)
        }

    }

    private fun irAlHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}