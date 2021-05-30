package space.digitallab.noticeboard.dialoghelper

import android.app.AlertDialog
import space.digitallab.noticeboard.MainActivity
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.accouthelper.AccountHelper
import space.digitallab.noticeboard.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {
    private val act = act
    private val accHelper = AccountHelper(act)

    fun createSignDialog(index:Int){
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root

        if(index == DialogConst.SIGN_UP_STATE){
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
        }else{
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_log_in)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
        }

        rootDialogElement.btSignUpIn.setOnClickListener{

            if(index == DialogConst.SIGN_UP_STATE){
                accHelper.signUpWithEmail(rootDialogElement.idSigmEmail.text.toString(),
                        rootDialogElement.idSignPassword.text.toString())
            }else{

            }
        }

        builder.setView(view)
        builder.show()

    }
}