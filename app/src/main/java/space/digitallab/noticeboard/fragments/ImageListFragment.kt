package space.digitallab.noticeboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.utils.ItemTouchMoveCallback

class ImageListFragment(private val fragmentCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>) : Fragment() {

    val adapter = SelectImageRvAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHealper = ItemTouchHelper(dragCallback)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectImage)
        touchHealper.attachToRecyclerView(rcView)
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter
        var updateList = ArrayList<SelectImageItem>()
        for(n in 0 until newList.size){
            updateList.add(SelectImageItem((n + 1).toString(), newList[n]))
        }
        adapter.updateAdapter(updateList)
        btnBack.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseInterface.onFragmentClose()
        Toast.makeText(activity, "ссылки \n" +
                "Title1 : ${adapter.mainArray[0].title} \n" +
                " Title2 : ${adapter.mainArray[1].title}\n" +
                " Title3 : ${adapter.mainArray[2].title}", Toast.LENGTH_LONG).show()
    }
}