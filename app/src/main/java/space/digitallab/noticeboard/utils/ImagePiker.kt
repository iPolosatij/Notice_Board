package space.digitallab.noticeboard.utils

import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix



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

}