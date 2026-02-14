package com.maysu.maysuapp.products
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.maysu.maysuapp.R

class ProductoViewHolder (inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(
        inflater.inflate(R.layout.item_productos,parent,false)
    ){
    private var imgProducto: ImageView? = null
    private var txtNombre: TextView? = null
    private var txtMedida: TextView? = null
    private var txtPrecio: TextView? = null

    init {
        imgProducto = itemView.findViewById(R.id.imgProducto)
        txtNombre = itemView.findViewById(R.id.txtNombre)
        txtMedida = itemView.findViewById(R.id.txtMedida)
        txtPrecio = itemView.findViewById(R.id.txtPrecio)
    }

    fun bind(producto: Producto) {
        txtNombre?.text = producto.nombre
        txtMedida?.text = producto.medida
        txtPrecio?.text = producto.precio
        imgProducto?.setImageResource(producto.imagen)
    }
}