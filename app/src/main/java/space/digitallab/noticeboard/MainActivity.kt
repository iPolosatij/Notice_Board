 package space.digitallab.noticeboard

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import space.digitallab.noticeboard.databinding.ActivityMainBinding


 class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var rootElement: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

    private fun init() {

        val toggle = ActionBarDrawerToggle(this, rootElement.driverLayout, rootElement.mainContent.toolbar, R.string.open, R.string.close)
        rootElement.driverLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.my_ads -> { Toast.makeText(this, "Pressed my_ads", Toast.LENGTH_LONG).show() }

            R.id.car -> { Toast.makeText(this, "Pressed car", Toast.LENGTH_LONG).show() }

            R.id.pc -> { Toast.makeText(this, "Pressed pc", Toast.LENGTH_LONG).show() }

            R.id.smartphone -> { Toast.makeText(this, "Pressed smartphone", Toast.LENGTH_LONG).show() }

            R.id.ds -> { Toast.makeText(this, "Pressed ds", Toast.LENGTH_LONG).show() }

            R.id.registration -> { Toast.makeText(this, "Pressed registration", Toast.LENGTH_LONG).show() }

            R.id.logout -> { Toast.makeText(this, "Pressed logout", Toast.LENGTH_LONG).show() }

        }

        rootElement.driverLayout.closeDrawer(GravityCompat.START)
        return true
    }
}