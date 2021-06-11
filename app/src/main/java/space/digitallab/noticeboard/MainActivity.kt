 package space.digitallab.noticeboard

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import space.digitallab.noticeboard.databinding.ActivityMainBinding
import space.digitallab.noticeboard.dialoghelper.DialogConst
import space.digitallab.noticeboard.dialoghelper.DialogHelper


 class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

     private lateinit var tvAccount: TextView
    private lateinit var rootElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
     val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

     override fun onStart() {
         super.onStart()
         uiUpdate(mAuth.currentUser)
     }
    private fun init() {

        val toggle = ActionBarDrawerToggle(this, rootElement.driverLayout, rootElement.mainContent.toolbar, R.string.open, R.string.close)
        rootElement.driverLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener(this)
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.tv_acaunt_email)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.my_ads -> { Toast.makeText(this, "Pressed my_ads", Toast.LENGTH_LONG).show() }

            R.id.car -> { Toast.makeText(this, "Pressed car", Toast.LENGTH_LONG).show() }

            R.id.pc -> { Toast.makeText(this, "Pressed pc", Toast.LENGTH_LONG).show() }

            R.id.smartphone -> { Toast.makeText(this, "Pressed smartphone", Toast.LENGTH_LONG).show() }

            R.id.ds -> { Toast.makeText(this, "Pressed ds", Toast.LENGTH_LONG).show() }

            R.id.registration -> {
               dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
            }

            R.id.login -> {
                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            }

            R.id.logout -> {
                uiUpdate(null)
                mAuth.signOut()
            }

        }

        rootElement.driverLayout.closeDrawer(GravityCompat.START)
        return true
    }

     fun uiUpdate(firebaseUser: FirebaseUser?){
         tvAccount.text = if (firebaseUser == null){
            resources.getString(R.string.not_reg)
         }else{
             firebaseUser.email
         }
     }
}