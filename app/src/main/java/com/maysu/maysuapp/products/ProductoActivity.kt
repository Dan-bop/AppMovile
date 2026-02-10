package com.maysu.maysuapp.products

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maysu.maysuapp.R

class ProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        // 1. Referencias de la UI
        val recyclerProductos = findViewById<RecyclerView>(R.id.recyclerProductos)
        val lblTitulo = findViewById<TextView>(R.id.lblTitulo)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnCart = findViewById<ImageView>(R.id.btnCart)

        // 2. Detectar qué categoría mostrar (Simulado por ahora)
        // Cuando tenga el Home, usaremos: intent.getStringExtra("CATEGORIA")
        val categoria = "LICORES"

        // 3. Cargar la lista y actualizar el título según la categoría
        val listaAMostrar = if (categoria == "ABARROTES") {
            lblTitulo.text = "Abarrotes"
            obtenerListaAbarrotes()
        } else {
            lblTitulo.text = "Licores"
            obtenerListaLicores()
        }

        // 4. Configurar el Adapter y RecyclerView
        val adapter = ProductoAdapter()
        adapter.setProductos(listaAMostrar)

        recyclerProductos.adapter = adapter
        recyclerProductos.layoutManager = GridLayoutManager(this, 2)

        // 5. Configurar eventos de botones
        btnBack.setOnClickListener {
            finish() // Cierra esta actividad y vuelve atrás
        }

        btnCart?.setOnClickListener {
            Toast.makeText(this, "Carrito próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    // --- LISTA DE LICORES ---
    private fun obtenerListaLicores(): List<Producto> {
        return listOf(
            Producto("Jagermeister", "750 ml", "$45.000", R.drawable.jager),
            Producto("Red Label", "750 ml", "$125.000", R.drawable.red_label),
            Producto("Absolut Vodka", "750 ml", "$52.000", R.drawable.absolut_vodka),
            Producto("Campari", "330 ml", "$4.500", R.drawable.campari),
            Producto("Corona Extra", "355 ml", "$6.000", R.drawable.corona),
            Producto("Malibu", "750 ml", "$150.000", R.drawable.malibu)
        )
    }

    // --- LISTA DE ABARROTES ---
    private fun obtenerListaAbarrotes(): List<Producto> {
        return listOf(
            Producto("Arroz Diana", "1 kg", "$4.800", R.drawable.arroz),
            Producto("Aceite Vegetal", "900 ml", "$12.500", R.drawable.aceite),
            Producto("Azúcar Blanca", "1 kg", "$3.900", R.drawable.azucar),
            Producto("Lentejas", "500 g", "$3.200", R.drawable.lenteja),
            Producto("Leche Entera", "1 L", "$4.100", R.drawable.leche),
            Producto("Café Molido", "500 g", "$18.000", R.drawable.cafe)
        )
    }
}