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

class UserPresenter : UserContract.Presenter {

    private lateinit var view: UserContract.View

    override fun attach(view: UserContract.View) {
        this.view = view
    }

    override fun launchGallery() {

    }

    override fun upDateUser(loggedInUser: User, context: Context) {
        val db = App.instance?.firebaseFirestore
        if ("" != view.getNameFromEditText()) {
            loggedInUser.name = view.getNameFromEditText()
        }
        if ("" != view.getEmailFromEditText()) {
            loggedInUser.email = view.getEmailFromEditText()
        }
        db?.collection("user")?.document(loggedInUser.id)
            ?.set(loggedInUser)
            ?.addOnSuccessListener {
                view.hideProgress()
                Toast.makeText(context, context.getText(R.string.process_success), Toast.LENGTH_LONG).show()
                view.showMainActivity()
            }
            ?.addOnFailureListener {
                view.hideProgress()
                Toast.makeText(context, context.getText(R.string.process_error), Toast.LENGTH_LONG).show()
            }
    }

    override fun uploadImage(filePath: Uri?, context: Context) {
        view.showProgress()

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
                    view.hideProgress()
                    Toast.makeText(context, context.getText(R.string.process_error), Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, context.getText(R.string.process_error), Toast.LENGTH_LONG).show()
            }
        }
    }

}