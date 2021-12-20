package space.digitallab.noticeboard.fragments

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.act.EditAdsAct
import space.digitallab.noticeboard.adapters.SelectImageRvAdapter
import space.digitallab.noticeboard.databinding.ListImageFragmentBinding
import space.digitallab.noticeboard.dialoghelper.ProgressDialog
import space.digitallab.noticeboard.utils.AdapterCallBack
import space.digitallab.noticeboard.utils.ImageManager
import space.digitallab.noticeboard.utils.ImagePiker
import space.digitallab.noticeboard.utils.ItemTouchMoveCallback

class ImageListFragment(private val fragmentCloseInterface : FragmentCloseInterface) : BaseAdsFragment(), AdapterCallBack {

    lateinit var binding : ListImageFragmentBinding
    val adapter = SelectImageRvAdapter(this)
    private val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private var job: Job? = null
    private var addItem: MenuItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ListImageFragmentBinding.inflate(layoutInflater)
        adView = binding.adView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        binding.apply {
            touchHelper.attachToRecyclerView(rcViewSelectImage)
            rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
            rcViewSelectImage.adapter = adapter
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

    override fun onClose() {
        super.onClose()
        activity?.supportFragmentManager?.beginTransaction()?.remove(this@ImageListFragment)?.commit()
    }

    fun resizeSelectedImage(newList: ArrayList<Uri>, needClear: Boolean, act: Activity){
        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(act)
            val bitmapList = ImageManager.imageResize(newList, act)
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
            if(adapter.mainArray.size == ImagePiker.MAX_IMAGE_COUNT) addItem?.isVisible = false

            tb.setNavigationOnClickListener {
                showInterAd()
            }
            deleteItem.setOnMenuItemClickListener {
                adapter.updateAdapter(ArrayList(), true)
                addItem?.isVisible = true
                true
            }
            addItem?.setOnMenuItemClickListener {
                val imageCount = ImagePiker.MAX_IMAGE_COUNT - adapter.mainArray.size
               ImagePiker.addImages(activity as EditAdsAct, imageCount)
                true
            }
        }
    }

    fun updateAdapter(newList : ArrayList<Uri>, act: Activity){
        resizeSelectedImage(newList, false, act)
    }

    fun setSingleImage(uri : Uri, position : Int){
        val pBar = binding.rcViewSelectImage[position].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(arrayListOf(uri), activity as Activity)
            pBar.visibility = View.GONE
            adapter.mainArray[position] = bitmapList[0]
            adapter.notifyItemChanged(position)
        }
    }
}