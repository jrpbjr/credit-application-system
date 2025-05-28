package br.com.jrpbjr.creditapplicationsystem.dto

import br.com.jrpbjr.creditapplicationsystem.entity.Address
import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "O nome é obrigatório")
    val firstName: String,
    @field:NotEmpty(message = "O sobrenome é obrigatório")
    val lastName: String,
    @field:NotNull(message = "A renda é obrigatória")
    @field:Positive(message = "A renda deve ser maior que zero")
    @field:Digits(integer = 10, fraction = 2, message = "A renda deve ter no máximo 10 dígitos inteiros e 2 decimais")
    val income: BigDecimal,
    @field:NotEmpty(message = "O e-mail é obrigatório")
    @field:Email(message = "O formato do E-mail está invalido")
    val email: String,
    @field:NotEmpty(message = "O CEP é obrigatório")
    val zipCode: String,
    @field:NotEmpty(message = "O endereço é obrigatório")
    val street: String
) {
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        income = this.income,
        email = this.email,
        address = Address(zipCode = this.zipCode, street = this.street)

    )

}
