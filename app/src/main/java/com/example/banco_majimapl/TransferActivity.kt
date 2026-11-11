package com.example.banco_majimapl

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.banco_majimapl.bd.MiBancoOperacional
import com.example.banco_majimapl.databinding.ActivityTransferBinding
import com.example.banco_majimapl.pojo.Cliente
import com.example.banco_majimapl.pojo.Cuenta
import com.google.android.material.snackbar.Snackbar
import kotlin.collections.map

class TransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Spinner
        //1. Instanciar API
        val mbo = MiBancoOperacional.getInstance(this)

        // 2. Recuperar el cliente enviado desde MainActivity
        val cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("Cliente", Cliente::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("Cliente") as? Cliente
        }

        // 3. Obtener la lista de cuentas reales de la API
        val listaCuentas = mbo?.getCuentas(cliente) as? ArrayList<Cuenta> ?: arrayListOf()

        // 4. Transformar los objetos Cuenta en una lista con solo los números de cuenta
        val cuentas: Array<String> = if (listaCuentas.isNotEmpty()) {
            listaCuentas.map { it.getNumeroCuenta() ?: "" }.toTypedArray()
        } else {
            arrayOf("Sin cuentas disponibles")
        }

        val divisa: Array<String> = arrayOf("€", "$", "£", "¥")

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