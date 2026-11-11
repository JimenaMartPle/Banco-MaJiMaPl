package com.example.banco_majimapl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.banco_majimapl.databinding.ItemCuentaBinding
import com.example.banco_majimapl.pojo.Cuenta

class CuentasAdapter(private val listaCuentas: ArrayList<Cuenta>) :
    RecyclerView.Adapter<CuentasAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCuentaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCuentaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listaCuentas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cuenta = listaCuentas[position]
        val context = holder.itemView.context

        // 1. Número de cuenta formateado con strings.xml
        holder.binding.numCuentaCardPosGlobal.text = context.getString(
            R.string.textNumCuentaPosGlobal,
            cuenta.getNumeroCuenta()
        )

        // 2. Saldo formateado
        holder.binding.saldoCardPosGlobal.text = context.getString(
            R.string.textSaldoPosGlobal,
            cuenta.getSaldoActual().toString()
        )

        // 3. Color según saldo positivo o negativo 🎨
        val colorRes = if ((cuenta.getSaldoActual() ?: 0f) < 0) {
            R.color.md_theme_errorContainer
        } else {
            R.color.md_theme_primary
        }

        val color = ContextCompat.getColor(context, colorRes)
        holder.binding.saldoCardPosGlobal.setTextColor(color)
        holder.binding.imgCardPosGlobal.setColorFilter(color)
    }
}