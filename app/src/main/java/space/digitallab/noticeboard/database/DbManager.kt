package space.digitallab.noticeboard.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DbManager {
    private val db = Firebase.database.getReference("Main")

    fun publishAd(){
        db.setValue(1)
    }
}