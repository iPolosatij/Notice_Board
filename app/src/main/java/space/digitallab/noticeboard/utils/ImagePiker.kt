package space.digitallab.noticeboard.utils

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.act.EditAdsAct


object ImagePiker {
    const val MAX_IMAGE_COUNT = 3

    fun getOptions(imageCounter: Int): Options {
        return Options().apply {
            count = imageCounter
            isFrontFacing = false
            mode = Mode.Picture
            path = "/pix/images"
        }
    }

    fun getMultiImages(context: EditAdsAct, imageCounter: Int) {
        context.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    getMultiSelectedImages(context, result.data)
                }
            }
        }
    }

    fun addImages(context: EditAdsAct, imageCounter: Int) {

        context.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    openChoseImageFragment(context)
                    context.chooseImageFragment?.updateAdapter(result.data as ArrayList<Uri>, context)
                }
            }
        }
    }

    fun getSingleImage(context: EditAdsAct) {
        val f = context.chooseImageFragment
        context.addPixToActivity(R.id.place_holder, getOptions(1)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    context.chooseImageFragment =  f
                    openChoseImageFragment(context)
                    singleImage(context, result.data[0])
                }
            }
        }
    }

    private  fun openChoseImageFragment(context: EditAdsAct){
        context.chooseImageFragment?.let {
            context.supportFragmentManager.beginTransaction().replace(R.id.place_holder, it).commit()
        }
    }
    private fun closePixFragment(context: EditAdsAct) {
        context.supportFragmentManager.fragments.forEach { fragment ->
            if (fragment.isVisible) context
                .supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commit()
        }
    }

    fun getMultiSelectedImages(context: EditAdsAct, uris: List<Uri>) {
        if (uris.size > 1 && context.chooseImageFragment == null) {
            context.openChooseImageFragment(uris as ArrayList<Uri>)
        } else if (uris.size == 1 && context.chooseImageFragment == null) {
            CoroutineScope(Dispatchers.Main).launch {
                context.binding.pBarLoad.visibility = View.VISIBLE
                val bitmapArray = ImageManager.imageResize(uris, context) as ArrayList<Bitmap>
                context.binding.pBarLoad.visibility = View.GONE
                context.imageAdapter.update(bitmapArray)
                closePixFragment(context)
            }
        }
    }

    private fun singleImage(context: EditAdsAct, uri: Uri) {
        context.chooseImageFragment?.setSingleImage(uri, context.editImagePosition)
    }
}
