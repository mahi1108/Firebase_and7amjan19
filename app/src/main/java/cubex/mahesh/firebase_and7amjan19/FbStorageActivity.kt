package cubex.mahesh.firebase_and7amjan19

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_fb_storage.*
import java.io.ByteArrayOutputStream

class FbStorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fb_storage)

        cview.setOnClickListener {

            var aDialog = AlertDialog.Builder(this@FbStorageActivity)
            aDialog.setTitle("Message")
            aDialog.setMessage("Please select Source")
            aDialog.setPositiveButton("Camera",
                object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var i = Intent("android.media.action.IMAGE_CAPTURE")
                        startActivityForResult(i, 123)
                    }
                })
            aDialog.setNegativeButton("Gallery",
                object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        var i = Intent()
                        i.action = Intent.ACTION_GET_CONTENT
                        i.type = "image/*"
                        startActivityForResult(i, 124)
                    }
                })
            aDialog.setCancelable(true)
            aDialog.show()


        }  // onClickListener

    }   // onCreate( )

        override fun onActivityResult(requestCode: Int,
                                      resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if(requestCode==123 && resultCode== Activity.RESULT_OK)
            {
                var bmp: Bitmap =   data!!.extras.get("data") as Bitmap
                var selected_file = getImageUri(this@FbStorageActivity,
                    bmp)

            }else if(requestCode==124 && resultCode== Activity.RESULT_OK)
            {
               var  selected_file =    data!!.data
            }

        }


        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
            return Uri.parse(path)
        }
}
