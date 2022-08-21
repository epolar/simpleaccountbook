package xyz.eraise.simpleaccountbook.ui.payment.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import xyz.eraise.simpleaccountbook.R
import xyz.eraise.simpleaccountbook.databinding.FragmentAddPaymentBinding
import xyz.eraise.simpleaccountbook.pojo.Payment
import xyz.eraise.simpleaccountbook.repository.PaymentRepository
import xyz.eraise.simpleaccountbook.utils.PromptUtils

/**
 * Created by eraise on 2018/2/10.
 */
class AddPaymentFragment : Fragment() {

    private var _binding: FragmentAddPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPaymentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val name = binding.etPaymentName.text.toString()
        if (name.isEmpty()) {
            PromptUtils.toast(R.string.add_payment_name_should_not_be_empty)
            return
        }
        val payment = Payment(name)
        lifecycleScope.launch {
            PaymentRepository
                .savePayment(payment)
                .await().let { if (it) onSaveSuccess(payment) }
        }
    }

    private fun onSaveSuccess(payment: Payment) {
        Toast.makeText(context, R.string.add_payment_success, Toast.LENGTH_SHORT).show()
        ViewModelProvider(this)[AddPaymentViewModel::class.java]
            .addPayment
            .postValue(payment)
    }

}