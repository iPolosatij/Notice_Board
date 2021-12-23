package space.digitallab.noticeboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DbManager {
    val db = Firebase.database.getReference(MAIN_NODE)
    val dbStorage = Firebase.storage.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishNotice(notice: Notice, finishListener: FinishWorkListener){
       if(auth.uid != null) db.child(notice.key?:"empty")
           .child(auth.uid!!).child(NOTICE_NODE)
           .setValue(notice).addOnCompleteListener{
               if(it.isSuccessful) {
                   finishListener.onFinish()
               }
           }
    }

    fun noticeViewed(notice: Notice) {
        var counter = notice.viewsCounter.toInt()
        counter++
        if (auth.uid != null) db.child(notice.key ?: "empty")
            .child(INFO_NODE).setValue(ItemInfo(counter.toString(), notice.emailsCounter, notice.callsCounter))
    }

    fun clickFavorite(notice: Notice, finishWorkListener: FinishWorkListener){
        if (notice.isFavorite) removeFavorite(notice, finishWorkListener)
        else addFavorite(notice, finishWorkListener)
    }

    private fun addFavorite(notice: Notice, finishListener: FinishWorkListener){
        notice.key?.let { key ->
            auth.uid?.let { uid ->
                db.child(key)
                    .child(FAVORITE_NODE)
                    .child(uid)
                    .setValue(uid)
                    .addOnCompleteListener {
                    if (it.isSuccessful){
                        finishListener.onFinish()
                    }
                }
            }
        }
    }

    private fun removeFavorite(notice: Notice, finishListener: FinishWorkListener){
        notice.key?.let { key ->
            auth.uid?.let { uid ->
                db.child(key)
                    .child(FAVORITE_NODE)
                    .child(uid)
                    .removeValue()
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            finishListener.onFinish()
                        }
                    }
            }
        }
    }

    fun getMyFavoriteNotice(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild("/favorite/${auth.uid}").equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    fun getMyNotice(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/notice/uid").equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }
    fun getAllNotice(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/notice/price")
        readDataFromDb(query, readDataCallback)
    }

    private fun readDataFromDb( query: Query, readDataCallback: ReadDataCallback?){
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val noticeList = ArrayList<Notice>()
                for(item in snapshot.children){
                    var notice: Notice? = null
                    item.children.forEach{snapShot ->
                        if(notice == null) notice = snapShot.child(NOTICE_NODE).getValue(Notice::class.java)
                    }

                    val itemInfo = item.child(INFO_NODE).getValue(ItemInfo::class.java)
                    val favCounter = item.child(FAVORITE_NODE).childrenCount
                    val isMyFavorite = auth.uid?.let{uid ->
                        item.child(FAVORITE_NODE).child(uid).getValue(String::class.java)
                    }

                    notice?.apply {
                        isFavorite = isMyFavorite != null
                        favoriteCounter = favCounter.toString()
                       itemInfo?.let { info ->
                           viewsCounter = info.viewsCounter.toString()
                           emailsCounter = info.emailsCounter.toString()
                           callsCounter = info.callsCounter.toString()
                       }
                    }
                    notice?.let{ noticeList.add(it) }
                }
                readDataCallback?.readData(noticeList)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun deleteNotice(notice: Notice, listener: FinishWorkListener){
        notice.key?.let {key->
            notice.uid?.let { uid->
                db.child(key).child(uid).removeValue().addOnCompleteListener {
                    if(it.isSuccessful) listener.onFinish()
                }
            }
        }
    }

    interface ReadDataCallback {
        fun readData(list: ArrayList<Notice>)
    }

    interface FinishWorkListener{
        fun onFinish()
    }

    companion object{
       const val NOTICE_NODE = "notice"
       const val MAIN_NODE = "main"
       const val INFO_NODE = "info"
       const val FAVORITE_NODE = "favorite"
    }
}