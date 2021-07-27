package space.digitallab.noticeboard.utils

import android.app.Activity
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import java.io.File
import java.io.InputStream

object ImageManager {

    private const val MAX_IMAGE_SIZE = 1000
    private const val WIDTH = 0
    private const val HEIGHT = 1

    fun getImageSize(uri : Uri, act : Activity) : List<Int>{
        val inStream = act.contentResolver.openInputStream(uri)
        val fTemp = File(act.cacheDir, "temp.tmp")
        if (inStream != null) {
            fTemp.copyInStreamToFile(inStream)
        }

        val options = BitmapFactory.Options().apply {

        }
        BitmapFactory.decodeFile(fTemp.path, options)

        return if(imageRotation(fTemp) == 90)
            listOf(options.outHeight, options.outWidth)

        else listOf(options.outWidth, options.outHeight)
    }

    private fun imageRotation(file: File) : Int{

        val rotation : Int

        val exif = ExifInterface(file.absolutePath)
        //val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.)
        rotation = 1
        return rotation
    }



    private fun File.copyInStreamToFile(inStream: InputStream){
        this.outputStream().use {
            out -> inStream.copyTo(out)
        }
    }
}