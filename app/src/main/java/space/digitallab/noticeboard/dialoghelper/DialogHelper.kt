package space.digitallab.noticeboard.dialoghelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import space.digitallab.noticeboard.MainActivity
import space.digitallab.noticeboard.R
import space.digitallab.noticeboard.accounthelper.AccountHelper
import space.digitallab.noticeboard.databinding.SignDialogBinding

class DialogHelper(val act:MainActivity) {

    val accHelper = AccountHelper(act)
    private var isResetPasswordPressed = false

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

        rootDialogElement.btGoogleSignIn.setOnClickListener{
            accHelper.signInWithGoogle()
            dialog.dismiss()
        }

        rootDialogElement.btForgetPassword.setOnClickListener{
            setOnClickResetPassword(rootDialogElement, dialog)
        }
        dialog.show()
    }

    private fun setOnClickResetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        if(isResetPasswordPressed) {
            var email = rootDialogElement.edSignEmail.text.toString()
            if (email.isNotEmpty()) {
                act.mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(act, act.resources.getString(R.string.email_reset_password_was_sent), Toast.LENGTH_LONG).show()
                        dialog?.dismiss()
                    } else {
                        rootDialogElement.tvDialogMessage.setText(R.string.invalid_email)
                    }
                }
            } else {
                rootDialogElement.tvDialogMessage.setText(R.string.dialog_reset_email_message)
                rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
            }
        }else{
            rootDialogElement.btSignUpIn.visibility = View.INVISIBLE
            rootDialogElement.edSignPassword.visibility = View.INVISIBLE
            rootDialogElement.btGoogleSignIn.visibility = View.INVISIBLE
            rootDialogElement.resetPassText.visibility = View.VISIBLE
            rootDialogElement.btForgetPassword.setText(R.string.reset_password)
            isResetPasswordPressed = true
        }
    }

    private fun setOnClickSignUpIn(index: Int, rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {

        dialog?.dismiss()
        if(index == DialogConst.SIGN_UP_STATE){
            accHelper.signUpWithEmail(rootDialogElement.edSignEmail.text.toString(),
                    rootDialogElement.edSignPassword.text.toString())
        }else{
            accHelper.signInWithEmail(rootDialogElement.edSignEmail.text.toString(),
                    rootDialogElement.edSignPassword.text.toString())
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