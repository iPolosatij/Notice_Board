 package space.digitallab.noticeboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import space.digitallab.noticeboard.accounthelper.AccountHelper
import space.digitallab.noticeboard.act.EditAdsAct
import space.digitallab.noticeboard.adapters.NoticeRcAdapter
import space.digitallab.noticeboard.databinding.ActivityMainBinding
import space.digitallab.noticeboard.dialoghelper.DialogConst
import space.digitallab.noticeboard.dialoghelper.DialogHelper
import space.digitallab.noticeboard.dialoghelper.GoogleAccConst
import space.digitallab.noticeboard.model.Notice
import space.digitallab.noticeboard.viewModel.FirebaseViewModel


 class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, NoticeRcAdapter.ActionListener {

     private lateinit var tvAccount: TextView
     private lateinit var rootElement: ActivityMainBinding
     private val dialogHelper = DialogHelper(this)
     val mAuth = Firebase.auth
     val adapter = NoticeRcAdapter(this)
     private val firebaseViewModel: FirebaseViewModel by viewModels()

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         rootElement = ActivityMainBinding.inflate(layoutInflater)
         val view = rootElement.root
         setContentView(view)
         init()
         initRecyclerView()
         observeVm()
         firebaseViewModel.loadAllNotice()
         bottomMenuOnClick()
     }

     override fun onResume() {
         super.onResume()
         rootElement.mainContent.bottonNavView.selectedItemId = R.id.home
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

     private fun bottomMenuOnClick() = with(rootElement.mainContent) {
         this.bottonNavView.setOnNavigationItemSelectedListener { item ->
             when (item.itemId) {
                 R.id.newNotice -> {
                     val i = Intent(this@MainActivity, EditAdsAct::class.java)
                     startActivity(i)
                 }

                 R.id.myNotice -> {

                     firebaseViewModel.loadMyNotice()
                     this.toolbar.title = getString(R.string.my_ads)
                 }

                 R.id.favoriteNotice -> {
                     firebaseViewModel.loadMyFavoriteNotice()
                     this.toolbar.title = getString(R.string.my_favorite)
                 }

                 R.id.home -> {

                     firebaseViewModel.loadAllNotice()
                     this.toolbar.title = getString(R.string.default_list)
                 }
             }
             true
         }
     }

     private fun initRecyclerView(){
         rootElement.apply {
             mainContent.rvNoticeList.layoutManager = LinearLayoutManager(this@MainActivity)
             mainContent.rvNoticeList.adapter = adapter

         }
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
                if (mAuth.currentUser?.isAnonymous == true) {
                    rootElement.driverLayout.closeDrawer(GravityCompat.START)
                    return true
                }
                uiUpdate(null)
                mAuth.signOut()
                dialogHelper.accHelper.signOutGoogle()
            }

        }

        rootElement.driverLayout.closeDrawer(GravityCompat.START)
        return true
    }

     fun uiUpdate(firebaseUser: FirebaseUser?) {
         when{
             firebaseUser == null -> {
                 dialogHelper.accHelper.signInAnonymously(object : AccountHelper.Listener {
                     override fun onComplete() {
                         tvAccount.text = getString(R.string.guest)
                     }
                 })
             }
             firebaseUser.isAnonymous -> {
                 tvAccount.text = getString(R.string.guest)
             }
             !firebaseUser.isAnonymous -> {
                 tvAccount.text = firebaseUser.email
             }
         }
     }

     private fun observeVm() {
         firebaseViewModel.noticeData.observe(this, { dataList ->
             adapter.updateAdapter(dataList)
             rootElement.mainContent.isEmpty.visibility =
                 if (dataList.isEmpty()) View.VISIBLE
                 else View.GONE
         })
     }

     companion object{
         const val EDIT_STATE = "edit_state"
         const val NOTICES_DATA = "notices_data"
     }

     override fun onDeleteItem(notice: Notice) {
         firebaseViewModel.deleteItem(notice)
     }

     override fun onNoticeViewed(notice: Notice) {
         firebaseViewModel.noticeViewed(notice)
     }

     override fun onFavoriteClick(notice: Notice) {
         firebaseViewModel.favoriteClick(notice)
     }
 }