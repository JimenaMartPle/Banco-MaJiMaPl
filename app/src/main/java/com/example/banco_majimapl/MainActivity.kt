package com.example.banco_majimapl

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.banco_majimapl.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dni = intent.getStringExtra("dni") ?: ""
        binding.welcomeMsg.text = getString(R.string.mainWelcome, dni)

        setupMenuListeners()
    }

    private fun setupMenuListeners() {
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
            .setMessage("Introduce tu nueva contraseña (4 dígitos):")
            .setView(container)
            .setPositiveButton("Guardar") { dialog, _ ->
                val newPassword = input.text.toString()
                if (newPassword.length == 4) {
                    Toast.makeText(this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, getString(R.string.error_pass_length), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}