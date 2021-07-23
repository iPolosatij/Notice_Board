package space.digitallab.noticeboard.act

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.databinding.ActivityEditAdsBinding
import space.digitallab.noticeboard.dialogs.DialogSpinnerHelper
import space.digitallab.noticeboard.fragments.FragmentCloseInterface
import space.digitallab.noticeboard.fragments.ImageListFragment
import space.digitallab.noticeboard.utils.CitySearchHelper

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
        rootElement.scrolViewMine.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction().replace(R.id.place_holder, ImageListFragment(this)).commit()
    }

    override fun onFragmentClose() {
        rootElement.scrolViewMine.visibility = View.VISIBLE
    }
}