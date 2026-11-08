package com.example.banco_majimapl

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.banco_majimapl.databinding.ActivityTransferBinding
import com.google.android.material.snackbar.Snackbar

class TransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Spinner
        val cuentas: Array<String> = arrayOf(
            "ES60-2100-0418-40-1234567891",
            "ES90-2080-5801-10-1234567891",
            "ES12-3456-7890-12-3456789012",
            "ES98-7654-3210-98-7654321098"
        )

        val divisa: Array<String> = arrayOf(
            "€",
            "$",
            "£",
            "¥"
        )

        // número de cuentas
        val arrayAdapterCuentas = ArrayAdapter(this, R.layout.spinner_cuentas_item, R.id.numCuentaItem, cuentas)

        val spinnerOrigen = binding.numCuentaSpinner

        arrayAdapterCuentas.setDropDownViewResource(R.layout.spinner_cuentas_item)
        spinnerOrigen.adapter = arrayAdapterCuentas

        val spinnerDestino = binding.miCuentaDestinoSpinner
        spinnerDestino.adapter = arrayAdapterCuentas

        // divisas
        val arrayAdapterDivisa = ArrayAdapter(this, R.layout.spinner_cuentas_item, R.id.numCuentaItem, divisa)

        val spinnerDivisa = binding.divisaSpinner

        arrayAdapterDivisa.setDropDownViewResource(R.layout.spinner_cuentas_item)
        spinnerDivisa.adapter = arrayAdapterDivisa

        //RadioButtons
        binding.radioButGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.miCuentaRadioBut -> {
                    binding.miCuentaDestinoSpinner.visibility = View.VISIBLE
                    binding.cuentaDestinaEdit.visibility = View.GONE
                }
                R.id.otraCuentaRadioBut -> {
                    binding.miCuentaDestinoSpinner.visibility = View.GONE
                    binding.cuentaDestinaEdit.visibility = View.VISIBLE
                }
            }
        }

        //Boton enviar
        binding.enviarTransfBut.setOnClickListener {
            //cuenta origen
            val cuentaOrigen = binding.numCuentaSpinner.selectedItem.toString()

            //cuenta destino
            val cuentaDestino = if (binding.miCuentaRadioBut.isChecked){
                getString(R.string.aCuentaPropia) + "\n" + binding.miCuentaDestinoSpinner.selectedItem.toString()
            } else {
                getString(R.string.aCuentaExterna) + "\n" + binding.cuentaDestinaEdit.text.toString()
            }

            //importe, divisa  y justificante
            val importe = binding.importeEdit.text.toString()
            val divisa = binding.divisaSpinner.selectedItem.toString()
            val justificante = if (binding.enviarJustiCheck.isChecked){
                getString(R.string.justificanteSi)
            } else {
                getString(R.string.justificanteNo)
            }

            // Toast ---> No se ve toda la info y la personalización del Toast esta deprecada.
            /*val resumenTrans = getString(
                R.string.toastTransferencia,
                cuentaOrigen,
                cuentaDestino,
                importe,
                divisa,
                justificante
            )
            Toast.makeText(this, resumenTrans, Toast.LENGTH_LONG).show()*/

            //Snackbar
            val resumenTrans = getString(
                R.string.toastTransferencia,
                cuentaOrigen,
                cuentaDestino,
                importe,
                divisa,
                justificante
            )
            val snackbar = Snackbar.make(binding.root, resumenTrans, Snackbar.LENGTH_LONG)
            //Personalizar para que muestre 5 lineas
            val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.maxLines = 5

            snackbar.show()
        }

        //Boton cancelar
        binding.cancelarTransfBut.setOnClickListener {
            //spinners
            binding.numCuentaSpinner.setSelection(0)
            binding.miCuentaDestinoSpinner.setSelection(0)
            binding.divisaSpinner.setSelection(0)
            //Edit text
            binding.importeEdit.text.clear()
            binding.cuentaDestinaEdit.text.clear()
            //Radio button
            binding.miCuentaRadioBut.isChecked = true
            //Checkbox
            binding.enviarJustiCheck.isChecked = false
        }
    }
}