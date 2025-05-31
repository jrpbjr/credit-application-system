package br.com.jrpbjr.creditapplicationsystem.dto.request

import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.io.Serializable
import java.math.BigDecimal

/**
 * DTO for {@link br.com.jrpbjr.creditapplicationsystem.entity.Customer}
 */
data class CustomerUpdateDto(
    @field:NotEmpty(message = "O nome é obrigatório") val firstName: String,
    @field:NotEmpty(message = "O sobrenome é obrigatório") val lastName: String,
    @field:NotNull(message = "A renda é obrigatória")
    @field:Positive(message = "A renda deve ser maior que zero")
    @field:Digits(integer = 10, fraction = 2, message = "A renda deve ter no máximo 10 dígitos inteiros e 2 decimais")
    val income: BigDecimal,
    @field:NotEmpty(message = "O CEP é obrigatório") val zipCode: String,
    @field:NotEmpty(message = "O endereço é obrigatório") val street: String
) {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.street = this.street
        customer.address.zipCode = this.zipCode
        return customer
    }
}