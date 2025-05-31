package br.com.jrpbjr.creditapplicationsystem.dto.response

import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import br.com.jrpbjr.creditapplicationsystem.enummeration.Status
import java.io.Serializable
import java.math.BigDecimal
import java.util.UUID

/**
 * DTO for {@link br.com.jrpbjr.creditapplicationsystem.entity.Credit}
 */
data class CreditView(
    val creditCode: UUID = UUID.randomUUID(),
    val creditValue: BigDecimal = BigDecimal.ZERO,
    val numberOfInstallments: Int = 0,
    val status: Status = Status.IN_PROGRESS,
    val emailCustomer: String? = "",
    val incomeCustomer: BigDecimal? = BigDecimal.ZERO

) : Serializable {
    constructor(credit: Credit) : this (
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments,
        status = credit.status,
        emailCustomer = credit.customer?.email ?: "",
        incomeCustomer = credit.customer?.income ?: BigDecimal.ZERO
    )
}