package com.demo.sabilabapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sabilabapp.databinding.ActivityMainBinding
//
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import android.widget.Toast
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.content.res.Configuration
import android.widget.TextView
import com.demo.sabilabapp.Aprovisionamiento.AprovisionamientoActivity
import com.demo.sabilabapp.Categorias.CategoriasActivity
import com.demo.sabilabapp.Clientes.Clientes2Activity
import com.demo.sabilabapp.Clientes.ClientesActivity
import com.demo.sabilabapp.Login.LoginActivity
import com.demo.sabilabapp.Login.UserData  // Class que almacena data
import com.demo.sabilabapp.Productos.Productos2Activity
import com.demo.sabilabapp.Productos.ProductosActivity
import com.demo.sabilabapp.Proveedores.ProveedorActivity
import com.demo.sabilabapp.Usuarios.UsuarioActivity

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    var id_usuarios: Int? = null
    var id_roles:Int? = null
    var id_vendedor:Int? = null
    var nombre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //--------------------------------------------------------------------------------------------
        // TODO RECUPERANDO DATOS DE INICIO DE SESION
        val userData = intent.getSerializableExtra("userData") as? UserData
        if (userData != null) {
            id_usuarios = userData.id_usuarios
            id_roles = userData.id_roles
            id_vendedor = userData.id_vendedor
            nombre = userData.nombre
        }
        //--------------------------------------------------------------------------------------------
        binding?.tvMainIdUsuario?.text = id_usuarios.toString()
        binding?.tvMainIdRol?.text = id_roles.toString()
        binding?.tvMainIdVendedor?.text = id_vendedor.toString()
        binding?.tvMainNombre?.text = nombre.toString()

        //--------------------------------------------------------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.nav_view)
        changueNameOfHeader()

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            findViewById(R.id.toolbar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)

        //--------------------------------------------------------------------------------------------

        val navigationView = binding?.navView //findViewById<NavigationView>(R.id.nav_view)

        val menu = navigationView?.menu
        val dashboardItem = menu?.findItem(R.id.nav_dashboard)
        val usuariosItem = menu?.findItem(R.id.nav_usuarios)
        val aprovisionamientoItem = menu?.findItem(R.id.nav_aprovisionamiento)
        val vendedoresItem = menu?.findItem(R.id.nav_vendedores)
        val categoriasItem = menu?.findItem(R.id.nav_categorias)
        val proveedoresItem = menu?.findItem(R.id.nav_proveedores)
        val reportesItem = menu?.findItem(R.id.nav_reportes)
        if (id_roles == 2) {
            dashboardItem?.isVisible = false
            usuariosItem?.isVisible = false
            aprovisionamientoItem?.isVisible = false
            vendedoresItem?.isVisible = false
            categoriasItem?.isVisible = false
            proveedoresItem?.isVisible = false
            reportesItem?.isVisible = false
        }

        navigationView?.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    Toast.makeText(applicationContext, "Dashboard clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                    true
                    //val anny = Intent(this@MainActivity, LoginActivity::class.java)
                    //startActivity(anny)
                }
                R.id.nav_usuarios -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.closeDrawers()
                    true
                    val anny = Intent(this@MainActivity, UsuarioActivity::class.java)
                    startActivity(anny)
                }
                R.id.nav_aprovisionamiento -> {
                    Toast.makeText(applicationContext, "Aprovisionamiento clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.closeDrawers()
                    true
                    val anny = Intent(this@MainActivity, AprovisionamientoActivity::class.java)
                    startActivity(anny)
                }
                R.id.nav_pedidos -> {
                    Toast.makeText(applicationContext, "Pedidos clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_clientes -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.closeDrawers()
                    true
                    if (id_roles == 2){
                        val anny = Intent(this@MainActivity, Clientes2Activity::class.java)
                        anny.putExtra("id_vendedor", id_vendedor)
                        startActivity(anny)
                    } else {
                        val anny = Intent(this@MainActivity, ClientesActivity::class.java)
                        startActivity(anny)
                    }
                }
                R.id.nav_vendedores -> {
                    Toast.makeText(applicationContext, "Vendedores clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_productos -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.closeDrawers()
                    true
                    if (id_roles == 2){
                        val anny = Intent(this@MainActivity, Productos2Activity::class.java)
                        startActivity(anny)
                    } else {
                        val anny = Intent(this@MainActivity, ProductosActivity::class.java)
                        startActivity(anny)
                    }
                }
                R.id.nav_categorias -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.closeDrawers()
                    true
                    val anny = Intent(this@MainActivity, CategoriasActivity::class.java)
                    startActivity(anny)
                }
                R.id.nav_proveedores -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    drawerLayout.closeDrawers()
                    true
                    val anny = Intent(this@MainActivity, ProveedorActivity::class.java)
                    startActivity(anny)
                }
                R.id.nav_reportes -> {
                    Toast.makeText(applicationContext, "Reporte clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
            true
        }
        //--------------------------------------------------------------------------------------------


    }

    private fun changueNameOfHeader() {
        val headerView = navigationView.getHeaderView(0)
        val tvNombreUser = headerView.findViewById<TextView>(R.id.tv_nombre_user)
        if (nombre != null) {
            tvNombreUser.text = nombre
        }
    }


    //--------------------------------------------------------------------------------------------
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //--------------------------------------------------------------------------------------------
}