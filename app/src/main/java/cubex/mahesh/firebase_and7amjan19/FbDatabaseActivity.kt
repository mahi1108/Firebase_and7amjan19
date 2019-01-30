package cubex.mahesh.firebase_and7amjan19

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_fb_database.*

class FbDatabaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fb_database)

        submit.setOnClickListener {

                var fDbase = FirebaseDatabase.getInstance()
                var fRef = fDbase.getReference("students")
                var uid_ref = FirebaseAuth.getInstance().uid
                var child_uid = fRef.child(uid_ref!!)
                child_uid.child("id").setValue(et1.text.toString())
                child_uid.child("name").setValue(et2.text.toString())
                child_uid.child("gender").setValue(et3.text.toString())
                child_uid.child("mno").setValue(et4.text.toString())
                child_uid.child("address").setValue(et5.text.toString())

            var i = Intent(this@FbDatabaseActivity,
                FbStorageActivity::class.java)
            startActivity(i)

        }
    }
}
