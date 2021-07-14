package space.digitallab.noticeboard.utils

import android.content.Context
import org.json.JSONObject
import space.digitallab.noticeboard.R
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

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

    fun filterListData(list: ArrayList<String>, searchText : String?, context: Context) : ArrayList<String> {
        val tempList = ArrayList<String>()
        tempList.clear()

        if(searchText == null){
            tempList.add(context.resources.getString(R.string.no_result))
            return tempList
        }
        for (selection: String in list) {
            if(selection.toLowerCase(Locale.ROOT).startsWith(searchText.toLowerCase(Locale.ROOT)))
            tempList.add(selection)
        }
        if(tempList.size == 0){
            tempList.add(context.resources.getString(R.string.no_result))
        }
        return tempList
    }
}