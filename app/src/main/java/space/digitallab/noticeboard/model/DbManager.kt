package space.digitallab.noticeboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishNotice(notice: Notice, finishListener: FinishWorkListener){
       if(auth.uid != null) db.child(notice.key?:"empty")
           .child(auth.uid!!).child(NOTICE_NODE)
           .setValue(notice).addOnCompleteListener{
               if(it.isSuccessful) {
                   finishListener.loadNoticeFinish()
               }
           }
    }

    fun noticeViewed(notice: Notice) {
        var counter = notice.viewsCounter.toInt()
        counter++
        if (auth.uid != null) db.child(notice.key ?: "empty")
            .child(INFO_NODE).setValue(ItemInfo(counter.toString(), notice.emailsCounter, notice.callsCounter))
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
                    notice?.apply {
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
                    if(it.isSuccessful) listener.loadNoticeFinish()
                }
            }
        }
    }

    interface ReadDataCallback {
        fun readData(list: ArrayList<Notice>)
    }

    interface FinishWorkListener{
        fun loadNoticeFinish()
    }

    companion object{
       const val NOTICE_NODE = "notice"
       const val MAIN_NODE = "main"
       const val INFO_NODE = "info"
    }
}