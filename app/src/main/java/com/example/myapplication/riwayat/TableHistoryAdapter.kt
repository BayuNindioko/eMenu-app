package com.example.myapplication.riwayat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.TableResponseItem
import com.example.myapplication.pesanan.PesananActivity

class TableHistoryAdapter(private val tableList: List<TableResponseItem?>) :
    RecyclerView.Adapter<TableHistoryAdapter.TableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val table = tableList[position]
        holder.bind(table)
    }

    override fun getItemCount(): Int {
        return tableList.size
    }

    inner class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val numberTextView: TextView = itemView.findViewById(R.id.item_tv_minutes)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val table = tableList[position]
                    table?.let {
                        val number = it.number

                        val intent = Intent(itemView.context, RiwayatByTable::class.java)
                        intent.putExtra("NUMBER_KEY_HISTORY", number)
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
        fun bind(table: TableResponseItem?) {
            table?.let {
                numberTextView.text = it.number
            }
        }
    }
}