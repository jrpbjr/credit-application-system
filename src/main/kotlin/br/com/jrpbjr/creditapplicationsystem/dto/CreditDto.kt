package br.com.jrpbjr.creditapplicationsystem.dto

import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import br.com.jrpbjr.creditapplicationsystem.enummeration.Status
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

/**
 * DTO for {@link br.com.jrpbjr.creditapplicationsystem.entity.Credit}
 */
data class CreditDto(
    @field:NotNull(message = "O valor do crédito é obrigatório")
    @field:Positive(message = "O valor do crédito deve ser maior que zero")
    @field:Digits(
        integer = 10,
        fraction = 2,
        message = "O valor do crédito deve ter no máximo 10 dígitos inteiros e 2 decimais"
    )
    @field:DecimalMin(value = "100.0", message = "O valor mínimo do crédito é R$ 100,00")
    @field:DecimalMax(value = "1000000.0", message = "O valor máximo do crédito é R$ 1.000.000,00")
    val creditValue: BigDecimal = BigDecimal.ZERO,
    @field:NotNull(message = "A data da primeira parcela é obrigatória")
    @field:FutureOrPresent(message = "A data da primeira parcela deve ser atual ou futura")
    val dayFirstInstallment: LocalDate? = null,
    @field:NotNull(message = "O número de parcelas é obrigatório")
    @field:Min(value = 1, message = "O número mínimo de parcelas é 1")
    @field:Max(value = 48, message = "O número máximo de parcelas é 48")
    val numberOfInstallments: Int = 0,
    @field:NotNull(message = "O ID do cliente é obrigatório")
    @field:Positive(message = "O ID do cliente deve ser um número positivo")
    val customerId: Long? = null
) : Serializable {

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment!!,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId!!)
    )

}