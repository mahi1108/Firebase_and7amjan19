package cubex.mahesh.firebase_and7amjan19

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

                }else{

                }
            }
        }

        register.setOnClickListener {

            var fAuth = FirebaseAuth.getInstance()
            var task = fAuth.createUserWithEmailAndPassword(
                et1.text.toString(),et2.text.toString())
            task.addOnCompleteListener {
                if(it.isSuccessful){

                }else{

                }
            }

        }

    }
}
