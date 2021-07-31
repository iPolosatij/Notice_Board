package space.digitallab.noticeboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.digitallab.noticeboard.R

class ImageListFragment(private val fragmentCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>) : Fragment() {

    val adapter = SelectImageRvAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectImage)
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
    }
}