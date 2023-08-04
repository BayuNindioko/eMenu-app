package com.example.myapplication.detailReservasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.CheckinResponse
import com.example.myapplication.data.DetailReservationResponse
import com.example.myapplication.databinding.ActivityDetailReservasiBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class DetailReservasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailReservasiBinding

    private var tableId: Int = 0
    private var reservationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReservasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Reservasi")
        }
        val id = intent.getStringExtra("RES_ID")
        val number_table = intent.getStringExtra("NUMBER_TABLE")
        binding.noMeja.text = "Meja $number_table"


        val apiService = ApiConfig().getApiService()
        if (id != null) {
            apiService.detailReservation(id.toInt()).enqueue(object :
                Callback<DetailReservationResponse> {
                override fun onResponse(call: Call<DetailReservationResponse>, response: Response<DetailReservationResponse>) {
                    if (response.isSuccessful) {
                        val detailReservation = response.body()

                        detailReservation?.let {
                            if (it.status == "Finish") {
                                binding.status.text = "Status : Finish"
                                binding.pin.text = "-"
                                binding.waktuCheckin.text = "Waktu : -"
                                binding.checkout.isEnabled = false
                            } else {
                                binding.status.text = "Status : ${it.status}"
                                binding.pin.text = it.pin
                                binding.checkin.isEnabled = false
                                val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
                                val desiredFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm")

                                try {
                                    val date = originalFormat.parse(it.created_at)
                                    val formattedDate = desiredFormat.format(date)
                                    binding.waktuCheckin.text = "Waktu : ${formattedDate}"
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Log.d("aab", "Error while parsing date: ${e.message}")
                                    binding.waktuCheckin.text = "Waktu : ${it.created_at}"
                                }
                                tableId = it.table_id
                                reservationId = it.id

                            }
                        }
                    }

                    else{
                        Log.d("bayu", "$response.code")
                    }
                }

                override fun onFailure(call: Call<DetailReservationResponse>, t: Throwable) {
                    Log.d("bayu", "${t.message}")
                }
            })
        }

        binding.checkin.setOnClickListener{
            showBottomSheetDialog()
        }

        binding.checkout.setOnClickListener{
            checkoutReservation(tableId, reservationId)
            recreate()
        }

    }



    private fun showBottomSheetDialog() {
        val id = intent.getStringExtra("RES_ID")

        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.dialogue_checkin, null)

        val btnCheckin = bottomSheetView.findViewById<Button>(R.id.idBtnSelesai)
        val checkinEditText = bottomSheetView.findViewById<TextInputEditText>(R.id.checkinEditText)
        val checkinEditTextLayout = bottomSheetView.findViewById<TextInputLayout>(R.id.checkinEditTextLayout)
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setContentView(bottomSheetView)

        btnCheckin.setOnClickListener {
            val inputText = checkinEditText.text.toString()

            if (inputText.isNotEmpty()) {
                if (id != null) {
                    performCheckin(id.toInt(),inputText)
                }
                bottomSheetDialog.dismiss()
                recreate()
            } else {
                checkinEditTextLayout.error = "Nama harus diisi"
            }
        }

        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
    }

    private fun performCheckin(id:Int,inputText: String) {
        val apiService = ApiConfig().getApiService()

        apiService.checkIn(id,inputText).enqueue(object : Callback<CheckinResponse> {
            override fun onResponse(call: Call<CheckinResponse>, response: Response<CheckinResponse>) {
                if (response.isSuccessful) {
                    val checkinResponse = response.body()
                    Toast.makeText(this@DetailReservasiActivity,"Meja ${checkinResponse!!.table_id} berhasil check in", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailReservasiActivity, "Failed to check in. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CheckinResponse>, t: Throwable) {
                Log.e("bayo", "Checkin onFailure: ${t.message}")
                Toast.makeText(this@DetailReservasiActivity, "Failed to check in. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun checkoutReservation(tableId: Int, reservationId: Int) {
        val apiService = ApiConfig().getApiService()


        apiService.checkOut(tableId, reservationId).enqueue(object : Callback<DetailReservationResponse> {
            override fun onResponse(call: Call<DetailReservationResponse>, response: Response<DetailReservationResponse>) {
                if (response.isSuccessful) {
                    val checkoutResponse = response.body()
                    binding.status.text = "Status : ${checkoutResponse?.status}"
                    binding.waktuCheckin.text = "Waktu : -"
                    binding.pin.text = "-"
                    binding
                    Toast.makeText(this@DetailReservasiActivity,"Meja ${checkoutResponse!!.table_id} berhasil checkout", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("bayo", "Checkout failed: ${response.code()}")

                }
            }

            override fun onFailure(call: Call<DetailReservationResponse>, t: Throwable) {
                Log.e("bayo", "Checkout onFailure: ${t.message}")

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}