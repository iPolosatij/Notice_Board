package space.digitallab.noticeboard.dialoghelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
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
        builder.setView(view)

        setDialogState(index, rootDialogElement)

        val dialog = builder.create()

        rootDialogElement.btSignUpIn.setOnClickListener{
            setOnClickSignUpIn(index, rootDialogElement, dialog)
        }
        rootDialogElement.btForgetPassword.setOnClickListener{
            setOnClickResetPassword(rootDialogElement, dialog)
        }
        dialog.show()
    }

    private fun setOnClickResetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        rootDialogElement.idSignPassword.visibility = View.INVISIBLE
        rootDialogElement.btSignUpIn.setText(R.string.reset_password)
        rootDialogElement.btForgetPassword.visibility = View.INVISIBLE
        rootDialogElement.tvDialogMessage.setText(R.string.dialog_reset_email_message)
        rootDialogElement.btSignUpIn.setOnClickListener {
            resetPassword(rootDialogElement, dialog)
        }
    }

    private fun resetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        if(rootDialogElement.idSigmEmail.text.isNotEmpty()){
            act.mAuth.sendPasswordResetEmail(rootDialogElement.idSigmEmail.toString()).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(act, R.string.email_reset_password_was_sent, Toast.LENGTH_LONG).show()
                }
                dialog?.dismiss()
            }
        }else{
            rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
        }
    }

    private fun setOnClickSignUpIn(index: Int, rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {

        dialog?.dismiss()
        if(index == DialogConst.SIGN_UP_STATE){
            accHelper.signUpWithEmail(rootDialogElement.idSigmEmail.text.toString(),
                    rootDialogElement.idSignPassword.text.toString())
        }else{
            accHelper.signInWithEmail(rootDialogElement.idSigmEmail.text.toString(),
                    rootDialogElement.idSignPassword.text.toString())
        }
    }

    private fun setDialogState(index: Int, rootDialogElement: SignDialogBinding){

        if(index == DialogConst.SIGN_UP_STATE){
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_sign_up)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)
        }else{
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ac_log_in)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
            rootDialogElement.btForgetPassword.visibility = View.VISIBLE

        }
    }
}