package space.digitallab.noticeboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import space.digitallab.noticeboard.R

class ImageListFragment(val fragmentCloseInterface : FragmentCloseInterface) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_image_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCloseInterface.onFragmentClose()
    }
}