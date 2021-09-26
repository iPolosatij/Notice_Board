package space.digitallab.noticeboard.fragments

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.adapters.SelectImageRvAdapter
import space.digitallab.noticeboard.dialoghelper.ProgressDialog
import space.digitallab.noticeboard.utils.AdapterCallBack
import space.digitallab.noticeboard.utils.ImageManager
import space.digitallab.noticeboard.utils.ImagePiker
import space.digitallab.noticeboard.utils.ItemTouchMoveCallback

class ImageListFragment(private val fragmentCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>?) : BaseSelectImageFragment(), AdapterCallBack {


    val adapter = SelectImageRvAdapter(this)
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHealper = ItemTouchHelper(dragCallback)
    private var job: Job? = null
    private var addItem: MenuItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        binding.apply {
            touchHealper.attachToRecyclerView(rcViewSelectImage)
            rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
            rcViewSelectImage.adapter = adapter
            if(newList != null) resizeSelectedImage(newList, true)
        }


    }

    override fun onItemDelete() {
        addItem?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList : List<Bitmap>){
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseInterface.onFragmentClose(adapter.mainArray)
        job?.cancel()
    }

    private fun resizeSelectedImage(newList: ArrayList<String>, needClear: Boolean){
        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity as Activity)
            val bitmapList = ImageManager.imageResize(newList)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
            if(adapter.mainArray.size == ImagePiker.MAX_IMAGE_COUNT) addItem?.isVisible = false
        }
    }

    private fun setUpToolbar() {

        binding.apply {

            tb.inflateMenu(R.menu.menu_choose_image)
            val deleteItem = tb.menu.findItem(R.id.delete_image)
            addItem = tb.menu.findItem(R.id.add_image)

            tb.setNavigationOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this@ImageListFragment)?.commit()
            }
            deleteItem.setOnMenuItemClickListener {
                adapter.updateAdapter(ArrayList(), true)
                addItem?.isVisible = true
                true
            }
            addItem?.setOnMenuItemClickListener {
                val imageCount = ImagePiker.MAX_IMAGE_COUNT - adapter.mainArray.size
                ImagePiker.getImages(
                    activity as AppCompatActivity,
                    imageCount,
                    ImagePiker.REQUEST_CODE_GET_IMAGES
                )
                true
            }
        }
    }

    fun updateAdapter(newList : ArrayList<String>){ resizeSelectedImage(newList, false) }

    fun setSingleImage(uri : String, position : Int){
        val pBar = binding.rcViewSelectImage[position].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(listOf(uri))
            pBar.visibility = View.GONE
            adapter.mainArray[position] = bitmapList[0]
            adapter.notifyItemChanged(position)
        }
    }
}