package space.digitallab.noticeboard.act

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.databinding.ActivityEditAdsBinding
import space.digitallab.noticeboard.dialogs.DialogSpinnerHelper
import space.digitallab.noticeboard.fragments.FragmentCloseInterface
import space.digitallab.noticeboard.fragments.ImageListFragment
import space.digitallab.noticeboard.utils.CitySearchHelper
import space.digitallab.noticeboard.utils.ImagePiker


class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {
    lateinit var rootElement: ActivityEditAdsBinding
    private val dialog = DialogSpinnerHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePiker.REQUEST_CODE_GET_IMAGES) {
            if(data != null) {
                val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                Toast.makeText(this, "Images : " +
                        "${returnValue?.get(0)} \n " +
                        "${returnValue?.get(1)} \n" +
                        "${returnValue?.get(2)} ", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    ImagePiker.getImages(this)
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

    fun onClickGetImages(view: View){
        ImagePiker.getImages(this)
        rootElement.scrolViewMine.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction().replace(
            R.id.place_holder, ImageListFragment(
                this
            )
        ).commit()
    }

    override fun onFragmentClose() {
        rootElement.scrolViewMine.visibility = View.VISIBLE
    }
}