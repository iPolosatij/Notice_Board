package space.digitallab.noticeboard.utils

import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.digitallab.noticeboard.act.EditAdsAct


object ImagePiker {
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    const val MAX_IMAGE_COUNT = 3

    fun getOptions(imageCounter: Int): Options {
        return Options.init()
            .setCount(imageCounter)
            .setFrontfacing(false)
            .setMode(Options.Mode.Picture)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/pix/images")
    }

    fun launcher(context: EditAdsAct, launcher: ActivityResultLauncher<Intent>?, imageCounter: Int){
        PermUtil.checkForCamaraWritePermissions(context){
            launcher?.launch(Intent(context, Pix::class.java).apply {
                putExtra("options", getOptions(imageCounter))
            })
        }
    }

    fun getLauncherForMultiSelectGetImages(context: EditAdsAct): ActivityResultLauncher<Intent> {
        return context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
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
            }
        }
    }

    fun getLauncherForSingleImage(context: EditAdsAct ): ActivityResultLauncher<Intent> {
        return context.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val uris = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                context.chooseImageFragment?.setSingleImage(
                    uris?.get(0)!!,
                    context.editImagePosition
                )
            }
        }
    }
}