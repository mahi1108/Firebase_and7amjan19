package cubex.mahesh.firebase_and7amjan19

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login.setOnClickListener {
            var fAuth = FirebaseAuth.getInstance()
            var task = fAuth.signInWithEmailAndPassword(
                et1.text.toString(),et2.text.toString())
            task.addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this@MainActivity,
                        "Login Success",
                        Toast.LENGTH_LONG).show()

                    var i = Intent(this@MainActivity,
                                                DashboardActivity::class.java)
                    startActivity(i)

                }else{
                    Toast.makeText(this@MainActivity,
                        "Login Fail",
                        Toast.LENGTH_LONG).show()
                }
            }
        }

        register.setOnClickListener {

            var fAuth = FirebaseAuth.getInstance()
            var task = fAuth.createUserWithEmailAndPassword(
                et1.text.toString(),et2.text.toString())
            task.addOnCompleteListener {
                if(it.isSuccessful){
                        Toast.makeText(this@MainActivity,
                                    "Registration Success",
                            Toast.LENGTH_LONG).show()
                    var i = Intent(this@MainActivity,
                        FbDatabaseActivity::class.java)
                    startActivity(i)
                }else{
                    Toast.makeText(this@MainActivity,
                        "Registration Fail",
                        Toast.LENGTH_LONG).show()
                }
            }

        }

    }
}
