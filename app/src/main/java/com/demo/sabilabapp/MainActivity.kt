package com.demo.sabilabapp

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
import com.demo.sabilabapp.Compras.ComprasActivity
import com.demo.sabilabapp.Login.LoginActivity
import com.demo.sabilabapp.Usuarios.UsuarioActivity

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //setContentView(R.layout.activity_main)

        //--------------------------------------------------------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout)


        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = findViewById(R.id.nav_view)

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
                    Toast.makeText(applicationContext, "Usuarios clicked", Toast.LENGTH_SHORT).show()
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
                    val anny = Intent(this@MainActivity, ComprasActivity::class.java)
                    startActivity(anny)
                }
                R.id.nav_clientes -> {
                    Toast.makeText(applicationContext, "Clientes clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_vendedores -> {
                    Toast.makeText(applicationContext, "Vendedores clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    Toast.makeText(applicationContext, "Logout clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_productos -> {
                    Toast.makeText(applicationContext, "Productos clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_categorias -> {
                    Toast.makeText(applicationContext, "Categorias clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_proveedores -> {
                    Toast.makeText(applicationContext, "Proveedores clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_reportes -> {
                    Toast.makeText(applicationContext, "Reporte clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    Toast.makeText(applicationContext, "Logout clicked", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
            true
        }
        //--------------------------------------------------------------------------------------------


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