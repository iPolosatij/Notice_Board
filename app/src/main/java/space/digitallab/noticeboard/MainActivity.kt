 package space.digitallab.noticeboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import space.digitallab.noticeboard.act.EditAdsAct
import space.digitallab.noticeboard.database.DbManager
import space.digitallab.noticeboard.databinding.ActivityMainBinding
import space.digitallab.noticeboard.dialoghelper.DialogConst
import space.digitallab.noticeboard.dialoghelper.DialogHelper
import space.digitallab.noticeboard.dialoghelper.GoogleAccConst


 class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

     private lateinit var tvAccount: TextView
     private lateinit var rootElement: ActivityMainBinding
     private val dialogHelper = DialogHelper(this)
     val mAuth = FirebaseAuth.getInstance()
     val dbManager =  DbManager()

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         rootElement = ActivityMainBinding.inflate(layoutInflater)
         val view = rootElement.root
         setContentView(view)
         init()
         dbManager.readDataFromDb()
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         if(item.itemId == R.id.id_new_ads){
             val i = Intent(this, EditAdsAct::class.java)
             startActivity(i)
         }
         return super.onOptionsItemSelected(item)
     }

     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.main_menu, menu)
         return super.onCreateOptionsMenu(menu)
     }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

         if(requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE){
             //Log.d("MyLog", "Sign in result")
             val task = GoogleSignIn.getSignedInAccountFromIntent(data)

             try {
                 val account = task.getResult(ApiException::class.java)
                 if(account != null){
                     dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                 }
             }catch (e:ApiException){
                 Log.d("MyLog", "Api error: ${e.message}")
             }
         }
         super.onActivityResult(requestCode, resultCode, data)
     }

     override fun onStart() {
         super.onStart()
         uiUpdate(mAuth.currentUser)
     }

     private fun init() {
         setSupportActionBar(rootElement.mainContent.toolbar)
         val toggle = ActionBarDrawerToggle(
             this,
             rootElement.driverLayout,
             rootElement.mainContent.toolbar,
             R.string.open,
             R.string.close
         )
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
                dialogHelper.accHelper.signOutGoogle()
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