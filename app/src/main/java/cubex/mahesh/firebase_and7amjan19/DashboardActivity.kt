package cubex.mahesh.firebase_and7amjan19

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fcm_dialog.view.*
import kotlinx.android.synthetic.main.indiview.view.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var dBase = FirebaseDatabase.getInstance()
        var dRef = dBase.getReference("students")
        dRef.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(stus: DataSnapshot) {

                 var childs_students =    stus.children

                    var students_list = mutableListOf<Student>()

                    childs_students.forEach {

                        var childs_uid = it.children
                        var s = Student( )

                        childs_uid.forEach {
                           when(it.key){
                             "id"-> s.id = it.value.toString().toInt()
                               "name"-> s.name = it.value.toString()
                               "gender"-> s.gender = it.value.toString()
                               "mno"-> s.mno = it.value.toString()
                               "address"-> s.address = it.value.toString()
                               "fcm_token"-> s.fcm_id = it.value.toString()
                                "profile_pic_url"-> s.profile_pic_url = it.value.toString()
                           }
                        }
                            students_list.add(s)
                    }  // reading completed of all uids.

                    lview.adapter = object:BaseAdapter(){
                        override fun getCount(): Int  = students_list.size

                        override fun getItem(p0: Int): Any = 0

                        override fun getItemId(p0: Int): Long = 0

                        override fun getView(pos: Int, p1: View?, p2: ViewGroup?): View {
                            var inflater = LayoutInflater.from(this@DashboardActivity)
                            var v = inflater.inflate(R.layout.indiview, null)
                            Glide.with(this@DashboardActivity).load(
                                students_list.get(pos).profile_pic_url).into(v.cview)

                            when(students_list.get(pos).gender)
                            {
                                "male" -> { v.gender.setBackground( resources.getDrawable
                                    (R.drawable.ic_male_close_up_silhouette_with_tie))
                                }
                                "female" -> {
                                    v.gender.setBackground( resources.getDrawable
                                        (R.drawable.ic_man_with_shirt_user_symbol))
                                }
                            }

                            v.name.text = students_list.get(pos).name
                            v.mno.text = students_list.get(pos).mno
                            v.address.text = students_list.get(pos).address



                            v.chat.setOnClickListener {
                                var aDialog = AlertDialog.Builder(this@DashboardActivity)
                                aDialog.setTitle("FCM Message")
                                var v1 = inflater.inflate(R.layout.fcm_dialog,null)
                                aDialog.setView(v1)
                                aDialog.setPositiveButton("Send",
                                     { dialogInterface, i ->
                                        sendFcmMessage(students_list.get(pos).fcm_id,v1.et1.text.toString())
                                    })
                                aDialog.setNegativeButton("Cancel",
                                     { dialogInterface, i ->
                                        dialogInterface.dismiss()
                                    })
                                aDialog.setNeutralButton("SendToAll",
                                     { dialogInterface, i ->

                                         var templist = mutableListOf<String>()
                                         for (s in students_list){
                                             templist.add(s.fcm_id!!)
                                         }
                                         sendFcmMessageToAll(v1.et1.text.toString(),
                                             templist)
                                     })
                                aDialog.show()
                            }


                            return  v;
                        }

                    }
                }
            })
    } // onCreate( )

    fun sendFcmMessage(token:String?, msg:String?)
    {
        var jsonObjec: JSONObject? = null
        var bodydata:String = msg!!

        jsonObjec =  JSONObject()
        var list = mutableListOf<String>()
        list.add(token!!)

        var   jsonArray: JSONArray = JSONArray(list)
        jsonObjec.put("registration_ids", jsonArray);
        var jsonObjec2: JSONObject = JSONObject()
        jsonObjec2.put("body", bodydata);
        jsonObjec2.put("title", "Text Message from Android 7AMJan19")
        jsonObjec2.put("fcm_type", "text")
        jsonObjec.put("notification", jsonObjec2);

        jsonObjec.put("time_to_live", 172800);
        jsonObjec.put("priority", "HIGH");

        println("*************")
        print(jsonObjec)
        println("*************")


        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, jsonObjec.toString())
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=AAAAY6VNnHA:APA91bG70OUW6NuhMYGanG7OZ5QoOqLDxBhb6MDMFHCXlrIkY3e_2X5Ke3B-b0lUzdqaTZ0oZtTEhy_6vv5vcTZetsRd-OCCdPEydECS93rWDFeZp4SgZjOPoGGqavOTrJoVfKWySrjf")
            .url("https://fcm.googleapis.com/fcm/send")
            .post(body)
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {

//                Toast.makeText(this@DashboardActivity,
//                    "Message Sending Success",
//                    Toast.LENGTH_LONG).show()

                Log.i("msg",response!!.body().toString())

            }
            override fun onFailure(call: Call?, e: IOException?) {
//                Toast.makeText(this@DashboardActivity,
//                    "Message Sending Failure",
//                    Toast.LENGTH_LONG).show()

                Log.i("msg","Fail....")

            }
        })
    }

    fun sendFcmMessageToAll(msg:String,fcm_tokens_list:MutableList<String>)
    {
        var bodydata:String = msg

        var  jsonObjec =  JSONObject()

        var   jsonArray: JSONArray = JSONArray(fcm_tokens_list)
        jsonObjec.put("registration_ids", jsonArray);
        var jsonObjec2: JSONObject = JSONObject()
        jsonObjec2.put("body", bodydata);
        jsonObjec2.put("title", "Text Message from FbNov7AM Jan19 ")
        jsonObjec.put("notification", jsonObjec2);

        jsonObjec.put("time_to_live", 172800);
        jsonObjec.put("priority", "HIGH");

        println("*************")
        print(jsonObjec)
        println("*************")


        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, jsonObjec.toString())
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=AAAACRZA4-0:APA91bGmzKM2joCYGvKUQMAK6uNpy7_oP_S325mwqANkJnFh3PKeKd9ka3yhIpcOE0XEPoZihhd4qr7HLmZ7XQrsPftTNJwxD_GaVXpP7yRiMm7yDlamAHLXOf0atioz3z3DdMACFJQX")
            .url("https://fcm.googleapis.com/fcm/send")
            .post(body)
            .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {

//                Toast.makeText(this@DashboardActivity,
//                    "Message Sending Success",
//                    Toast.LENGTH_LONG).show()

                Log.i("msg",response!!.body().toString())

            }
            override fun onFailure(call: Call?, e: IOException?) {
//                Toast.makeText(this@DashboardActivity,
//                    "Message Sending Failure",
//                    Toast.LENGTH_LONG).show()

                Log.i("msg","Fail....")

            }
        })
    }
}  // MainActivity
