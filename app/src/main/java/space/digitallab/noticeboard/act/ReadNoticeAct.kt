package space.digitallab.noticeboard.act

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.adapters.ImageAdapter
import space.digitallab.noticeboard.constants.CallConstants
import space.digitallab.noticeboard.databinding.ActivityReadNoticeBinding
import space.digitallab.noticeboard.model.Notice
import space.digitallab.noticeboard.utils.ImageManager

class ReadNoticeAct : AppCompatActivity() {
    lateinit var binding: ActivityReadNoticeBinding
    lateinit var adapter: ImageAdapter
    private var notice: Notice? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.call.setOnClickListener { call() }
        binding.mail.setOnClickListener { writeEmail() }
    }
    private fun init(){
        adapter = ImageAdapter()
        binding.apply {
            vpNoticeImage.adapter = adapter
        }
        getIntentForAction()
        imageChangeCounter()
    }

    private fun getIntentForAction(){
        notice = intent.getSerializableExtra(CallConstants.NOTICE) as Notice
        notice?.let {updateUI(it)}
    }

    private fun updateUI(notice: Notice){
        ImageManager.fillImageArray(notice, adapter)
        fillTextViews(notice)
    }

    private fun fillTextViews(notice: Notice) = with(binding){
        title.text = notice.title
        description.text = notice.description
        price.text = notice.price
        country.text = notice.country
        city.text = notice.city
        withSent.text = isWithSent(notice.withSend.toBoolean())
        tel.text = notice.tel
    }

    private fun isWithSent(withSent: Boolean): String{
        return if(withSent) getString(R.string.withSent)
        else getString(R.string.withOutSent)
    }

    private fun call(){
        val callUri = "tel:${notice?.tel}"
        val iCall = Intent(Intent.ACTION_DIAL)
        iCall.data = callUri.toUri()
        startActivity(iCall)
    }

    private fun writeEmail(){
        val iWriteEmail = Intent(Intent.ACTION_SEND)
        iWriteEmail.type = "message/rfc822"
        iWriteEmail.apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(notice?.email))
            putExtra(Intent.EXTRA_SUBJECT, "Сообщение по обьявлению ${notice?.title}")
            putExtra(Intent.EXTRA_TEXT, "Меня заинтересовало ваше обьявление" )
        }
        try {
          startActivity(Intent.createChooser(iWriteEmail, "Open with"))
        }catch (e: ActivityNotFoundException){

        }
    }

    private fun imageChangeCounter(){
        binding.vpNoticeImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCounter = "${position+1}/${binding.vpNoticeImage.adapter?.itemCount}"
                binding.imageCounter.text = imageCounter
            }
        })
    }
}