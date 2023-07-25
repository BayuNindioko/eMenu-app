import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.OrderResponseItem
import com.example.myapplication.databinding.ItemPesananBinding

class PesananAdapter(private val orderList: List<OrderResponseItem?>) :
    RecyclerView.Adapter<PesananAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class OrderViewHolder(private val binding: ItemPesananBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: OrderResponseItem?) {
            binding.apply {
                order?.let {
                    binding.textMenuName.text = it.name
                    binding.textMenuAmount.text = "Jumlah: ${it.quantityOrder}"

                }
            }
        }
    }
}
