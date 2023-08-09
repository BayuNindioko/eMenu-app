import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.OrderItem
import com.example.myapplication.databinding.ItemPesananBinding

class PesananAdapter(
    private var itemsList: List<OrderItem?>,
    private val onItemClick: (OrderItem) -> Unit
) : RecyclerView.Adapter<PesananAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemsList[position]
        item?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                onItemClick(item)
            }

        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    // Method to update the items list
    fun updateItems(newItemsList: List<OrderItem?>) {
        itemsList = newItemsList
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val binding: ItemPesananBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: OrderItem) {
            binding.apply {
                binding.textMenuName.text = item.name
                binding.textMenuAmount.text = "Jumlah: ${item.quantity_order}"
                binding.textStatus.text = "Orderan ke - ${item.order_id}"

                if (item.quantity_order <= item.quantity_delivered) {
                    binding.status.setImageResource(R.drawable.baseline_check_circle_24)
                } else {
                    binding.status.setImageResource(R.drawable.baseline_access_time_24)
                }

                Log.d("FotoDebugaa", "Item ID: ${item.id}, Foto: ${item.item?.foto}")

                Glide.with(itemView.context)
                    .load(item.item?.foto)
                    .centerCrop()
                    .into(binding.imageMenu)


            }

        }
    }
}

