package com.example.concert_app.view.main.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.concert_app.R
import com.example.concert_app.data.user.UserResponse
import com.example.concert_app.service.user.UserApiService
import com.example.concert_app.databinding.FragmentAccountBinding
import com.example.concert_app.remote.NetworkConfig
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.utils.FirebaseServiceInstance.firebaseStorage
import com.example.concert_app.utils.Libs
import com.example.concert_app.utils.Libs.dialogMessageAnimate
import com.example.concert_app.view.LoginActivity
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.UUID

class AccountFragment : Fragment() {

    companion object {
        const val TAG = "AccountFragment"
        const val REQUEST_CODE = 100
    }

    private lateinit var binding: FragmentAccountBinding

    private lateinit var photoUrl: ImageView
    private lateinit var name: TextView
    private lateinit var phone: TextView
    private lateinit var btnLogout: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var chooseImage: CircleImageView
    private lateinit var fillPath: Uri
    private lateinit var progressDialog: ProgressDialog

    private fun initView() {
        photoUrl = binding.profileImage
        name = binding.profileName
        phone = binding.profilePhone
        btnLogout = binding.btnLogout
        progressBar = binding.progressbar
        chooseImage = binding.ivChooseImage
        progressDialog = ProgressDialog(context, R.style.MaterialAlertDialog_rounded)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        initView()

        val uid = auth.currentUser?.uid

        Log.d(TAG, "uid --> $uid")

        val apiService = UserApiService()
        if (uid != null) {
            apiService.getUserById(uid, TAG, photoUrl, name, phone, progressBar)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        chooseImage.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            fillPath = data?.data!!
            try {
                photoUrl.setImageURI(data.data)
                uploadImageToFirebase(fillPath)

                progressDialog.setTitle("is updating")
                progressDialog.setMessage("Please wait, data is updating")
                progressDialog.show()

              } catch (e: IOException) {
                e.stackTrace
            }
        }
    }

    private fun uploadImageToFirebase(UriPath: Uri) {
        if (UriPath != null) {
            val fillName = "profile_${UUID.randomUUID()}.jpg"
            val refStorage = firebaseStorage.reference.child("images_profile/${auth.currentUser?.email}/$fillName")
            refStorage.putFile(UriPath).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    progressDialog.dismiss()
                    val uriImg = uri.toString()
                    val uid = auth.currentUser!!.uid

                    NetworkConfig()
                        .getUserService()
                        .getUserById(uid)
                        .enqueue(object : Callback<UserResponse> {
                            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                                if (response.isSuccessful) {
                                    val id = response.body()?.data?.id
                                    val name = response.body()?.data?.name
                                    val phone = response.body()?.data?.phone
                                    val email = response.body()?.data?.email
                                    if (id != null && name != null && phone != null && email != null) {
                                        UserApiService().updateUser(id, name, phone, email, uriImg)
                                    }

                                    dialogMessageAnimate(layoutInflater, requireContext(), "Data Updated", R.raw.successful, "Success")
                                    progressBar.visibility = View.GONE
                                } else {
                                    Log.d(TAG, "Response Not Successfully")
                                    progressBar.visibility = View.GONE
                                    dialogMessageAnimate(layoutInflater, requireContext(), "Data Not Updated", R.raw.auth_failure, "Failure")
                                }
                            }

                            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                                Log.d(TAG, t.message.toString())
                                progressBar.visibility = View.GONE
                                dialogMessageAnimate(layoutInflater, requireContext(), t.message.toString(), R.raw.auth_failure, "Failure")
                            }

                        })

                }
            }
        }
    }


}