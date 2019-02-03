package cubex.mahesh.firebase_and7amjan19

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mInterstitialAd = InterstitialAd(this@MainActivity)
        mInterstitialAd.adUnitId = "ca-app-pub-6232583571965519/6871175661"
        mInterstitialAd.loadAd(AdRequest.Builder().build())


        var mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this@MainActivity)
        //mRewardedVideoAd.rewardedVideoAdListener = this
        mRewardedVideoAd.loadAd("ca-app-pub-6232583571965519/2357215580",
            AdRequest.Builder().build())


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

        banner.setOnClickListener {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

        interestial.setOnClickListener {

            if(mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            }
        }

        rewarded.setOnClickListener {

            if (mRewardedVideoAd.isLoaded) {
                mRewardedVideoAd.show()
            }
        }


        mlkit.setOnClickListener {

            startActivity(Intent(this@MainActivity,
                MLKitActivity::class.java))
        }

    } // onCreate( )
}

/*
FCM WebSevice URL
    Post Request :
      https://fcm.googleapis.com/fcm/send  - URL

  Header :
    1. Authorization : key=AAAAY6VNnHA:APA91bG70OUW6NuhMYGanG7OZ5QoOqLDxBhb6MDMFHCXlrIkY3e_2X5Ke3B-b0lUzdqaTZ0oZtTEhy_6vv5vcTZetsRd-OCCdPEydECS93rWDFeZp4SgZjOPoGGqavOTrJoVfKWySrjf
    2. Content-Type : application/json

   Body :

        {
            "data" : {
                  "title" : "Message from Rahul Kothari App",
                  "body":"Happy 72nd Independence Day",
                },
              "registration_ids" :[fcm_token1,fcm_token2]
        }


 */