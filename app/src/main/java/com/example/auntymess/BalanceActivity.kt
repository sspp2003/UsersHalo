package com.example.auntymess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auntymess.Adapters.BalanceAdapter
import com.example.auntymess.Models.BalanceItemModel
import com.example.auntymess.databinding.ActivityBalanceBinding
import com.example.auntymess.databinding.BalanceItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBalanceBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAdapter: BalanceAdapter
    private var ballist= mutableListOf<BalanceItemModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        databaseReference= FirebaseDatabase.getInstance().getReference()

        mAdapter= BalanceAdapter(ballist,object : BalanceAdapter.OnItemClickListener{
            override fun OnPresentClick(balitem: BalanceItemModel) {
                val intent=Intent(this@BalanceActivity,PresentAbsentActivity::class.java)
                intent.putExtra("activity","Balance")
                intent.putExtra("action","present")
                intent.putExtra("bal_id",balitem.balanceid)
                startActivity(intent)
            }

            override fun OnAbsentClick(balitem: BalanceItemModel) {
                val intent=Intent(this@BalanceActivity,PresentAbsentActivity::class.java)
                intent.putExtra("activity","Balance")
                intent.putExtra("action","absent")
                intent.putExtra("bal_id",balitem.balanceid)
                startActivity(intent)
            }

        })
        val recyclerview=binding.recyclerviewBalance
        recyclerview.layoutManager=LinearLayoutManager(this)
        recyclerview.adapter=mAdapter

        val userid=auth.currentUser!!.uid

        if(userid!=null){
            val balReference=databaseReference.child("balance").child(userid)

            balReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ballist.clear()
                    for (balsnapshot in snapshot.children){
                        val balItem=balsnapshot.getValue(BalanceItemModel::class.java)

                        if(balItem!=null){
                            val startDate=balItem.startDate
                            val endDate=balItem.endDate
                            val balance=balItem.balanceamount
                            val id=balItem.balanceid
                            val presentDates=balItem.presentDates
                            val absentDates=balItem.absentDates

                            val balitem=BalanceItemModel(
                                id,
                                startDate,
                                endDate,
                                balance,
                                presentDates,
                                absentDates
                            )

                            ballist.add(balitem)
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }
}