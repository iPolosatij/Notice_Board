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

    fun favoriteClick(notice: Notice){
        dbManager.clickFavorite(notice, object: DbManager.FinishWorkListener{
            override fun onFinish() {
                val updatedList = noticeData.value
                val pos = updatedList?.indexOf(notice)
                if (pos != -1){
                    pos?.let {position ->
                        updatedList[position] = updatedList[position].copy(isFavorite = !notice.isFavorite)
                    }
                }
                noticeData.postValue(updatedList)
            }
        })
    }

    fun noticeViewed(notice: Notice){
        dbManager.noticeViewed(notice)
    }

    fun loadMyNotice(){
        dbManager.getMyNotice(object: DbManager.ReadDataCallback{
            override fun readData(list: ArrayList<Notice>) {
                noticeData.value = list
            }
        })
    }

    fun deleteItem(notice: Notice){
        dbManager.deleteNotice(notice,object : DbManager.FinishWorkListener{
            override fun onFinish() {
                val updatedList = noticeData.value
                updatedList?.remove(notice)
                noticeData.postValue(updatedList)

            }
        })
    }
}