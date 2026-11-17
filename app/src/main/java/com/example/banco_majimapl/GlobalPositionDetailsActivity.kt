package com.example.banco_majimapl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.banco_majimapl.databinding.ActivityGlobalPositionDetailsBinding
import com.example.banco_majimapl.pojo.Cuenta

class GlobalPositionDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGlobalPositionDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalPositionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recuperar cuenta seleccionada
        val cuentaSeleccionada = intent.getSerializableExtra("Cuenta") as Cuenta

        //Crear instancia del fragment
        val frgMovements = AccountsMovementsFragment.newInstance(cuentaSeleccionada)

        //Reemplazar el contenedor del XML con el fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentAccountsDetMove, frgMovements)
            .commitNow()
    }
}
