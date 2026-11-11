package com.example.banco_majimapl

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_majimapl.bd.MiBancoOperacional
import com.example.banco_majimapl.databinding.ActivityGlobalPositionBinding
import com.example.banco_majimapl.pojo.Cliente
import com.example.banco_majimapl.pojo.Cuenta

class GlobalPositionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGlobalPositionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalPositionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instanciar API
        val mbo = MiBancoOperacional.getInstance(this)

        //Recuperar el cliente (enviado desde el Login
        //val cliente = intent.getSerializableExtra("Cliente") as? Cliente   -----> Deprecado

        val cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //Metodo moderno para Android 13 (API 33) o superior 🚀
            intent.getSerializableExtra("Cliente", Cliente::class.java)
        } else {
            // Metodo clásico para versiones anteriores
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("Cliente") as? Cliente
        }

        //Obtener la lista de cuentas
        val listaCuentas = mbo?.getCuentas(cliente) as? ArrayList<Cuenta> ?: arrayListOf()

        //Configurar el RecyclerView con LinearLayoutManager
        binding.recyclerPosGlobal.layoutManager = LinearLayoutManager(this)

        //Asignar el adaptador con la lista de cuentas
        binding.recyclerPosGlobal.adapter = CuentasAdapter(listaCuentas)
    }
}