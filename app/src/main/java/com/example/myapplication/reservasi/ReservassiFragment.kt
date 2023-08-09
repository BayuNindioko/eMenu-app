import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentReservassiBinding
import com.example.myapplication.reservasi.ReservasiAdapter
import com.example.myapplication.reservasi.ReservationViewModel


class ReservassiFragment : Fragment() {

    private var _binding: FragmentReservassiBinding? = null
    private lateinit var tableAdapter: ReservasiAdapter
    private lateinit var reservasiViewModel: ReservationViewModel
    private val binding get() = _binding!!

    private val fetchHandler = Handler()
    private val fetchRunnable = object : Runnable {
        override fun run() {
            reservasiViewModel.loadTableData()
                fetchHandler.postDelayed(this, 10000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReservassiBinding.inflate(inflater, container, false)
        val view = binding.root

        tableAdapter = ReservasiAdapter(emptyList())
        binding.rvReservation.layoutManager = LinearLayoutManager(requireContext())


        reservasiViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)
        reservasiViewModel.getTableData().observe(viewLifecycleOwner) { tableList ->
            tableAdapter.updateTableList(tableList)
            binding.rvReservation.adapter = tableAdapter
        }

        reservasiViewModel.loadTableData()
        fetchHandler.postDelayed(fetchRunnable, 20000)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}