package com.vptarasov.autosearch.ui.activity.user

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerActivityComponent
import com.vptarasov.autosearch.di.module.ActivityModule
import com.vptarasov.autosearch.model.User
import com.vptarasov.autosearch.ui.activity.main.MainActivity
import kotlinx.android.synthetic.main.activity_user.*
import java.io.IOException
import javax.inject.Inject

class UserActivity : AppCompatActivity(), UserContract.View {

    @Inject
    lateinit var presenter: UserContract.Presenter

    private lateinit var user: User

    private var filePath: Uri? = null

    private lateinit var buttonBack:Button
    private lateinit var userPhoto: ImageView
    private lateinit var userPhotoChoose: Button
    private lateinit var userName: EditText
    private lateinit var email: EditText
    private lateinit var userApply: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        injectDependency()
        presenter.attach(this)

        user = App.instance?.user!!

        initView()

    }

    override fun initView() {

        buttonBack = buttonBackUser
        buttonBack.setOnClickListener { showMainActivity() }

        userPhoto = userPhotoedit
        if("" != user.photoUrl){
            Picasso.get().load(user.photoUrl).into(userPhoto)
        }

        userPhotoChoose = userPhotoChooseButton
        userPhotoChoose.setOnClickListener { launchGallery() }

        userName = userNameInput
        userName.setText(user.name)

        email = emailInput
        email.setText(user.email)

        userApply = userApplyButton
        userApply.setOnClickListener { presenter.uploadImage(filePath, this) }

        progressBar = progressBarUser
    }

    private fun launchGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else{
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
            }
        }
        else{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                userPhoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun showMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun getNameFromEditText(): String {
        return userName.text.toString()
    }

    override fun getEmailFromEditText(): String {
        return email.text.toString()
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

}
