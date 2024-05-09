package com.example.auntymess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.auntymess.Models.AttendanceItemModel
import com.example.auntymess.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().getReference()

        val uid=auth.currentUser?.uid

        binding.checkBalance.setOnClickListener {
            getMessId(uid){messId ->
                if (messId!=null){
                    val intent=Intent(this,BalanceActivity::class.java)
                    intent.putExtra("messId",messId)
                    startActivity(intent)
                }
            }
        }

        binding.presentdatesBtn.setOnClickListener {
            getMessId(uid){messId ->
                if(messId!=null){
                    val intent=Intent(this,PresentAbsentActivity::class.java)
                    intent.putExtra("activity","Main")
                    intent.putExtra("action","present")
                    intent.putExtra("messId",messId)
                    startActivity(intent)
                }
            }
        }

       binding.absentdatesBtn.setOnClickListener {
           getMessId(uid){messId->
               if(messId!=null){
                   val intent=Intent(this,PresentAbsentActivity::class.java)
                   intent.putExtra("activity","Main")
                   intent.putExtra("action","absent")
                   intent.putExtra("messId",messId)
                   startActivity(intent)
               }
           }
       }



        if(uid!=null){

            getMessId(uid){ messId ->
                if(messId!=null){
                    val attRef=database.child("MessOwners").child(messId).child("attendance").child(uid)

                    attRef.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                val attModel=snapshot.getValue(AttendanceItemModel::class.java)

                                if(attModel!=null){
                                    binding.presentCount.text=attModel.presentCount.toString()
                                    binding.absentCount.text=attModel.absentCount.toString()
                                    binding.messStartDate.text=attModel.startDate
                                    binding.messEndDate.text=attModel.endDate
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                }
            }



        }

    }

    private fun getMessId(uid: String?, callback: (String?) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference()

        database.child("MessOwners").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var messId: String? = null

                for(messSnapshot in snapshot.children){
                    if(messSnapshot.exists()){
                        val id=messSnapshot.key

                        val present=messSnapshot.child("users").child(uid!!).exists()

                        if(present){
                            messId=id
                            break
                        }
                    }
                }

                callback.invoke(messId)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.invoke(null)
            }

        })
    }
}