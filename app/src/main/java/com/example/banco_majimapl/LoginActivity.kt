package com.example.banco_majimapl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.banco_majimapl.databinding.ActivityLoginBinding
import android.content.Intent
import android.widget.Toast
import com.example.banco_majimapl.bd.MiBancoOperacional
import com.example.banco_majimapl.pojo.Cliente

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instanciar API
        val mbo = MiBancoOperacional.getInstance(this)

        //Accion del botón "SALIR"
        binding.signOutButton.setOnClickListener {
            finishAffinity()
        }

        //Accion del botón "ENTRAR"
        binding.logInButton.setOnClickListener {
            val dni = binding.userNameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()

            binding.userNameLayout.error =
                if (dni.isEmpty()) getString(R.string.error_user_empty) else null

            binding.passwordLayout.error = when {
                password.isEmpty() -> getString(R.string.error_pass_empty)
                password.length != 4 -> getString(R.string.error_pass_length)
                else -> null
            }

            if (dni.isNotEmpty() && password.length == 4) {
                val c = Cliente()
                c.setNif(dni)
                c.setClaveSeguridad(password)

                val clienteLogeado = mbo?.login(c)
                if (clienteLogeado != null) {
                    // Autenticación correcta: pasamos el objeto a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("Cliente", clienteLogeado)
                    startActivity(intent)
                    finish() //
                } else {
                    // Datos incorrectos: mostramos un mensaje de error
                    Toast.makeText(this, getString(R.string.error_login_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}