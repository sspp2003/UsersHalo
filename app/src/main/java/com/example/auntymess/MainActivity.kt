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
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference()

        binding.checkBalance.setOnClickListener {
            startActivity(Intent(this,BalanceActivity::class.java))
        }

        binding.presentdatesBtn.setOnClickListener {
            val intent=Intent(this,PresentAbsentActivity::class.java)
            intent.putExtra("activity","Main")
            intent.putExtra("action","present")
            startActivity(intent)
        }

       binding.absentdatesBtn.setOnClickListener {
           val intent=Intent(this,PresentAbsentActivity::class.java)
           intent.putExtra("activity","Main")
           intent.putExtra("action","absent")
           startActivity(intent)
       }

        val uid=auth.currentUser!!.uid

        if(uid!=null){
            val attRef=databaseReference.child("attendance").child(uid)

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