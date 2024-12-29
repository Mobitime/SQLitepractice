package com.example.sqlitepractice

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ProductAdapter
    private lateinit var productList: ArrayList<Product>

    private lateinit var etName: EditText
    private lateinit var etWeight: EditText
    private lateinit var etPrice: EditText
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Потребительская корзина"


        etName = findViewById(R.id.etProductName)
        etWeight = findViewById(R.id.etWeight)
        etPrice = findViewById(R.id.etPrice)
        listView = findViewById(R.id.listView)


        dbHelper = DatabaseHelper(this)
        productList = ArrayList()


        adapter = ProductAdapter(this, productList)
        listView.adapter = adapter


        loadProducts()


        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveProduct()
        }


        listView.setOnItemLongClickListener { _, _, position, _ ->
            val product = productList[position]
            AlertDialog.Builder(this)
                .setTitle("Удаление")
                .setMessage("Удалить ${product.name}?")
                .setPositiveButton("Да") { _, _ ->
                    dbHelper.deleteProduct(product.id)
                    loadProducts()
                    Toast.makeText(this, "Продукт удален", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Нет", null)
                .show()
            true
        }
    }

    private fun saveProduct() {
        val name = etName.text.toString().trim()
        val weight = etWeight.text.toString().trim()
        val price = etPrice.text.toString().trim()

        if (name.isEmpty() || weight.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val product = Product(name = name, weight = weight, price = price)
        dbHelper.insertProduct(product)
        clearInputs()
        loadProducts()
        Toast.makeText(this, "Продукт сохранен", Toast.LENGTH_SHORT).show()
    }

    private fun loadProducts() {
        productList.clear()
        productList.addAll(dbHelper.getAllProducts())
        adapter.notifyDataSetChanged()
    }

    private fun clearInputs() {
        etName.text.clear()
        etWeight.text.clear()
        etPrice.text.clear()
        etName.requestFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}