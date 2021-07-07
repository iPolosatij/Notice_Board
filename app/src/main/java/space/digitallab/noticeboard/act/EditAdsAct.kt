package space.digitallab.noticeboard.act

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import space.digitallab.noticeboard.databinding.ActivityEditAdsBinding
import space.digitallab.noticeboard.utils.CitySearchHelper

class EditAdsAct : AppCompatActivity() {
    private lateinit var rootElement: ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            CitySearchHelper.getAllCountries(this))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootElement.spCountry.adapter = adapter
    }
}