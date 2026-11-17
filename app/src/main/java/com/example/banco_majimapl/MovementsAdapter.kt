package com.example.banco_majimapl

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.banco_majimapl.databinding.DialogMovementBinding
import com.example.banco_majimapl.databinding.ItemMovimientoBinding
import com.example.banco_majimapl.pojo.Cuenta
import com.example.banco_majimapl.pojo.Movimiento
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovementsAdapter(private val listaMovimientos: ArrayList<Movimiento>) :
    RecyclerView.Adapter<MovementsAdapter.ViewHolder>() {

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

        holder.binding.root.setOnClickListener {
            mostrarDialogo(context, movimiento, fechaFormateada)
        }
    }

    // Método para actualizar los datos en el RecyclerView al cambiar de cuenta en el Spinner
    fun actualizarMovimientos(nuevaLista: ArrayList<Movimiento>) {
        listaMovimientos.clear()
        listaMovimientos.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    //Método para mostrar el cuadro de diálogo con el detalle de los movimientos
    private fun mostrarDialogo(context: Context, m: Movimiento, fecha: String) {
        // Inflamos la vista personalizada (como en la teoría, pero con binding)
        val dialogBinding = DialogMovementBinding.inflate(LayoutInflater.from(context))

        dialogBinding.dlgImporte.text = context.getString(R.string.dlgImporte, m.getImporte())
        dialogBinding.dlgDescripcion.text = context.getString(R.string.dlgDescripcion, m.getDescripcion() ?: "-")
        dialogBinding.dlgFecha.text = context.getString(R.string.dlgFecha, fecha)
        dialogBinding.dlgOrigen.text = context.getString(R.string.dlgOrigen, datosCuenta(m.getCuentaOrigen()))
        dialogBinding.dlgDestino.text = context.getString(R.string.dlgDestino, datosCuenta(m.getCuentaDestino()))

        // Mismo icono y color que en la fila
        if (m.getImporte() < 0) {
            dialogBinding.dlgIcono.setImageResource(R.drawable.ic_egreso)
            dialogBinding.dlgIcono.setColorFilter(ContextCompat.getColor(context, R.color.md_theme_errorContainer))
        } else {
            dialogBinding.dlgIcono.setImageResource(R.drawable.ic_ingreso)
            dialogBinding.dlgIcono.setColorFilter(ContextCompat.getColor(context, R.color.md_theme_primaryContainer))
        }

        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dlgTitulo)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.dlgCerrar) { dialog, _ -> dialog.cancel() }
            .show()
    }

    // Una cuenta puede ser null (p. ej. en un ingreso sin origen)
    private fun datosCuenta(c: Cuenta?): String =
        if (c == null) "-" else "${c.getBanco()} ${c.getSucursal()} ${c.getDc()} ${c.getNumeroCuenta()}"
}