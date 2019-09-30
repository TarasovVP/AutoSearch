package com.vptarasov.autosearch.ui.activity.user

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.model.User
import com.vptarasov.autosearch.ui.fragments.BasePresenter

class UserPresenter : BasePresenter<UserContract.View>(), UserContract.Presenter {

    override fun launchGallery() {

    }

    override fun upDateUser(loggedInUser: User, context: Context) {
        val db = App.instance?.firebaseFirestore
        if ("" != getView()?.getNameFromEditText()) {
            loggedInUser.name = getView()!!.getNameFromEditText()
        }
        if ("" != getView()?.getEmailFromEditText()) {
            loggedInUser.email = getView()!!.getEmailFromEditText()
        }
        db?.collection("user")?.document(loggedInUser.id)
            ?.set(loggedInUser)
            ?.addOnSuccessListener {
                getView()?.hideProgress()
                Toast.makeText(context, context.getText(R.string.process_success), Toast.LENGTH_LONG).show()
                getView()?.showMainActivity()
            }
            ?.addOnFailureListener {
                getView()?.hideProgress()
                Toast.makeText(context, context.getText(R.string.process_error), Toast.LENGTH_LONG).show()
            }
    }

    override fun uploadImage(filePath: Uri?, context: Context) {
        getView()?.showProgress()

        val loggedInUser = App.instance?.user
        if (filePath == null) {
            loggedInUser?.let { upDateUser(it, context) }
        } else {
            val storageReference = FirebaseStorage.getInstance().reference
            val ref = storageReference.child("usersPhoto/${loggedInUser?.id}")
            val uploadTask = filePath.let { ref.putFile(it) }


            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception.let {
                        throw it!!
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    loggedInUser?.photoUrl = downloadUri.toString()
                    loggedInUser?.let { upDateUser(it, context) }
                } else {
                    getView()?.hideProgress()
                    Toast.makeText(context, context.getText(R.string.process_error), Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, context.getText(R.string.process_error), Toast.LENGTH_LONG).show()
            }
        }
    }

}