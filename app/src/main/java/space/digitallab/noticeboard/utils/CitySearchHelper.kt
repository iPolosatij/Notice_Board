package space.digitallab.noticeboard.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object CitySearchHelper {
    fun getAllCountries(context: Context):ArrayList<String>{
        var tempArray = ArrayList<String>()

        try {
            val inputStream:InputStream = context.assets.open("countriesToCities.json")
            val size : Int = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val countriesNames = jsonObject.names()
            if(countriesNames != null) {
                for (n in 0 until countriesNames.length()) {
                    tempArray.add(countriesNames.getString(n))
                }
            }
        }catch (e: IOException){

        }

        return tempArray
    }
}