package space.digitallab.noticeboard.database

import space.digitallab.noticeboard.data.Notice

interface ReadDataCallback {

    fun readData(list: List<Notice>)
}