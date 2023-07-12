package com.example.myapplication.pesanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityPesananBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class PesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.Pesanan)
        }

        binding.cardpesanan.setOnClickListener {
            val dialog = BottomSheetDialog(this)

            val view = layoutInflater.inflate(R.layout.note_dialogue, null)
            val btnPrint = view.findViewById<Button>(R.id.idBtnPrint)

            btnPrint.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setCancelable(true)
            dialog.setContentView(view)

            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.show()
        }
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