package space.digitallab.noticeboard.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import space.digitallab.noticeboard.data.Notice

class DbManager {
    val db = Firebase.database.getReference("Main")
    val auth = Firebase.auth

    fun publishNotice(notice: Notice){
       if(auth != null) db.child(notice.key?:"empty").child(auth.uid!!).child("notice").setValue(notice)
    }
}