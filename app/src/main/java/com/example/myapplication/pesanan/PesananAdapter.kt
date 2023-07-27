import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.OrderResponse
import com.example.myapplication.databinding.ItemPesananBinding

class PesananAdapter(
    private val orderList: List<OrderResponse?>,
    private val onItemClick: (OrderResponse) -> Unit
) : RecyclerView.Adapter<PesananAdapter.OrderViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        if (order != null) {
            holder.bind(order)
            holder.itemView.setOnClickListener {
                onItemClick(order)
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class OrderViewHolder(private val binding: ItemPesananBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: OrderResponse?) {
            binding.apply {
                order?.let {
                    binding.textMenuName.text = it.name
                    binding.textMenuAmount.text = "Jumlah: ${it.quantityOrder}"


                    if (it.quantityOrder!! <= it.quantityDelivered!!) {
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
}
