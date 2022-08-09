package com.example.retrofitrecyclerview.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.retrofitrecyclerview.R
import com.example.retrofitrecyclerview.Api.RestApi
import com.example.retrofitrecyclerview.Service.ServiceBuilder
import com.example.retrofitrecyclerview.DataClass.UserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        Log.e("record oncreate", "record")


        var name = findViewById<EditText>(R.id.nameText)
        val email = findViewById<EditText>(R.id.emailText)
        val password = findViewById<EditText>(R.id.passText)
        val surname = findViewById<EditText>(R.id.surnameText)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Save page"

        }
        findViewById<Button>(R.id.button).setOnClickListener {


            var nameText = name.text.toString()
            var emailText = email.text.toString()
            var passwordText = password.text.toString()
            var surnameText = surname.text.toString()

            fun addUser(userData: UserInfo, onResult: (UserInfo?) -> Unit) {
                val retrofit = ServiceBuilder.buildService(RestApi::class.java)
                retrofit.addUser(userData).enqueue(
                    object : Callback<UserInfo> {

                        override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                            onResult(null)
                        }

                        override fun onResponse(
                            call: Call<UserInfo>,
                            response: Response<UserInfo>
                        ) {
                            val addedUser = response.body()

                            onResult(addedUser)
                        }
                    }
                )
            }


            if (nameText != null || surnameText != null || emailText != null || passwordText != null) {
                var RehberList = UserInfo(id = null, nameText, surnameText, emailText, passwordText)

                addUser(RehberList) {

                }

                onBackPressed()


            }

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true


    }

}