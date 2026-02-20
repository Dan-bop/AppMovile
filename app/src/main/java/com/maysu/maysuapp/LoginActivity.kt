package com.maysu.maysuapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient // Movido aquí para usarlo en los botones

    private val googleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bienvenido a MaysuApp", Toast.LENGTH_SHORT).show()
                        irAlHome()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error Firebase: ${it.message}", Toast.LENGTH_LONG).show()
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

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(this, gso)

        // Botón Google: Ahora SIEMPRE pedirá elegir cuenta
        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            googleClient.signOut().addOnCompleteListener {
                googleLauncher.launch(googleClient.signInIntent)
            }
        }

        // Botón Celular
        findViewById<Button>(R.id.btnPhone).setOnClickListener {
            startActivity(Intent(this, CelularActivity::class.java))
        }

        // Botón Correo
        findViewById<Button>(R.id.btnCorreo).setOnClickListener {
            startActivity(Intent(this, CorreoActivity::class.java))
        }
    }

    private fun irAlHome() {
        val intent = Intent(this, HomeActivity::class.java)
        // Evita que el usuario regrese al login al pulsar el botón "atrás"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}