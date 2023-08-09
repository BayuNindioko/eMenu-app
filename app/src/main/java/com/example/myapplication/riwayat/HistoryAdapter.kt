import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.OrderItem
import com.example.myapplication.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat


class HistoryAdapter(
    private val itemsList: List<OrderItem?>,
    private val onItemClick: (OrderItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ItemViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: OrderItem) {
            binding.apply {
                binding.textMenuName.text = item.name
                binding.textMenuAmount.text = "Jumlah: ${item.quantity_order}"
                val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
                val desiredFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm")

                try {
                    val date = originalFormat.parse(item.created_at)
                    val formattedDate = desiredFormat.format(date)
                    binding.tgl.text = formattedDate

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("aab", "Error while parsing date: ${e.message}")
                    binding.tgl.text = item.created_at


                }


                Glide.with(itemView.context)
                    .load(item.item.foto)
                    .centerCrop()
                    .into(binding.imageMenu)

            }
        }
    }
}

