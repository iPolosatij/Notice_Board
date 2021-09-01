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
import space.digitallab.noticeboard.adapters.SelectImageRvAdapter
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
        adapter.updateAdapter(newList, true)
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
                ImagePiker.getImages(activity as AppCompatActivity, imageCount, ImagePiker.REQUEST_CODE_GET_IMAGES)
            }
            true
        }
    }

    fun updateAdapter(newList : ArrayList<String>){

        adapter.updateAdapter(newList, false)
    }

    fun setSingleImage(uri : String, position : Int){

        adapter.mainArray[position] = uri
        adapter.notifyDataSetChanged()
    }

}