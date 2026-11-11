package com.example.banco_majimapl

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_majimapl.bd.MiBancoOperacional
import com.example.banco_majimapl.databinding.ActivityMovementsBinding
import com.example.banco_majimapl.pojo.Cliente
import com.example.banco_majimapl.pojo.Cuenta
import com.example.banco_majimapl.pojo.Movimiento

class MovementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovementsBinding
    private lateinit var adapter: MovimientosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Instanciar API
        val mbo = MiBancoOperacional.getInstance(this)

        // 2. Recuperar Cliente del Intent
        val cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("Cliente", Cliente::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("Cliente") as? Cliente
        }

        // 3. Obtener cuentas del cliente
        val listaCuentas = mbo?.getCuentas(cliente) as? ArrayList<Cuenta> ?: arrayListOf()
        val numerosCuenta: Array<String> = if (listaCuentas.isNotEmpty()) {
            listaCuentas.map { it.getNumeroCuenta() ?: "" }.toTypedArray()
        } else {
            arrayOf(getString(R.string.seleccionaSubtit))
        }

        // 4. Configurar Spinner de cuentas
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_cuentas_item, R.id.numCuentaItem, numerosCuenta)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_cuentas_item)
        binding.numCuentaMovSpinner.adapter = spinnerAdapter

        // 5. Configurar RecyclerView con su Adapter
        binding.recyclerMovimientos.layoutManager = LinearLayoutManager(this)
        adapter = MovimientosAdapter(arrayListOf())
        binding.recyclerMovimientos.adapter = adapter

        // 6. Escuchar los cambios de selección en el Spinner
        binding.numCuentaMovSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (listaCuentas.isNotEmpty()) {
                    val cuentaSeleccionada = listaCuentas[position]
                    // Obtener movimientos de la cuenta seleccionada desde la API
                    val listaMovimientos = mbo?.getMovimientos(cuentaSeleccionada) as? ArrayList<Movimiento> ?: arrayListOf()
                    adapter.actualizarMovimientos(listaMovimientos)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}