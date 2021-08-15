package space.digitallab.noticeboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.databinding.ListImageFragmentBinding
import space.digitallab.noticeboard.utils.ImagePiker
import space.digitallab.noticeboard.utils.ItemTouchMoveCallback

class ImageListFragment(private val fragmentCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>) : Fragment() {

    lateinit var rootElement : ListImageFragmentBinding
    val adapter = SelectImageRvAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHealper = ItemTouchHelper(dragCallback)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootElement = ListImageFragmentBinding.inflate(inflater)
        return rootElement.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        rootElement.rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
        rootElement.rcViewSelectImage.adapter = adapter
        touchHealper.attachToRecyclerView(rootElement.rcViewSelectImage)

        var updateList = ArrayList<SelectImageItem>()
        for(n in 0 until newList.size){
            updateList.add(SelectImageItem((n + 1).toString(), newList[n]))
        }
        adapter.updateAdapter(updateList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseInterface.onFragmentClose(adapter.mainArray)

    }

    private fun setUpToolbar(){
        rootElement.tb.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = rootElement.tb.menu.findItem(R.id.delete_image)
        val addItem = rootElement.tb.menu.findItem(R.id.add_image)

        rootElement.tb.setNavigationOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            true
        }
        addItem.setOnMenuItemClickListener {
            if(adapter.mainArray.size < ImagePiker.MAX_IMAGE_COUNT) {
                val imageCount = ImagePiker.MAX_IMAGE_COUNT - adapter.mainArray.size
                ImagePiker.getImages(activity as AppCompatActivity, imageCount)
            }
            true
        }
    }

    fun updateAdapter(newList : ArrayList<String>){
        var updateList = ArrayList<SelectImageItem>()
        for(n in adapter.mainArray.size until newList.size + adapter.mainArray.size){
            updateList.add(SelectImageItem("Фото - " + (n + 1), newList[n - adapter.mainArray.size]))
        }
        adapter.updateAdapter(updateList, false)
    }

}