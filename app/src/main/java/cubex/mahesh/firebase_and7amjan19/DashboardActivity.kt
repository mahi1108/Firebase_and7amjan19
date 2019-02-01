package cubex.mahesh.firebase_and7amjan19

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*

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
                            return  v;
                        }

                    }


                }
            })

    }
}
