package com.maysu.maysuapp.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter: RecyclerView.Adapter<ProductoViewHolder>() {

    private var listaProductos = emptyList<Producto>()

    fun setProductos(productos: List<Producto>) {
        this.listaProductos = productos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductoViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int = listaProductos.size
}

