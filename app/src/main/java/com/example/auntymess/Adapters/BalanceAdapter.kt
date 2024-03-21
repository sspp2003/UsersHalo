package com.example.auntymess.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.auntymess.Models.BalanceItemModel
import com.example.auntymess.databinding.BalanceItemBinding

class BalanceAdapter(
    private val items: MutableList<BalanceItemModel>,
    //private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<BalanceAdapter.BalanceViewHolder>() {

    interface OnItemClickListener{
        fun OnPresentClick(balitem: BalanceItemModel)
        fun OnAbsentClick(balitem: BalanceItemModel)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BalanceItemBinding.inflate(inflater, parent, false)
        return BalanceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        val balitem=items[position]
        holder.bind(balitem)
    }

    inner class BalanceViewHolder(private val binding: BalanceItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(balitem: BalanceItemModel) {
            binding.messStartDate.text=balitem.startDate
            binding.messEndDate.text=balitem.endDate
            binding.balanceAmount.text=balitem.balanceamount

            binding.presentBtn.setOnClickListener {
               // itemClickListener.OnPresentClick(balitem)
            }
            binding.absentBtn.setOnClickListener {
               // itemClickListener.OnAbsentClick(balitem)
            }
        }

    }

}