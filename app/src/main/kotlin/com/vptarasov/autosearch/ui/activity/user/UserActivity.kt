package com.vptarasov.autosearch.ui.activity.user

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.toolbar.*

class UserActivity : AppCompatActivity() {

    private lateinit var cardView: CardView
    private lateinit var userPhoto: ImageView
    private lateinit var userName: EditText
    private lateinit var photoUrl: EditText
    private lateinit var email: EditText
    private lateinit var phoneNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)

        val user = App.instance?.user

        cardView = cardViewEdit
        userPhoto = userPhotoedit
        userName = userNameInput
        userName.setText(user!!.name)
        photoUrl = photoUrlInput
        email = emailInput

        /*Toast.makeText(
            context,
            "Вы успешно зарегистрированы.",
            Toast.LENGTH_LONG
        )
            .show()*/
    }

}
