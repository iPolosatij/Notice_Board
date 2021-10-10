package space.digitallab.noticeboard.act

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fxn.utility.PermUtil
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.adapters.ImageAdapter
import space.digitallab.noticeboard.data.Notice
import space.digitallab.noticeboard.database.DbManager
import space.digitallab.noticeboard.databinding.ActivityEditAdsBinding
import space.digitallab.noticeboard.dialogs.DialogSpinnerHelper
import space.digitallab.noticeboard.fragments.FragmentCloseInterface
import space.digitallab.noticeboard.fragments.ImageListFragment
import space.digitallab.noticeboard.utils.CitySearchHelper
import space.digitallab.noticeboard.utils.ImagePiker


class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {
    var chooseImageFragment : ImageListFragment? = null
    lateinit var rootElement: ActivityEditAdsBinding
    private val dialog = DialogSpinnerHelper()
    lateinit var imageAdapter : ImageAdapter
    var editImagePosition = 0
    private val dbManager = DbManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImagePiker.showSelectedImages(resultCode, requestCode, data, this)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    ImagePiker.getImages(this, 3, ImagePiker.REQUEST_CODE_GET_IMAGES)
                } else {
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    private fun init(){
        dialog.init(this)
        imageAdapter = ImageAdapter()
        rootElement.vpImages.adapter = imageAdapter
    }

    //OnClicks
    fun onClickSelectCountry(view: View){
        val listCountry = CitySearchHelper.getAllCountries(this)
        dialog.showSpinnerDialog(listCountry, rootElement.tvCountry)
        if(rootElement.tvCity.text.toString() != getString(R.string.select_city)){
            rootElement.tvCity.setText(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: View){
        val selectedCountry = rootElement.tvCountry.text.toString()
        if (selectedCountry != getString(R.string.select_country)) {
            val listCities = CitySearchHelper.getAllCities(selectedCountry, this)
            dialog.showSpinnerDialog(listCities, rootElement.tvCity)
        } else {
            Toast.makeText(this, getString(R.string.no_country_selected), Toast.LENGTH_LONG).show()
        }
    }

    fun onClickSelectCategory(view: View){
        val listCategory = resources.getStringArray(R.array.categories).toMutableList() as ArrayList
        dialog.showSpinnerDialog( listCategory, rootElement.tvCategory)
    }

    fun onClickGetImages(view: View){
        if(imageAdapter.mainArray.size == 0){
            ImagePiker.getImages(this, 3, ImagePiker.REQUEST_CODE_GET_IMAGES)
        }else{
            openChooseImageFragment(null)
            chooseImageFragment?.updateAdapterFromEdit(imageAdapter.mainArray)
        }
    }

    fun onClickPublish(view: View){
        dbManager.publishNotice(fillNotice())
    }

    private fun fillNotice(): Notice{
        val notice: Notice
        rootElement.apply {
            notice = Notice(
                tvCountry.text.toString(),
                tvCity.text.toString(),
                tvTel.text.toString(),
                tvIndex.text.toString(),
                withSend.isChecked.toString(),
                tvCategory.text.toString(),
                tvPrice.text.toString(),
                tvDescription.text.toString(),
                dbManager.db.push().key
            )
        }
        return notice
    }

    override fun onFragmentClose(list : ArrayList<Bitmap>) {
        rootElement.scrollViewMine.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFragment = null
    }

    fun openChooseImageFragment(newList : ArrayList<String>?){

        chooseImageFragment = ImageListFragment(this, newList)
        rootElement.scrollViewMine.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFragment!!)
        fm.commit()
    }
}