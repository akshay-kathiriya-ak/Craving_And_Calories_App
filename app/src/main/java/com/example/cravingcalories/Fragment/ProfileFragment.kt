package com.example.cravingcalories.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cravingcalories.Model.UserModel
import com.example.cravingcalories.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private  var auth =FirebaseAuth.getInstance()
    private val database =FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        setUserData()
        binding.apply {
            userName.isEnabled = false
            userEmail.isEnabled = false
            userAddress.isEnabled= false
            userPhoneNumber.isEnabled= false
            binding.editbutton.setOnClickListener {
                userName.isEnabled = !userName.isEnabled
                userEmail.isEnabled = !userEmail.isEnabled
                userAddress.isEnabled= !userAddress.isEnabled
                userPhoneNumber.isEnabled= !userPhoneNumber.isEnabled
            }

        }

        binding.saveInfoButton.setOnClickListener {
            val name= binding.userName.text.toString()
            val email= binding.userEmail.text.toString()
            val address= binding.userAddress.text.toString()
            val phone= binding.userPhoneNumber.text.toString()
            updateUserData(name,email,address,phone)
        }

        // Inflate the layout for this fragment
        return binding.root

    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId =auth.currentUser?.uid
        if (userId != null){
            val userReference =database.getReference("user").child(userId)
            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Update Successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Profile Update Failed ",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserData() {
        val userId =auth.currentUser?.uid
        if (userId != null){
            val userReference = database.getReference("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile =snapshot.getValue(UserModel::class.java)
                        if (userProfile != null){
                            binding.userName.setText(userProfile.name)
                            binding.userAddress.setText(userProfile.address)
                            binding.userEmail.setText(userProfile.email)
                            binding.userPhoneNumber.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    companion object {

    }
}