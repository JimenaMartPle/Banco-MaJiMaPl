package com.example.banco_majimapl

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.banco_majimapl.databinding.ActivityGlobalPositionBinding
import com.example.banco_majimapl.pojo.Cliente
import com.example.banco_majimapl.pojo.Cuenta

class GlobalPositionActivity : AppCompatActivity(), AccountsListener {

    private lateinit var binding: ActivityGlobalPositionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalPositionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Recuperar el cliente (enviado desde el Main) -----> Deprecado ↓↓
        val clienteLogeado = intent.getSerializableExtra("Cliente") as Cliente

        // Esta es la versión actual sugerida por Andorid ↓

        /*val cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //Metodo moderno para Android 13 (API 33) o superior 🚀
            intent.getSerializableExtra("Cliente", Cliente::class.java)
        } else {
            // Metodo clásico para versiones anteriores
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("Cliente") as? Cliente
        }*/

        // Crear una instancia del fragment pasándole el cliente
        val frgAccounts: AccountsFragment = AccountsFragment.newInstance(clienteLogeado)
        frgAccounts.setAccountsListener(this)

        //Reemplazar el contenedor del XML con el fragment cargado
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentAccounts, frgAccounts)
            .commitNow()
    }

    override fun onCuentaSeleccionada(cuenta: Cuenta) {
        //Comprobar si en el layout cargado exite el segundo fragment
        val hayDetalle =
            supportFragmentManager.findFragmentById(R.id.fragmentAccountsDetMove) != null

        if (hayDetalle) {
            //Si estamos en tablet habrá dos fragments, por lo que hacemos:
            val movementsFragment = AccountsMovementsFragment.newInstance(cuenta)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentAccountsDetMove, movementsFragment)
                .commitNow()
        } else {
            //Si es false es porque estamos en un móvil entonces hacemos:
            val intent = Intent(this, GlobalPositionDetailsActivity::class.java)
            intent.putExtra("Cuenta", cuenta)
            startActivity(intent)
        }
    }
}