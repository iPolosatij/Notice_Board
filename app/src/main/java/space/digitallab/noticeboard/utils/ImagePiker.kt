package space.digitallab.noticeboard.utils

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.act.EditAdsAct

object ImagePiker {

   private fun getOptions(imageCounter : Int) : Options{

       val options = Options().apply{

           count = imageCounter                                        //Number of images to restrict selection count
           path = "/pix/images"                                        //Custom Path For media Storage
           isFrontFacing = false                                       //Front Facing camera on start
           mode = Mode.Picture                                         //Option to select only pictures or videos or both
           //flash = Flash.Auto                                        //Option to select flash type
           //preSelectedUrls = ArrayList<Uri>()                        //Pre selected Image Urls
           //ratio = Ratio.RATIO_AUTO                                  //Image/video capture ratio
           //spanCount = 4                                             //Number for columns in grid
           //videoDurationLimitInSeconds = 10                          //Duration for video recording
       }
       return options
    }

    fun launcher(edAct: EditAdsAct, Launcher: ActivityResultLauncher<Intent>?, imageCounter: Int) {
       edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    val fList = edAct.supportFragmentManager.fragments
                    fList.forEach {
                        if (it.isVisible) edAct.supportFragmentManager.beginTransaction().remove(it).commit()
                    }
                }//use results as it.data
                   // PixEventCallback.Status.BACK_PRESSED -> // back pressed called
            }
        }
    }

    fun getImages() {

    }
}