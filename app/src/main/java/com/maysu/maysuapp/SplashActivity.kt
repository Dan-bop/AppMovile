package com.maysu.maysuapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Forzamos el color de la barra de estado
        window.statusBarColor = ContextCompat.getColor(this, R.color.logo)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 2. Animación suave para el logo
        val logo = findViewById<ImageView>(R.id.imgLogoSplash)
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1500
        logo.startAnimation(fadeIn)

        // 3. Lógica de salto (Login o Home)
        Handler(Looper.getMainLooper()).postDelayed({
            verificarSesion()
        }, 3000) // 3 segundos es ideal para no cansar al usuario
    }

    private fun verificarSesion() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            // EL USUARIO YA ESTÁ LOGUEADO -> Vamos al Home directamente
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            // NO HAY SESIÓN -> Vamos al Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish() // IMPORTANTE: Cerramos el Splash para que no puedan volver con el botón "Atrás"
    }
}