package com.example.Chalish

import okhttp3.*
import android.content.Context
import android.content.Intent
import android.net.http.HttpResponseCache.install
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import okio.ByteString
import org.json.JSONArray
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private var m_strOrderBy: String = "emp_no"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        val intent = Intent(this, thirdPartyLogin::class.java)
        startActivity(intent)

//        var strings: String = "abcdefgh"
//        var re = strings?.reversed()
//        Log.d("testrev",re)
//        rbEmpNo.setOnCheckedChangeListener(m_OnCheckedChangeListener)
//        rbEmpFName.setOnCheckedChangeListener(m_OnCheckedChangeListener)
//        rbEmpLName.setOnCheckedChangeListener(m_OnCheckedChangeListener)
//        rbEmpGender.setOnCheckedChangeListener(m_OnCheckedChangeListener)
//        rbEmpBirthDate.setOnCheckedChangeListener(m_OnCheckedChangeListener)


//        mRecyclerView = findViewById(R.id.vMyview)
//
//        rbgOrderby.setOnCheckedChangeListener(
//            RadioGroup.OnCheckedChangeListener { group, checkedId ->
//            })
//
//        btnSearch.setOnClickListener {
//            val strLastName: String ? = etLastName.text.toString()
//            Log.d("btn",strLastName)
//
//            val inputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            if (inputMethodManager.isActive()) {
//                inputMethodManager.hideSoftInputFromWindow(
//                    this@MainActivity.currentFocus!!.windowToken,
//                    0
//                )
//            }
//            Search()
//        }
//
//        etLastName.setOnEditorActionListener{_,actionId,_ ->
//            var handled = false
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
////                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//
//                val inputMethodManager =
//                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                if (inputMethodManager.isActive()) {
//                    inputMethodManager.hideSoftInputFromWindow(
//                        this@MainActivity.currentFocus!!.windowToken,
//                        0
//                    )
//                }
//
//                Toast.makeText(this@MainActivity, "send", Toast.LENGTH_SHORT).show()
//                Search()
//                handled = true
//            }
//            handled
//        }
//
//    }
//
//    fun Search(){
//        val strLastName: String = etLastName.text.toString()
//
//        if(strLastName.isEmpty()) {
//            AlertDialog.Builder(this)
//                .setMessage("請輸入資料")
//                .setTitle("輸入資料不得為空，請重新輸入")
//                .show()
//            return
//        }
//
//        when(rbgOrderby.checkedRadioButtonId){
//            R.id.rbEmpNo ->{
////                Toast.makeText(this@MainActivity, "安安, 1!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "emp_no"
//            }
//            R.id.rbEmpFName->{
////                Toast.makeText(this@MainActivity, "安安, 2!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "first_name"
//            }
//            R.id.rbEmpLName ->{
////                Toast.makeText(this@MainActivity, "安安, 3!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "last_name"
//            }
//            R.id.rbEmpGender ->{
////                Toast.makeText(this@MainActivity, "安安, 4!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "gender"
//            }
//            R.id.rbEmpBirthDate ->{
////                Toast.makeText(this@MainActivity, "安安, 5!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "birth_date"
//            }
//        }
//        val strUrl = "http://140.136.61.74:3000/query/get_employee/"+strLastName+"/"+m_strOrderBy
//        Log.i("Login",strUrl)
//        val que = Volley.newRequestQueue(this)
//        val req = object : JsonArrayRequest(Request.Method.GET, strUrl, null,
//            Response.Listener {
//                    response ->
//                Log.i("Login","Success")
//                Log.i("Login",response.toString())
//                Toast.makeText(this@MainActivity, "Login Success", Toast.LENGTH_SHORT).show()
//                if(response.length()==0) {
//                    AlertDialog.Builder(this)
//                        .setMessage("無搜尋結果，請檢察查詢資料！")
//                        .setTitle("無搜尋結果")
//                        .show()
//                    return@Listener
//                }
//                val gson = GsonBuilder().create()
//                val items= gson.fromJson(response.toString(),Array<MyJsonObj>::class.java).toList()
//
//                mRecyclerView.layoutManager = LinearLayoutManager(this)
//                mRecyclerView.adapter = MainAdapter(items, this)
//
//            }, Response.ErrorListener {
//                    error ->
//                Toast.makeText(this@MainActivity, "Login Failed", Toast.LENGTH_SHORT).show()
//                Log.i("LoginErr",error.toString())
//            }){}
//
//        que.add(req)
//
//    }


//    private val m_OnCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, _ ->
//        when(buttonView.id){
//            R.id.rbEmpNo ->{
//                Toast.makeText(this@MainActivity, "安安, 1!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "emp_no"
//            }
//            R.id.rbEmpFName->{
//                Toast.makeText(this@MainActivity, "安安, 2!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "first_name"
//            }
//            R.id.rbEmpLName ->{
//                Toast.makeText(this@MainActivity, "安安, 3!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "last_name"
//            }
//            R.id.rbEmpGender ->{
//                Toast.makeText(this@MainActivity, "安安, 4!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "gender"
//            }
//            R.id.rbEmpBirthDate ->{
//                Toast.makeText(this@MainActivity, "安安, 5!", Toast.LENGTH_SHORT).show()
//                m_strOrderBy = "birth_date"
//            }
//
//        }
//    }

//    class MyJsonObj {
//        @SerializedName("emp_no")
//        var emp_no: Int = 0
//
//        @SerializedName("birth_date")
//        var birth_date : String? = null
//
//        @SerializedName("first_name")
//        var first_name: String? = null
//
//        @SerializedName("last_name")
//        var last_name: String? = null
//
//        @SerializedName("gender")
//        var gender: String? = null
//
//        @SerializedName("hire_date")
//        var hire_date : String? = null
//    }
//
//    class MainAdapter(private val items : List<MyJsonObj>, var context: Context) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//            val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_layout, parent, false)
//            return ViewHolder(view)
//        }
//
//        override fun getItemCount(): Int = items.size
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder?.bind(items[position])
//        }
//
//        class ViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView) {
//            private val m_EmpNo = itemView?.findViewById<TextView>(R.id.tvEmpNo)
//            private val m_BirthDate = itemView?.findViewById<TextView>(R.id.tvBirthDate)
//            private val m_FirstName = itemView?.findViewById<TextView>(R.id.tvFirstName)
//            private val m_LastName = itemView?.findViewById<TextView>(R.id.tvLastName)
//            private val m_Gender = itemView?.findViewById<TextView>(R.id.tvGender)
//            private val m_HireDate = itemView?.findViewById<TextView>(R.id.tvHireDate)
//
//            fun bind(item: MyJsonObj) {
//                m_EmpNo?.text = item.emp_no.toString()
//                m_BirthDate?.text = item.birth_date?.substring(0,10)
//                m_FirstName?.text = item.first_name
//                m_LastName?.text = item.last_name
//                m_Gender?.text = item.gender
//                m_HireDate?.text = ""
////                m_HireDate?.text = item.hire_date?.substring(0,10)
//            }
//        }
    }
//
}