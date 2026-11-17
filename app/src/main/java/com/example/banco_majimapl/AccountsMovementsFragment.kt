package com.example.banco_majimapl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_majimapl.bd.MiBancoOperacional
import com.example.banco_majimapl.databinding.FragmentAccountsMovementsBinding
import com.example.banco_majimapl.pojo.Cuenta
import com.example.banco_majimapl.pojo.Movimiento

private const val ARG_CUENTA = "cuenta"
class AccountsMovementsFragment : Fragment() {
    private lateinit var binding: FragmentAccountsMovementsBinding
    private var cuenta: Cuenta? = null
    private lateinit var movementsAdapter: MovementsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var itemDecoration: DividerItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cuenta = it.getSerializable(ARG_CUENTA) as? Cuenta
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountsMovementsBinding.inflate(inflater,container,false)

        val mbo = MiBancoOperacional.getInstance(context)
        val listaMovimientos = mbo?.getMovimientos(cuenta) as? ArrayList<Movimiento> ?: arrayListOf()

        movementsAdapter = MovementsAdapter(listaMovimientos)
        linearLayoutManager = LinearLayoutManager(context)
        itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        binding.recyclerMovimientos.apply {
            layoutManager = linearLayoutManager
            adapter = movementsAdapter
            addItemDecoration(itemDecoration)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(c: Cuenta) =
            AccountsMovementsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CUENTA, c)
                }
            }
    }
}