package com.example.banco_majimapl

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.banco_majimapl.bd.MiBancoOperacional
import com.example.banco_majimapl.databinding.ActivityMainBinding
import com.example.banco_majimapl.pojo.Cliente

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var cliente : Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar el cliente enviado desde LoginActivity
        cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("Cliente", Cliente::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("Cliente") as? Cliente
        }

        //Mensaje de bienvenida
        val nombreMostrar = cliente?.getNombre() ?: intent.getStringExtra("dni") ?: ""
        binding.welcomeMsg.text = getString(R.string.mainWelcome, nombreMostrar)

        setupMenuListeners()
    }

    private fun setupMenuListeners() {
        // Opción 1: Posición Global
        binding.opButton1.setOnClickListener{
            val intent = Intent(this, GlobalPositionActivity::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)
        }

        // Opción 2: Movimientos
        binding.opButton2.setOnClickListener {
            val intent = Intent(this, MovementsActivity::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)
        }

        // Opción 3: Transferencias
        binding.opButton3.setOnClickListener{
            val intent = Intent(this, TransferActivity::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)
        }

        // Opción 4: Cambiar contraseña
        binding.opButton4.setOnClickListener {
            showChangePasswordDialog()
        }

        // Opción 7: Salir
        binding.opButton7.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            // Limpia la pila de actividades para que el usuario no pueda volver atrás con el botón físico
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showChangePasswordDialog() {
        val input = EditText(this).apply {
            hint = getString(R.string.pass)
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        }

        val container = FrameLayout(this).apply {
            val padding = (20 * resources.displayMetrics.density).toInt()
            setPadding(padding, 16, padding, 0)
            addView(input)
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.opButText4)
            .setMessage(R.string.changePassText)
            .setView(container)
            .setPositiveButton(R.string.guardarBtn) { dialog, _ ->
                val newPassword = input.text.toString()
                if (newPassword.length == 4) {
                    // 1. Instanciar la API y actualizar la contraseña en el objeto cliente
                    val mbo = MiBancoOperacional.getInstance(this)
                    cliente?.setClaveSeguridad(newPassword)

                    // 2. Persistir el cambio mediante la API
                    val resultado = mbo?.changePassword(cliente)

                    // 3. Validar la respuesta de la BD con textos internacionalizados
                    if (resultado != null && resultado > 0) {
                        Toast.makeText(this, R.string.cambioOkText, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, R.string.error_change_pass, Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, getString(R.string.error_pass_length), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.cancelarBtn) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}