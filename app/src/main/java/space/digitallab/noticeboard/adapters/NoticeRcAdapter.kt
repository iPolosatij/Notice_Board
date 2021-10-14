package space.digitallab.noticeboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import space.digitallab.noticeboard.data.Notice
import space.digitallab.noticeboard.databinding.NoticeListItemBinding

class NoticeRcAdapter: RecyclerView.Adapter<NoticeRcAdapter.NoticeHolder>() {

    val noticeList = ArrayList<Notice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        val binding = NoticeListItemBinding.inflate(LayoutInflater.from(parent.context))
        return NoticeHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {
        holder.setData(noticeList[position])
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    fun updateAdapter(newList: ArrayList<Notice>){
        noticeList.clear()
        noticeList.addAll(newList)
        notifyDataSetChanged()
    }

    class NoticeHolder(val binding: NoticeListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(notice: Notice){
            binding.apply {
                tvDiscription.text = notice.description
                tvPrice.text = notice.price
            }
        }

    }
}