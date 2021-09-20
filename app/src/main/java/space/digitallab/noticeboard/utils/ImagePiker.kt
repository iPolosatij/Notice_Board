package space.digitallab.noticeboard.utils

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.digitallab.noticeboard.act.EditAdsAct


object ImagePiker {
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    const val MAX_IMAGE_COUNT = 3

    fun getImages(act:AppCompatActivity, imageCounter: Int, rCode : Int){
        val options = Options.init()
                .setRequestCode(rCode)                       //Request code for activity results
                .setCount(imageCounter)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setMode(Options.Mode.Picture)                                 //Option to select only pictures or videos or both
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images")                                        //Custom Path For media Storage

        Pix.start(act, options)
    }

    fun showSelectedImages(resultCode: Int, requestCode: Int, data: Intent?, edAct: EditAdsAct ){
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGES) {

            if(data != null) {

                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                if(returnValues?.size!! > 1 && edAct.chooseImageFragment == null) {
                    edAct.openChooseImageFragment(returnValues)
                } else if(returnValues.size == 1 && edAct.chooseImageFragment == null){
                    CoroutineScope(Dispatchers.Main).launch{
                        edAct.rootElement.pBarLoad.visibility = View.VISIBLE
                        val bitmapArray = ImageManager.imageResize(returnValues) as ArrayList<Bitmap>
                        edAct.rootElement.pBarLoad.visibility = View.GONE
                        edAct.imageAdapter.update(bitmapArray)
                    }
                } else if (edAct.chooseImageFragment != null) {
                    edAct.chooseImageFragment?.updateAdapter(returnValues)
                }
            }
        } else if(resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_SINGLE_IMAGE){

            val uris = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            edAct.chooseImageFragment?.setSingleImage(uris?.get(0)!!, edAct.editImagePosition)
        }
    }

}