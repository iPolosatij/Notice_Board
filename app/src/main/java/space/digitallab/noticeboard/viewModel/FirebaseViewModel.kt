package space.digitallab.noticeboard.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import space.digitallab.noticeboard.model.DbManager
import space.digitallab.noticeboard.model.Notice

class FirebaseViewModel: ViewModel() {

    private val dbManager = DbManager()
    val noticeData = MutableLiveData<ArrayList<Notice>>()

    fun loadAllNotice(){
        dbManager.getAllNotice(object: DbManager.ReadDataCallback{
            override fun readData(list: ArrayList<Notice>) {
                noticeData.value = list
            }
        })
    }

    fun loadMyNotice(){
        dbManager.getMyNotice(object: DbManager.ReadDataCallback{
            override fun readData(list: ArrayList<Notice>) {
                noticeData.value = list
            }
        })
    }
}