package com.example.banco_majimapl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.banco_majimapl.databinding.ItemMovimientoBinding
import com.example.banco_majimapl.pojo.Movimiento
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovimientosAdapter(private val listaMovimientos: ArrayList<Movimiento>) :
    RecyclerView.Adapter<MovimientosAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMovimientoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovimientoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listaMovimientos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movimiento = listaMovimientos[position]
        val context = holder.itemView.context

        // 1. Formatear la fecha
        val fechaMs = movimiento.getFechaOperacion()?.time ?: 0L
        val fechaFormateada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(fechaMs))

        // 2. Detalle del movimiento
        holder.binding.detalleMovimientoText.text = context.getString(
            R.string.textDetalleMov,
            movimiento.getDescripcion() ?: ""
        )

        // 3. Texto combinando fecha e importe
        val importe = movimiento.getImporte() ?: 0f
        holder.binding.fechaImporteMov.text = context.getString(
            R.string.textFechaImporteMov,
            fechaFormateada,
            importe
        )

        // 4. Lógica de Icono y Color dinámico según si es Ingreso (>= 0) o Egreso (< 0) 🎨
        if (importe < 0) {
            // Egreso
            holder.binding.imgCardMov.setImageResource(R.drawable.ic_egreso)
            val colorError = ContextCompat.getColor(context, R.color.md_theme_errorContainer)
            holder.binding.imgCardMov.setColorFilter(colorError)
        } else {
            // Ingreso
            holder.binding.imgCardMov.setImageResource(R.drawable.ic_ingreso)
            val colorPrimary = ContextCompat.getColor(context, R.color.md_theme_primaryContainer)
            holder.binding.imgCardMov.setColorFilter(colorPrimary)
        }
    }

    // Método para actualizar los datos en el RecyclerView al cambiar de cuenta en el Spinner
    fun actualizarMovimientos(nuevaLista: ArrayList<Movimiento>) {
        listaMovimientos.clear()
        listaMovimientos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}