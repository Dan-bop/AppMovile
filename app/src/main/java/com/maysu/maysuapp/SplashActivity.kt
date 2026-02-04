package com.maysu.maysuapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Forzamos el color de la barra de estado antes de cargar la vista
        window.statusBarColor = ContextCompat.getColor(this, R.color.logo)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 2. Animación suave para el logo
        val logo = findViewById<ImageView>(R.id.imgLogoSplash)
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeIn.duration = 1500 // El logo aparece suavemente en 1.5 segundos
        logo.startAnimation(fadeIn)

        // 3. Temporizador de 5 segundos para ir al Login
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}