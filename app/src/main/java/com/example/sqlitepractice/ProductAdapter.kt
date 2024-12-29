package com.example.sqlitepractice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ProductAdapter(private val context: Context, private val products: List<Product>) :
    ArrayAdapter<Product>(context, R.layout.list_item, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item, parent, false)

        val product = products[position]

        view.findViewById<TextView>(R.id.tvName).text = product.name
        view.findViewById<TextView>(R.id.tvWeight).text = "Вес: ${product.weight}"
        view.findViewById<TextView>(R.id.tvPrice).text = "Цена: ${product.price} ₽"

        return view
    }
}