package com.example.concert_app.view.main.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.concert_app.R
import com.example.concert_app.data.user.UserResponse
import com.example.concert_app.service.user.UserApiService
import com.example.concert_app.databinding.FragmentAccountBinding
import com.example.concert_app.apiConfig.NetworkConfig
import com.example.concert_app.utils.FirebaseServiceInstance
import com.example.concert_app.utils.FirebaseServiceInstance.auth
import com.example.concert_app.utils.FirebaseServiceInstance.firebaseStorage
import com.example.concert_app.utils.Libs
import com.example.concert_app.utils.Libs.dialogMessageAnimate
import com.example.concert_app.utils.Libs.simpleDateFormat
import com.example.concert_app.utils.LocalKeys
import com.example.concert_app.utils.Preference.loadData
import com.example.concert_app.view.LoginActivity
import com.example.concert_app.view.RegisterActivity
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.UUID

class AccountFragment : Fragment() {

    companion object {
        const val TAG = "AccountFragment"
        const val REQUEST_CODE = 100
    }

    private lateinit var binding: FragmentAccountBinding

    private lateinit var photoUrl: ImageView
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var phone: TextView
    private lateinit var btnLogout: MaterialButton
    private lateinit var chooseImage: MaterialButton
    private lateinit var fillPath: Uri
    private lateinit var progressDialog: ProgressDialog
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var following: TextView
    private lateinit var followers: TextView

    private lateinit var shimmerViewContainer: ShimmerFrameLayout
    private lateinit var shimmerViewContainerSatu: ShimmerFrameLayout
    private lateinit var profileLayout: LinearLayout
    private lateinit var layoutItem1View: RelativeLayout

    private fun initView() {
        photoUrl = binding.profileImage
        name = binding.profileName
        email = binding.profileEmail
        phone = binding.profilePhone
        title = binding.title
        desc = binding.description
        btnLogout = binding.btnSettingAccount
        chooseImage = binding.ivChooseImage
        progressDialog = ProgressDialog(context, R.style.MaterialAlertDialog_rounded)
        shimmerViewContainer = binding.shimmerViewContainer
        profileLayout = binding.profileLayout
        shimmerViewContainerSatu = binding.shimmerViewContainer1View
        layoutItem1View = binding.layoutItem1View
        following = binding.followingCount
        followers = binding.followersCount
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)


        initView()

        profileLayout.visibility = View.GONE
        layoutItem1View.visibility = View.GONE
        shimmerViewContainer.startShimmer()
        shimmerViewContainerSatu.startShimmer()

        val uid = auth.currentUser?.uid

        Log.d(TAG, "uid --> $uid")

        getUserById(uid)

