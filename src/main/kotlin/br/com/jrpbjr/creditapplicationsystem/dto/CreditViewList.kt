package br.com.jrpbjr.creditapplicationsystem.dto

import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import java.io.Serializable
import java.math.BigDecimal
import java.util.UUID

data class CreditViewList(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallments: Int
) : Serializable {
    constructor(credit: Credit) : this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments
    )
}