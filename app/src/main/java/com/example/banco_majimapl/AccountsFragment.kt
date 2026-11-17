package com.example.banco_majimapl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.banco_majimapl.bd.MiBancoOperacional
import com.example.banco_majimapl.databinding.FragmentAccountsBinding
import com.example.banco_majimapl.pojo.Cliente
import com.example.banco_majimapl.pojo.Cuenta

private const val ARG_CLIENTE = "cliente"

class AccountsFragment : Fragment(), AccountsListener {
    private var cliente: Cliente? = null
    private lateinit var binding: FragmentAccountsBinding
    private lateinit var accountsAdapter: AccountsAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var itemDecoration: DividerItemDecoration
    private var listener: AccountsListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cliente = it.getSerializable(ARG_CLIENTE) as? Cliente
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)

        val mbo = MiBancoOperacional.getInstance(context)
        val listaCuentas = mbo?.getCuentas(cliente) as? ArrayList<Cuenta> ?: arrayListOf()

        accountsAdapter = AccountsAdapter(listaCuentas, this)
        linearLayoutManager = LinearLayoutManager(context)
        itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        binding.recyclerPosGlobal.apply {
            layoutManager  = linearLayoutManager
            adapter = accountsAdapter
            addItemDecoration(itemDecoration)
        }
        return binding.root
    }
    //Método que llamará GlobalPositionActivity para escuchar la selección
    fun setAccountsListener(listener: AccountsListener){
        this.listener = listener
    }

    //Cuando el Adapter avisa del clic, el fragment lo reenvía a la activity
    override fun onCuentaSeleccionada(cuenta: Cuenta){
        listener?.onCuentaSeleccionada(cuenta)
    }

    companion object {
        @JvmStatic
        fun newInstance(c: Cliente) =
            AccountsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CLIENTE, c)
                }
            }
    }
}