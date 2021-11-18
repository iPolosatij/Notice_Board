package space.digitallab.noticeboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import space.digitallab.noticeboard.databinding.NoticeListItemBinding
import space.digitallab.noticeboard.model.Notice

class NoticeRcAdapter(val auth: FirebaseAuth): RecyclerView.Adapter<NoticeRcAdapter.NoticeHolder>() {

    val noticeList = ArrayList<Notice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        val binding = NoticeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeHolder(binding, auth)
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

    class NoticeHolder(val binding: NoticeListItemBinding,val auth: FirebaseAuth) : RecyclerView.ViewHolder(binding.root) {

        fun setData(notice: Notice){
            binding.apply {
                tvTitle.text = notice.title
                tvDiscription.text = notice.description
                tvPrice.text = notice.price
            }
            ownerPanelVisible(isOwner(notice))
        }

        private fun isOwner(notice: Notice): Boolean{
            return notice.uid == auth.uid
        }

        private fun ownerPanelVisible(isOwner: Boolean){
            if(isOwner) binding.ownerPanel.visibility = View.VISIBLE
        }
    }
}