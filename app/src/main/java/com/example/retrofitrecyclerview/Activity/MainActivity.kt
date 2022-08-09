package com.example.retrofitrecyclerview.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitrecyclerview.ProgressBar.LoadingDialog
import com.example.retrofitrecyclerview.R
import com.example.retrofitrecyclerview.Adapter.adapter
import com.example.retrofitrecyclerview.Api.RestApi
import com.example.retrofitrecyclerview.Service.ServiceBuilder
import com.example.retrofitrecyclerview.DataClass.UserInfo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var list: ArrayList<UserInfo> = arrayListOf()
    private var recyclerViewAdapter: adapter? = null
    lateinit var recyclerView: RecyclerView
    private val BASE_URL = "https://melisa-test.herokuapp.com/api/test/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewText)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        loadData()
        recyclerViewAdapter?.notifyDataSetChanged()
        deleteUser()


        var buttonn = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        buttonn.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)

        }


    }

    override fun onResume() {
        loadData()
        recyclerViewAdapter?.notifyDataSetChanged()
        super.onResume()
    }


    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RestApi::class.java)
        val call = service.getUser()
        val loading = LoadingDialog(this@MainActivity)
        loading.startLoading()
        call.enqueue(object : Callback<List<UserInfo>> {

            override fun onResponse(
                call: Call<List<UserInfo>>,
                response: Response<List<UserInfo>>
            ) {
                recyclerViewAdapter?.notifyDataSetChanged()
                if (response.isSuccessful) {
                    response.body()?.let {
                        recyclerViewAdapter = adapter(it, this@MainActivity)
                        recyclerViewText.adapter = recyclerViewAdapter
                        loading.isDismiss()


                    }

                }
            }

            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                loading.isDismiss()
                Toast.makeText(this@MainActivity, "yanlış", Toast.LENGTH_SHORT).show()
            }

        })

    }


    fun deleteUser() {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val alert = AlertDialog.Builder(this@MainActivity)
                    alert.setTitle("Delete Contacts")
                    alert.setMessage("Are you sure")
                    alert.setPositiveButton("yes") { dialog, id ->


                        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
                        retrofit.deleteUser(position).enqueue(
                            object : Callback<List<UserInfo>> {

                                override fun onResponse(
                                    call: Call<List<UserInfo>>,
                                    response: Response<List<UserInfo>>
                                ) {
                                    if (response.isSuccessful) {
                                        list = (response.body() as ArrayList<UserInfo>?)!!
                                        recyclerViewAdapter?.notifyDataSetChanged()


                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Failed to Delete",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }


                                override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                                    Toast.makeText(this@MainActivity, "false", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            })
                    }
                    alert.setNegativeButton("no") { dialog, id ->
                        Toast.makeText(this@MainActivity, "Data not deleted", Toast.LENGTH_LONG)
                            .show()
                        recyclerView.adapter!!.notifyItemRangeChanged(0, list.size)

                        dialog.cancel()
                        recyclerViewAdapter?.notifyDataSetChanged()

                    }
                    val build = alert.create()
                    build.show()

                }


            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewText)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var menuItem = menu!!.findItem(R.id.action_search)
        var searchView = menuItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        //search kısmını toolbarda uzun olarak gösteriyor
        searchView.imeOptions = (EditorInfo.IME_ACTION_DONE)
        //klavyede ki search kısmını iptal ediyor ve tik iconu nu koyuyor.

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val retrofit = ServiceBuilder.buildService(RestApi::class.java)

                if (newText?.length == 3 || newText?.length == 0) {
                    retrofit.getMovies(newText).enqueue(
                        object : Callback<List<UserInfo>> {

                            override fun onResponse(
                                call: Call<List<UserInfo>>,
                                response: Response<List<UserInfo>>
                            ) {
                                if (response.isSuccessful) {
                                    recyclerViewAdapter?.setData(response.body()!!)
                                    //adapter da ki setData ya gidiyor.
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Failed to Delete",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }

                            override fun onFailure(call: Call<List<UserInfo>>, t: Throwable) {
                                Toast.makeText(this@MainActivity, "hatalı", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })

                }


                return false
            }


        })
        return true
    }

}


