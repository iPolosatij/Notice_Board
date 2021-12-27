package space.digitallab.noticeboard.act

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.digitallab.noticeboard.adapters.ImageAdapter
import space.digitallab.noticeboard.constants.CallConstants
import space.digitallab.noticeboard.databinding.ActivityReadNoticeBinding
import space.digitallab.noticeboard.model.Notice
import space.digitallab.noticeboard.utils.ImageManager

class ReadNoticeAct : AppCompatActivity() {
    lateinit var binding: ActivityReadNoticeBinding
    lateinit var adapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init(){
        adapter = ImageAdapter()
        binding.apply {
            vpNoticeImage.adapter = adapter
        }
        getIntentForAction()
    }

    private fun getIntentForAction(){
        val notice = intent.getSerializableExtra(CallConstants.NOTICE) as Notice
        fillImageArray(notice)
    }

    private fun fillImageArray(notice: Notice){
        val listUris = listOf(notice.mainImageUri, notice.secondImageUri, notice.thirdImageUri)
        CoroutineScope(Dispatchers.Main).launch {
            val bitmapList = ImageManager.getBitmapsFromUris(listUris)
            adapter.update(bitmapList as ArrayList<Bitmap>)
        }
    }
}