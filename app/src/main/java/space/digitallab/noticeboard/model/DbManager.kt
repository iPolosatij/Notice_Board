package space.digitallab.noticeboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    val db = Firebase.database.getReference("Main")
    val auth = Firebase.auth

    fun publishNotice(notice: Notice){
       if(auth.uid != null) db.child(notice.key?:"empty").child(auth.uid!!).child("notice").setValue(notice)
    }

    fun readDataFromDb( readDataCallback: ReadDataCallback?){
        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val noticeList = ArrayList<Notice>()
                for(item in snapshot.children){
                    val notice = item
                        .children
                        .iterator()
                        .next()
                        .child("notice")
                        .getValue(Notice::class.java)
                    notice?.let { noticeList.add(it) }
                }
                readDataCallback?.readData(noticeList)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    interface ReadDataCallback {
        fun readData(list: ArrayList<Notice>)
    }
}