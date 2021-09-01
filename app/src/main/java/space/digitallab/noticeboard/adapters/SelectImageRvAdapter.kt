package space.digitallab.noticeboard.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.act.EditAdsAct
import space.digitallab.noticeboard.utils.ImagePiker
import space.digitallab.noticeboard.utils.ItemTouchMoveCallback

class SelectImageRvAdapter : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {

     val mainArray = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_fragment_item, parent, false)
        return ImageHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) {
        val targetItem = mainArray[targetPos]
        mainArray[targetPos] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos, targetPos)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView : View, val context : Context) : RecyclerView.ViewHolder(itemView) {

        lateinit var tvTitle : TextView
        lateinit var image : ImageView
        lateinit var imEditImage : ImageButton

        fun setData(item : String){
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageView)
            imEditImage =  itemView.findViewById(R.id.imEditImage)
            imEditImage.setOnClickListener {

                ImagePiker.getImages(context as EditAdsAct, 1, ImagePiker.REQUEST_CODE_GET_SINGLE_IMAGE)
                context.editImagePosition = adapterPosition
            }
            tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            image.setImageURI(Uri.parse(item))
        }
    }

    fun updateAdapter(newList : List<String>, needClear : Boolean){
        if(needClear) mainArray.clear()
            mainArray.addAll(newList)
            notifyDataSetChanged()

    }



}