package space.digitallab.noticeboard.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import space.digitallab.noticeboard.databinding.ActivityReadNoticeBinding

class ReadNoticeAct : AppCompatActivity() {
    lateinit var binding: ActivityReadNoticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}