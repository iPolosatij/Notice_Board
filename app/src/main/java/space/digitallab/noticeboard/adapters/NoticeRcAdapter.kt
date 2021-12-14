package space.digitallab.noticeboard.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import space.digitallab.noticeboard.MainActivity
import space.digitallab.noticeboard.act.EditAdsAct
import space.digitallab.noticeboard.databinding.NoticeListItemBinding
import space.digitallab.noticeboard.model.Notice

class NoticeRcAdapter(val act: MainActivity): RecyclerView.Adapter<NoticeRcAdapter.NoticeHolder>() {

    val noticeList = ArrayList<Notice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        val binding = NoticeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeHolder(binding, act)
    }

    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {
        holder.setData(noticeList[position])
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    fun updateAdapter(newList: ArrayList<Notice>){
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(noticeList, newList))
        diffResult.dispatchUpdatesTo(this)
        noticeList.clear()
        noticeList.addAll(newList)
    }

    class NoticeHolder(val binding: NoticeListItemBinding,val act: MainActivity) : RecyclerView.ViewHolder(binding.root) {

        fun setData(notice: Notice) = with(binding) {
            tvTitle.text = notice.title
            tvDiscription.text = notice.description
            tvPrice.text = notice.price
            tvViewCounter.text = notice.viewsCounter
            tvFavorit.text = notice.emailsCounter
            ownerPanelVisible(isOwner(notice))
            itemView.setOnClickListener {
                act.onNoticeViewed(notice)
            }
            ibEdit.setOnClickListener(onClickEdit(notice))
            ibDelete.setOnClickListener {
                act.onDeleteItem(notice)
            }
        }

        private fun onClickEdit(notice: Notice): View.OnClickListener{
            return View.OnClickListener {
                val editIntent = Intent(act, EditAdsAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.NOTICES_DATA, notice)

                }
                act.startActivity(editIntent)
            }
        }
        private fun isOwner(notice: Notice): Boolean{
            return notice.uid == act.mAuth.uid
        }

        private fun ownerPanelVisible(isOwner: Boolean){
            if(isOwner) binding.ownerPanel.visibility = View.VISIBLE
        }
    }

    interface ActionListener{
        fun onDeleteItem(notice: Notice)
        fun onNoticeViewed(notice: Notice)
    }
}