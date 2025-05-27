package br.com.jrpbjr.creditapplicationsystem.dto

import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import br.com.jrpbjr.creditapplicationsystem.enummeration.Status
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

/**
 * DTO for {@link br.com.jrpbjr.creditapplicationsystem.entity.Credit}
 */
data class CreditDto(
    val creditValue: BigDecimal = BigDecimal.ZERO,
    val dayFirstInstallment: LocalDate? = null,
    val numberOfInstallments: Int = 0,
    val customerId: Long? = null
) : Serializable {

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment!!,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId!!)
    )

}