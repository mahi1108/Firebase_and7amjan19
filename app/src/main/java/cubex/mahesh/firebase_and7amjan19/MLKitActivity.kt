package cubex.mahesh.firebase_and7amjan19

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_mlkit.*
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer



class MLKitActivity : AppCompatActivity() {
    var bmp: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mlkit)

        cam.setOnClickListener {

            var i = Intent("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(i, 123)
        }

        getText.setOnClickListener {

            val image = FirebaseVisionImage.fromBitmap(bmp!!)
            val detector = FirebaseVision.getInstance()
                .onDeviceTextRecognizer
            detector.processImage(image).addOnSuccessListener {
               var list =  it.textBlocks
                var msg = "";
                for(l in list){
                    msg = msg + l.text +" "
                }
                tv1.text = msg
            }

        }

    } //onCreate( )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        bmp = data?.extras?.get("data") as Bitmap
        iview.setImageBitmap(bmp)
    }
}
