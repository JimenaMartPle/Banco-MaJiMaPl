package com.example.banco_majimapl

import com.example.banco_majimapl.pojo.Cuenta

interface AccountsListener {
    fun onCuentaSeleccionada(cuenta: Cuenta)
}