        return binding.root
    }

    private fun getUserById(uid: String?) {
        val apiService = UserApiService(requireActivity())
        if (uid != null) {
            apiService.getUserById(
                uid,
                TAG,
                photoUrl,
                name,
                email,
                phone,
                title,
                desc,
                shimmerViewContainer,
                shimmerViewContainerSatu,
                profileLayout,
                layoutItem1View
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout.setOnClickListener {
            popupMenu()
        }

        chooseImage.setOnClickListener {
            openGalleryForImage()
        }

        following.setOnClickListener {
            startActivity(Intent(requireActivity(), ListPersonAccountActivity::class.java))
        }
        followers.setOnClickListener {
            Toast.makeText(requireActivity(), "Belum ada followers di akun kamu", Toast.LENGTH_SHORT).show()
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

    private fun uploadImageToFirebase(uriPath: Uri) {
        if (uriPath != null) {
            val fillName = "profile_${UUID.randomUUID()}.jpg"
            val refStorage =
                firebaseStorage.reference.child("/images_profile/${auth.currentUser?.email}/$fillName")
            refStorage.putFile(uriPath).addOnSuccessListener {
                val pathStorage = loadData(requireActivity())
                if (pathStorage != null) {
                    Log.d("storage", pathStorage)
                    val refStorageDelete =
                        firebaseStorage.reference.child(pathStorage)
                    refStorageDelete.delete().addOnSuccessListener { delStorage ->
                        Log.d("storage", "Photo Sebelum nya berhasil di hapus")
                    }.addOnFailureListener { it ->
                        Log.d("storage", "Photo Sebelum nya gagal di hapus ${it.message}")
                    }
                }

                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    progressDialog.dismiss()
                    val uriImg = uri.toString()
                    val uid = auth.currentUser!!.uid

                    NetworkConfig(LocalKeys.LOCAL_BASE_URL)
                        .getUserService()
                        .getUserById(uid)
                        .enqueue(object : Callback<UserResponse> {
                            override fun onResponse(
                                call: Call<UserResponse>,
                                response: Response<UserResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val id = response.body()?.data?.id
                                    val name = response.body()?.data?.name
                                    val phone = response.body()?.data?.phone
                                    val email = response.body()?.data?.email
                                    val gender = response.body()?.data?.gender
                                    val title = response.body()?.data?.title
                                    val desc = response.body()?.data?.description
                                    val pathStorage = response.body()?.data?.pathStorageProfile
                                    val date = response.body()?.data?.createdDate

                                    if (id != null && name != null && phone != null && email != null && gender != null && title != null && desc != null && pathStorage != null && date != null) {
                                        saveData(id, name, phone, email, uriImg, date)
                                        UserApiService(requireActivity()).updateUser(
                                            id,
                                            name,
                                            phone,
                                            email,
                                            uriImg,
                                            gender,
                                            title,
                                            desc,
                                            refStorage.path,
                                            date = date
                                        )
                                    }

                                    dialogMessageAnimate(
                                        layoutInflater,
                                        requireContext(),
                                        "Data Updated",
                                        R.raw.successful,
                                        "Success"
                                    )
                                } else {
                                    Log.d(TAG, "Response Not Successfully")
                                    dialogMessageAnimate(
                                        layoutInflater,
                                        requireContext(),
                                        "Data Not Updated",
                                        R.raw.auth_failure,
                                        "Failure"
                                    )
                                }
                            }
                            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                               if (t is SocketTimeoutException){
                                   Log.d(TAG, "Error: ${t.message}")
                                   Toast.makeText(requireActivity(), "Error : ${t.message}", Toast.LENGTH_SHORT).show()
                                   dialogMessageAnimate(
                                       layoutInflater,
                                       requireContext(),
                                       "Error ${t.message}",
                                       R.raw.auth_failure,
                                       t.message.toString()
                                   )
                               } else {
                                   Log.d(TAG, "Error: ${t.message}")
                               }
                            }

                        })

                }
            }
        }
    }

    private fun popupMenu() {
        val popupMenu = PopupMenu(
            activity,
            binding.btnSettingAccount,
            Gravity.RIGHT,
            R.style.CustomPopupMenu,
            R.style.CustomPopupMenu
        )
        popupMenu.menuInflater.inflate(R.menu.pop_up_setting_account, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout_account -> {
                    auth.currentUser.let { currentUser ->
                        if (currentUser != null) {
                            auth.signOut()
                            startActivity(Intent(activity, LoginActivity::class.java))
                            activity?.finish()
                        }
                    }
                }
                R.id.update_account -> {
                    Toast.makeText(requireActivity(), "Activity belum dibuat", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            true
        }
        popupMenu.show()
    }


    private fun saveData(
        uid: String,
        namePar: String,
        phonePar: String,
        emailPar: String,
        photoUrl: String,
        date: String
    ) {
        FirebaseServiceInstance.databaseReference =
            FirebaseServiceInstance.firebaseDatabase.getReference("users").child(uid)

        val hashMap: HashMap<String, String> = HashMap()

        hashMap["photoUrl"] = photoUrl
        hashMap["phone"] = phonePar
        hashMap["name"] = namePar
        hashMap["email"] = emailPar
        hashMap["id"] = uid
        hashMap["title"] = "Hobi | Pekerjaan | ETC"
        hashMap["description"] = "Belum ingin menulis sesuatu"
        hashMap["pathStorageProfile"] = "profile path"
        hashMap["createdDate"] = date
        hashMap["modifiedDate"] = simpleDateFormat()

        FirebaseServiceInstance.databaseReference.setValue(hashMap)
            .addOnCompleteListener { database ->
                if (database.isSuccessful) {
                    Log.i(RegisterActivity.TAG, "Succesfully update data in realtime database")
                } else {
                    Log.w(RegisterActivity.TAG, "Failure update data in database")
                }
            }.addOnFailureListener {
                Log.w(RegisterActivity.TAG, it.message.toString())
            }

    }

}