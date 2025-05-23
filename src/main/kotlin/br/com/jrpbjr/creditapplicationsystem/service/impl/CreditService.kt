package br.com.jrpbjr.creditapplicationsystem.service.impl

import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import br.com.jrpbjr.creditapplicationsystem.repository.CreditRepository
import br.com.jrpbjr.creditapplicationsystem.service.ICreditService
import java.util.*

class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {

    override fun save(credit: Credit): Credit {
      credit.apply {
          customer = customerService.findById(credit.customer?.id!!)
      }
      return  this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> {
        TODO("Not yet implemented")
    }

    override fun findByCreditCode(creditCode: UUID): Credit {
        TODO("Not yet implemented")
    }


}