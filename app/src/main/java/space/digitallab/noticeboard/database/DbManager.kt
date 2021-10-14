package space.digitallab.noticeboard.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import space.digitallab.noticeboard.data.Notice

class DbManager {
    val db = Firebase.database.getReference("Main")
    val auth = Firebase.auth

    fun publishNotice(notice: Notice){
       if(auth.uid != null) db.child(notice.key?:"empty").child(auth.uid!!).child("notice").setValue(notice)
    }

    fun readDataFromDb(){
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
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}