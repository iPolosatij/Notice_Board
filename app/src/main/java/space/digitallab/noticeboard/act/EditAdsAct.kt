package space.digitallab.noticeboard.act

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import space.digitallab.noticeboard.MainActivity
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.adapters.ImageAdapter
import space.digitallab.noticeboard.databinding.ActivityEditAdsBinding
import space.digitallab.noticeboard.dialogs.DialogSpinnerHelper
import space.digitallab.noticeboard.fragments.FragmentCloseInterface
import space.digitallab.noticeboard.fragments.ImageListFragment
import space.digitallab.noticeboard.model.DbManager
import space.digitallab.noticeboard.model.Notice
import space.digitallab.noticeboard.utils.CitySearchHelper
import space.digitallab.noticeboard.utils.ImageManager
import space.digitallab.noticeboard.utils.ImagePiker
import java.io.ByteArrayOutputStream


class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {
    var chooseImageFragment : ImageListFragment? = null
    lateinit var binding: ActivityEditAdsBinding
    private val dialog = DialogSpinnerHelper()
    lateinit var imageAdapter : ImageAdapter
    private val dbManager = DbManager()
    var editImagePosition = 0
    private var imageIndex = 0
    private var isEditState = false
    private var notice: Notice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        checkEditState()
        imageChangeCounter()
    }

    private fun checkEditState(){
        isEditState = isEditState()
        if (isEditState){
            notice = intent.getSerializableExtra(MainActivity.NOTICES_DATA) as Notice
            notice?.let { fillViews(it)}
        }
    }

    private fun isEditState(): Boolean{
        return intent.getBooleanExtra(MainActivity.EDIT_STATE, false)
    }

    private fun fillViews(notice: Notice) = with(binding){
        tvCountry.text = notice.country
        tvCity.text = notice.city
        tvTel.setText(notice.tel)
        tvIndex.setText(notice.index)
        withSend.isChecked = notice.withSend.toBoolean()
        tvCategory.text = notice.category
        tvNoticeTitle.setText(notice.title)
        tvPrice.setText(notice.price)
        tvDescription.setText(notice.description)
        tvEmail.setText(notice.email)
        ImageManager.fillImageArray(notice, imageAdapter)
    }

    private fun init(){
        dialog.init(this)
        imageAdapter = ImageAdapter()
        binding.vpImages.adapter = imageAdapter
    }

    //OnClicks
    fun onClickSelectCountry(view: View){
        val listCountry = CitySearchHelper.getAllCountries(this)
        dialog.showSpinnerDialog(listCountry, binding.tvCountry)
        if(binding.tvCity.text.toString() != getString(R.string.select_city)){
            binding.tvCity.setText(R.string.select_city)
        }
    }

    fun onClickSelectCity(view: View){
        val selectedCountry = binding.tvCountry.text.toString()
        if (selectedCountry != getString(R.string.select_country)) {
            val listCities = CitySearchHelper.getAllCities(selectedCountry, this)
            dialog.showSpinnerDialog(listCities, binding.tvCity)
        } else {
            Toast.makeText(this, getString(R.string.no_country_selected), Toast.LENGTH_LONG).show()
        }
    }

    fun onClickSelectCategory(view: View){
        val listCategory = resources.getStringArray(R.array.categories).toMutableList() as ArrayList
        dialog.showSpinnerDialog( listCategory, binding.tvCategory)
    }

    fun onClickGetImages(view: View){
        if(imageAdapter.mainArray.size == 0){
            ImagePiker.getMultiImages(this, ImagePiker.MAX_IMAGE_COUNT)
        }else{
            openChooseImageFragment(null)
            chooseImageFragment?.updateAdapterFromEdit(imageAdapter.mainArray)
        }
    }

    fun onClickPublish(view: View){
        notice = fillNotice()
        if (isEditState) {
            notice?.copy(key = this.notice?.key)?.let { dbManager.publishNotice(it, onPublishFinish()) }
        }else {
            upLoadAllImages()
        }

    }

    fun onPublishFinish(): DbManager.FinishWorkListener{
        return object : DbManager.FinishWorkListener{
            override fun onFinish() {
               finish()
            }
        }
    }

    private fun fillNotice(): Notice{
        val notice: Notice
        binding.apply {
            notice = Notice(
                tvCountry.text.toString(),
                tvCity.text.toString(),
                tvTel.text.toString(),
                tvIndex.text.toString(),
                withSend.isChecked.toString(),
                tvNoticeTitle.text.toString(),
                tvCategory.text.toString(),
                tvPrice.text.toString(),
                tvDescription.text.toString(),
                tvEmail.text.toString(),
                "Empty",
                "Empty",
                "Empty",
                dbManager.db.push().key,
                dbManager.auth.uid
            )
        }
        return notice
    }

    override fun onFragmentClose(list : ArrayList<Bitmap>) {
        binding.scrollViewMine.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFragment = null
    }

    fun openChooseImageFragment(newList : ArrayList<Uri>?){
        chooseImageFragment = ImageListFragment(this)
        newList?.let { chooseImageFragment?.resizeSelectedImage(it, true, this) }
        binding.scrollViewMine.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFragment!!)
        fm.commit()
    }

    private fun upLoadAllImages(){
        if (imageAdapter.mainArray.size == imageIndex) {
            notice?.let { dbManager.publishNotice(it, onPublishFinish()) }
            return
        }
        upLoadImage(prepareImageToArray(imageAdapter.mainArray[imageIndex])){uri->
            //notice?.let { dbManager.publishNotice(it, onPublishFinish()) }
            nextImage(uri.result.toString())
        }
    }

    private fun nextImage(uri: String){
        setImageUriToNotice(uri)
        imageIndex++
        upLoadAllImages()
    }

    private fun setImageUriToNotice(uri: String){
        when(imageIndex){
            0 -> notice = notice?.copy(mainImageUri = uri)
            1 -> notice = notice?.copy(secondImageUri = uri)
            2 -> notice = notice?.copy(thirdImageUri = uri)
        }
    }

    private fun prepareImageToArray(bitmap: Bitmap): ByteArray{
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream)
        return outStream.toByteArray()
    }

    private fun upLoadImage(byteArray: ByteArray, listener: OnCompleteListener<Uri>){
        val imStorageReference = dbManager.auth.uid?.let { uid ->
            dbManager.dbStorage.child(uid).child("image_${System.currentTimeMillis()}")
        }
        val uploadTask = imStorageReference?.putBytes(byteArray)
        uploadTask?.continueWithTask { task -> imStorageReference.downloadUrl
        }?.addOnCompleteListener(listener)
    }

    private fun imageChangeCounter(){
        binding.vpImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val imageCounter = "${position+1}/${binding.vpImages.adapter?.itemCount}"
                binding.imageCounter.text = imageCounter
            }
        })
    }
}