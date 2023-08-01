import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Items
import com.example.myapplication.databinding.ItemPesananBinding

class PesananAdapter(
    private var itemsList: List<Items?>,
    private val onItemClick: (Items) -> Unit
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
    fun updateItems(newItemsList: List<Items?>) {
        itemsList = newItemsList
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val binding: ItemPesananBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Items) {
            binding.apply {
                binding.textMenuName.text = item.name
                binding.textMenuAmount.text = "Jumlah: ${item.quantity_order}"

                if (item.quantity_order <= item.quantity_delivered) {
                    // If they are equal, set the status order image to the logo
                    binding.status.setImageResource(R.drawable.baseline_check_circle_24)
                } else {
                    // Otherwise, set the status order image to the default image
                    binding.status.setImageResource(R.drawable.baseline_access_time_24)
                }
            }
        }
    }
}

