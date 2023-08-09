package com.example.myapplication.detailReservasi

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.CheckinResponse
import com.example.myapplication.data.DetailReservationResponse
import com.example.myapplication.data.OrderItem
import com.example.myapplication.data.OrderResponse
import com.example.myapplication.databinding.ActivityDetailReservasiBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.OutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class DetailReservasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailReservasiBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var mmSocket: BluetoothSocket
    private lateinit var mmOutputStream: OutputStream

    private var tableId: Int = 0
    private var reservationId: Int = 0

    private var latestOrderItemsz: List<OrderItem>? = null
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

        binding.progressBar2.visibility = View.VISIBLE

        val apiService = ApiConfig().getApiService()
        if (id != null) {
            apiService.detailReservation(id.toInt()).enqueue(object :
                Callback<DetailReservationResponse> {
                override fun onResponse(call: Call<DetailReservationResponse>, response: Response<DetailReservationResponse>) {
                    if (response.isSuccessful) {
                        val detailReservation = response.body()
                        binding.progressBar2.visibility = View.GONE

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
                                loaddata(reservationId)
                            }
                        }
                    }

                    else{
                        Log.d("bayu", "$response.code")
                        Toast.makeText(this@DetailReservasiActivity, "Gagal memuat data. Silakan coba lagi.", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<DetailReservationResponse>, t: Throwable) {
                    binding.progressBar2.visibility = View.GONE
                    Toast.makeText(this@DetailReservasiActivity, "Gagal memuat data. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                    Log.d("bayu", "${t.message}")
                }
            })
        }

        binding.checkin.setOnClickListener{
            showBottomSheetDialog()
        }

        binding.checkout.setOnClickListener{
            checkoutReservation(reservationId,tableId)
            recreate()
        }


        binding.fab.setOnClickListener{

            requestBluetoothConnectPermission()

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

    private fun checkoutReservation(reservationId: Int,tableId: Int ) {
        val apiService = ApiConfig().getApiService()

        apiService.checkOut( reservationId,tableId).enqueue(object : Callback<DetailReservationResponse> {
            override fun onResponse(call: Call<DetailReservationResponse>, response: Response<DetailReservationResponse>) {
                if (response.isSuccessful) {
                    val checkoutResponse = response.body()
                    binding.status.text = "Status : ${checkoutResponse?.status}"
                    binding.waktuCheckin.text = "Waktu : -"
                    binding.pin.text = "-"
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

// PRINTTTTTTTTTTTTTTTT // PRINTTTTTTTTTTTTTTTT // PRINTTTTTTTTTTTTTTTT // PRINTTTTTTTTTTTTTTTT // PRINTTTTTTTTTTTTTTTT // PRINTTTTTTTTTTTTTTTT

    private fun loaddata(id:Int) {
        val apiService = ApiConfig().getApiService()

        Log.d("bayyuu", "ID: $id")
        id.let {
            apiService.getOrderByTable(id.toString()).enqueue(object :
                Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    Log.d("bayubayu", "onResponse is called")
                    if (response.isSuccessful) {
                        val detailReservation = response.body()
                        Log.d("bayubayu", "onResponse is success")
                        detailReservation?.let { it ->
                            latestOrderItemsz = it.order_items
                            Log.d("dapet", "Latest Order Items: $latestOrderItemsz")

                        }
                    } else {
                        // Handle API response error here
                        Toast.makeText(
                            this@DetailReservasiActivity,
                            "Failed to retrieve order items.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    // Handle API call failure here
                    Toast.makeText(
                        this@DetailReservasiActivity,
                        "Failed to retrieve order items.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun requestBluetoothConnectPermission() {
        val permission = Manifest.permission.BLUETOOTH_CONNECT
        val requestCode = 1


        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                latestOrderItemsz?.let { orderItems ->
                    Log.d("bayu mamam", "Order Items: $orderItems")
                    doPrint(binding.root, orderItems)
                } ?: run {
                    Toast.makeText(this, "Data pesanan tidak ditemukann.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Izin Bluetooth tidak diberikan. Aplikasi memerlukan izin Bluetooth untuk mencetak.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkBluetoothPermissions(): Boolean {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {

            return false
        }

        if (!bluetoothAdapter.isEnabled) {

            return false
        }

        return true
    }

    @SuppressLint("MissingPermission")
    private fun findPrinterDevice(): BluetoothDevice? {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        if (pairedDevices != null) {
            for (device: BluetoothDevice in pairedDevices) {

                if (device.bluetoothClass.majorDeviceClass == BluetoothClass.Device.Major.IMAGING) {
                    return device
                }
            }
        }
        return null
    }

    @SuppressLint("MissingPermission")
    private fun connectToPrinter(): Boolean {
        val printerDevice = findPrinterDevice()
        if (printerDevice != null) {
            val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            mmSocket = printerDevice.createRfcommSocketToServiceRecord(uuid)
            try {
                mmSocket.connect()
                mmOutputStream = mmSocket.outputStream
                return true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return false
    }

    private fun disconnectPrinter() {
        try {
            mmOutputStream.close()
            mmSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun doPrint(view: View, orderItems: List<OrderItem>) {
        val no_table = intent.getStringExtra("NUMBER_TABLE")
        Log.d("keren", "doPrint - latestOrderItemsz: $latestOrderItemsz")
        Log.d("keren", "nomer meja: $no_table")

        var total = 0.00

        if (checkBluetoothPermissions()) {
            val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            try {
                if (connectToPrinter()) {
                    val billData = StringBuilder()

                    billData.append("================================\n")
                    billData.append("        JEBE Cafe & Resto       \n")
                    billData.append("================================\n")
                    billData.append("No Meja: $no_table\n")
                    billData.append("Tanggal: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}\n")
                    billData.append("Waktu  : ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())}\n")
                    billData.append("================================\n")
                    billData.append("Item          Jumlah     Harga  \n")
                    billData.append("================================\n")
                    for (orderItem in orderItems) {

                        val itemName = orderItem.name.padEnd(16)
                        val itemQuantity = orderItem.quantity_order.toString().padEnd(9)
                        val itemPrice = orderItem.price*orderItem.quantity_order

                        total += itemPrice

                        billData.append("$itemName$itemQuantity$itemPrice\n")

                    }

                    val formattedTotal = currencyFormat.format(total)
                    val pajak = currencyFormat.format(total*0.11)
                    val pajaktotal = currencyFormat.format(total +total*0.11)

                    billData.append("================================\n")
                    billData.append("Sub total : $formattedTotal\n")
                    billData.append("PPN 11%   : $pajak\n")
                    billData.append("Total     : $pajaktotal\n")
                    billData.append("================================\n")
                    billData.append("     SELAMAT DATANG KEMBALI     \n")
                    billData.append("================================\n")
                    billData.append("\n")
                    billData.append("\n")

                    try {
                        mmOutputStream.write(billData.toString().toByteArray())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        disconnectPrinter()
                    }
                }
            } catch (securityException: SecurityException) {
                securityException.printStackTrace()
            }
        } else {
            Toast.makeText(
                this,
                "Izin Bluetooth tidak diberikan. Aplikasi memerlukan izin Bluetooth untuk mencetak.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}