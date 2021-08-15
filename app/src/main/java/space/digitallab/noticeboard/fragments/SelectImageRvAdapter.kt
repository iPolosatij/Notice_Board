package space.digitallab.noticeboard.fragments

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.utils.ItemTouchMoveCallback

class SelectImageRvAdapter : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {

     val mainArray = ArrayList<SelectImageItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_fragment_item, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) {
        val targetItem = mainArray[targetPos]
        val title = mainArray[startPos].title
        mainArray[targetPos] = mainArray[startPos]
        mainArray[targetPos].title = targetItem.title
        mainArray[startPos] = targetItem
        mainArray[startPos].title = title
        notifyItemMoved(startPos, targetPos)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tvTitle : TextView
        lateinit var image : ImageView

        fun setData(item : SelectImageItem){
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageView)
            tvTitle.text = item.title
            image.setImageURI(Uri.parse(item.imageUri))
        }
    }

    fun updateAdapter(newList : List<SelectImageItem>, needClear : Boolean){
        if(needClear) mainArray.clear()
            mainArray.addAll(newList)
            notifyDataSetChanged()

    }



}