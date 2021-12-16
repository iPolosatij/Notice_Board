package space.digitallab.noticeboard.utils

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.act.EditAdsAct


object ImagePiker {
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    const val MAX_IMAGE_COUNT = 3

    fun getOptions(imageCounter: Int): Options {
        return Options().apply {
            count = imageCounter
            isFrontFacing = false
            mode = Mode.Picture
            path = "/pix/images"
        }
    }

    fun launcher(context: EditAdsAct, launcher: ActivityResultLauncher<Intent>?, imageCounter: Int){
        context.addPixToActivity(R.id.place_holder, getOptions(imageCounter)){
            when (it.status) {
                PixEventCallback.Status.SUCCESS -> {
                    context.supportFragmentManager.fragments.forEach{fragment ->
                        if(fragment.isVisible) context
                            .supportFragmentManager
                            .beginTransaction()
                            .remove(fragment)
                            .commit()
                    }

                }
                    //PixEventCallback.Status.BACK_PRESSED -> // back pressed called
            }
        }
    }

    fun getLauncherForMultiSelectGetImages(context: EditAdsAct): ActivityResultLauncher<Intent> {
        return context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            /*if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if(result.data != null) {
                    val returnValues = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    if(returnValues?.size!! > 1 && context.chooseImageFragment == null) {
                        context.openChooseImageFragment(returnValues)
                    } else if(returnValues.size == 1 && context.chooseImageFragment == null){
                        CoroutineScope(Dispatchers.Main).launch{
                            context.rootElement.pBarLoad.visibility = View.VISIBLE
                            val bitmapArray = ImageManager.imageResize(returnValues) as ArrayList<Bitmap>
                            context.rootElement.pBarLoad.visibility = View.GONE
                            context.imageAdapter.update(bitmapArray) }
                    } else if (context.chooseImageFragment != null) {
                       context.chooseImageFragment?.updateAdapter(returnValues)
                    }
                }
            }*/
        }
    }

    fun getLauncherForSingleImage(context: EditAdsAct ): ActivityResultLauncher<Intent> {
        return context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
          /*  if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val uris = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                context.chooseImageFragment?.setSingleImage(
                    uris?.get(0)!!,
                    context.editImagePosition
                )
            }*/
        }
    }
}