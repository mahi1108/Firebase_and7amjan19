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
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fb_database.*
import kotlinx.android.synthetic.main.activity_fb_storage.*
import java.io.ByteArrayOutputStream
import com.google.firebase.database.DatabaseReference
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask


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
                cview.setImageURI(selected_file)

                uploadFileIntoFb(selected_file)

            }else if(requestCode==124 && resultCode== Activity.RESULT_OK)
            {
               var  selected_file =    data!!.data
                cview.setImageURI(selected_file)

                uploadFileIntoFb(selected_file)

            }

        }


        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
            return Uri.parse(path)
        }

        fun uploadFileIntoFb(file_path : Uri)
        {
            var sRef =  FirebaseStorage.getInstance().
                getReference("files")
            var task = sRef.child(FirebaseAuth.getInstance().uid!!+"/profile_pic.jpg").
                putFile(file_path)
      /*      task.addOnCompleteListener {
                if(it.isSuccessful){

                    sRef.downloadUrl.addOnSuccessListener {
                        var fDbase = FirebaseDatabase.getInstance()
                        var fRef = fDbase.getReference("students")
                        var uid_ref = FirebaseAuth.getInstance().uid
                        var child_uid = fRef.child(uid_ref!!)
                        child_uid.child("profile_pic").setValue(it.toString())

                    }


                }else{
Toast.makeText(this@FbStorageActivity,
    "Failed to Upload File into Storage ",
    Toast.LENGTH_LONG).show()
                }
            } */

            val urlTask = task.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation sRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    var fDbase = FirebaseDatabase.getInstance()
                    var fRef = fDbase.getReference("students")
                    var uid_ref = FirebaseAuth.getInstance().uid
                    var child_uid = fRef.child(uid_ref!!)
                    child_uid.child("profile_pic").setValue(downloadUri.toString())


                } else {
                    // Handle failures
                    // ...
                }
            }
        }
}
