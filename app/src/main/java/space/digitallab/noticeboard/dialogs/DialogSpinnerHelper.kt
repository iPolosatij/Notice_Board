package space.digitallab.noticeboard.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.act.EditAdsAct
import space.digitallab.noticeboard.utils.CitySearchHelper

class DialogSpinnerHelper {

    private lateinit var act : Context

    fun  init(context: Context){
        act = (context as EditAdsAct)
    }

    fun showSpinnerDialog(list: ArrayList<String>) {
        val builder = AlertDialog.Builder(act)
        val dialog = builder.create()
        val rootView = LayoutInflater.from(act).inflate(R.layout.spinner_layout, null)
        val adapter = RcViewDialogSpinnerAdapter(act, dialog)
        val rcView = rootView.findViewById<RecyclerView>(R.id.rcSpView)
        val sv = rootView.findViewById<SearchView>(R.id.svSpinner)
        rcView.layoutManager = LinearLayoutManager(act)
        rcView.adapter = adapter
        dialog.setView(rootView)
        adapter.updateAdapter(list)
        setSearchView(adapter, list, sv)
        dialog.show()

    }

    private fun setSearchView(adapter: RcViewDialogSpinnerAdapter, list: ArrayList<String>, sv: SearchView?) {

        sv?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempList = CitySearchHelper.filterListData(list, newText, act)
                adapter.updateAdapter(tempList)
               return true
            }
        })
    }
}