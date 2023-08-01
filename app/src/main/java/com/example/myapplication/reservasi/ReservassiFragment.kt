import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.antrian.TableAdapter
import com.example.myapplication.databinding.FragmentReservassiBinding


class ReservassiFragment : Fragment() {

    private var _binding: FragmentReservassiBinding? = null
    private lateinit var tableAdapter: TableAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReservassiBinding.inflate(inflater, container, false)
        val view = binding.root

        tableAdapter = TableAdapter(emptyList())
//        binding.rvHistory.layoutManager = GridLayoutManager(requireContext(), 2)
//
//
//        val apiService = ApiConfig().getApiService()
//
//        apiService.getAllTable().enqueue(object : Callback<List<TableResponseItem>> {
//            override fun onResponse(call: Call<List<TableResponseItem>>, response: Response<List<TableResponseItem>>) {
//                if (response.isSuccessful) {
//                    val tableList = response.body()
//                    tableList?.let {
//                        tableAdapter = TableHistoryAdapter(it)
//                        binding.rvHistory.adapter = tableAdapter
//                    }
//                } else {
//                    Log.d("aldo", "${response.code()}")
//                    Toast.makeText(requireContext(), "Failed to get data", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<TableResponseItem>>, t: Throwable) {
//                Log.e("aldo", "Error: ${t.message}")
//                t.printStackTrace()
//                Toast.makeText(requireContext(), "Failed to makan", Toast.LENGTH_SHORT).show()
//            }
//        })
//
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding
        _binding = null
    }

}