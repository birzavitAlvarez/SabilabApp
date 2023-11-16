package com.demo.sabilabapp.Categorias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.R
import com.demo.sabilabapp.databinding.ActivityCategoriasBinding
import android.annotation.SuppressLint
import android.app.Dialog
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.sabilabapp.Adapters.CategoriasAdapter
import com.demo.sabilabapp.Api.RetrofitClient.apiService
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CategoriasActivity : AppCompatActivity() {

    private var binding: ActivityCategoriasBinding? = null
    private lateinit var adapter: CategoriasAdapter
    private val datitos = mutableListOf<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //
        initRecyclerView()
        listaAlEntrar()
        //
        binding?.btnCategoriaNueva?.setOnClickListener{
            showDialogAddCategory()
        }
    }

    private fun showDialogAddCategory() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.item_add_categoria)

        val tvAddCategoriaTitle: TextView = dialog.findViewById(R.id.tvAddCategoriaTitle)
        val ibAddCategoriaClose: ImageButton = dialog.findViewById(R.id.ibAddCategoriaClose)
        val tilAddCategoriaTipo: TextInputLayout = dialog.findViewById(R.id.tilAddCategoriaTipo)
        val tietAddCategoriaTipo: TextInputEditText = dialog.findViewById(R.id.tietAddCategoriaTipo)
        val tilAddCategoriaDescripcion: TextInputLayout = dialog.findViewById(R.id.tilAddCategoriaDescripcion)
        val tietAddCategoriaDescripcion: TextInputEditText = dialog.findViewById(R.id.tietAddCategoriaDescripcion)
        val btnAddCategoriaGuardar: Button = dialog.findViewById(R.id.btnAddCategoriaGuardar)

        tietAddCategoriaTipo.requestFocus()
        ibAddCategoriaClose.setOnClickListener{
            dialog.dismiss()
        }
        // TODO VALIDAR
        tietAddCategoriaTipo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddCategoriaTipo.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        tietAddCategoriaDescripcion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tilAddCategoriaDescripcion.error = if (s?.any { it.isLetterOrDigit() } == true) null else "ES REQUERIDO"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        // TODO FIN VALIDAR

        btnAddCategoriaGuardar.setOnClickListener{
            // TODO VALIDAR
            if (tietAddCategoriaTipo.text.toString().isEmpty()){
                tilAddCategoriaTipo.error = "ES REQUERIDO"
                return@setOnClickListener
            } else if(tietAddCategoriaDescripcion.text.toString().isEmpty()){
                tilAddCategoriaDescripcion.error = "ES REQUERIDO"
                return@setOnClickListener
            }
            // TODO FIN VALIDAR

            val texTipo = tietAddCategoriaTipo.text.toString()
            val textDescripcion = tietAddCategoriaDescripcion.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val categoria = Categoria(texTipo, textDescripcion)
                apiService.createCategory(categoria)
                // actualizo la lista
                val updatedData = apiService.listCategory().body()?.data
                runOnUiThread {
                    if (updatedData != null) {
                        adapter.updateList(updatedData)
                    }
                    hideKeyboard()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding?.vistaCategoriaPadre?.windowToken,0)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listaAlEntrar() {
        CoroutineScope(Dispatchers.IO).launch {
            val call2 = apiService.listCategory()
            val pruebita = call2.body()
            runOnUiThread {
                if (call2.isSuccessful) {
                    val dataUsuario = pruebita?.data ?: emptyList()
                    datitos.clear()
                    datitos.addAll(dataUsuario)
                    adapter.notifyDataSetChanged()
                } else { showError() }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        adapter = CategoriasAdapter(datitos)
        binding?.rvCategoria?.layoutManager = LinearLayoutManager(this)
        binding?.rvCategoria?.adapter = adapter
    }
    //

    //
}