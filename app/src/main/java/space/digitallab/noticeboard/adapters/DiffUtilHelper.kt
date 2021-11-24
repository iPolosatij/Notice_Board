package space.digitallab.noticeboard.adapters

import androidx.recyclerview.widget.DiffUtil
import space.digitallab.noticeboard.model.Notice

class DiffUtilHelper(val oldList: List<Notice>, val newList: List<Notice>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
       return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[newItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}