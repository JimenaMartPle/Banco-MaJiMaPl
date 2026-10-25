package com.example.banco_majimapl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.banco_majimapl.databinding.ActivityLoginBinding
import android.content.Intent

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                password.length < 4 -> getString(R.string.error_pass_length)
                else -> null
            }

            if (dni.isNotEmpty() && password.length == 4) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("dni", dni)
                startActivity(intent)
                finish()
            }
        }
    }
}