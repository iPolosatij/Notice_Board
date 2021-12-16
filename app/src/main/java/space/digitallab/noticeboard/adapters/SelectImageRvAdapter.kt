package space.digitallab.noticeboard.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.act.EditAdsAct
import space.digitallab.noticeboard.databinding.SelectImageFragmentItemBinding
import space.digitallab.noticeboard.utils.AdapterCallBack
import space.digitallab.noticeboard.utils.ImageManager
import space.digitallab.noticeboard.utils.ImagePiker
import space.digitallab.noticeboard.utils.ItemTouchMoveCallback

class SelectImageRvAdapter(val adapterCallBack: AdapterCallBack) : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(),
    ItemTouchMoveCallback.ItemTouchAdapter {

     val mainArray = ArrayList<Bitmap>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val viewBinding = SelectImageFragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(viewBinding, parent.context, this)
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

    class ImageHolder(private val viewBinding : SelectImageFragmentItemBinding, val context : Context,
                      val adapter : SelectImageRvAdapter) : RecyclerView.ViewHolder(viewBinding.root) {

        fun setData(bitmap : Bitmap){

            viewBinding.imEditImage.setOnClickListener {
                ImagePiker.launcher(context as EditAdsAct, 1)
                context.editImagePosition = adapterPosition
            }
            viewBinding.imDelete.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for(n in 0 until adapter.mainArray.size) adapter.notifyItemChanged(n)
                adapter.adapterCallBack.onItemDelete()
                /*
                if you use the notifyDataSetChanged method for the adapter,
                the animation of shifting photos disappears , and if you
                do not inform the adapter that the data inside has changed,
                the title will not change when deleting the photo
                */

            }
            viewBinding.tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            ImageManager.chooseScaleType(viewBinding.imageView, bitmap)
            viewBinding.imageView.setImageBitmap(bitmap)
        }
    }

    fun updateAdapter(newList : List<Bitmap>, needClear : Boolean){
        if(needClear) mainArray.clear()
            mainArray.addAll(newList)
            notifyDataSetChanged()

    }



}