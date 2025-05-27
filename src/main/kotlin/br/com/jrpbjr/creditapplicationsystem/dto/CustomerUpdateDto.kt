package br.com.jrpbjr.creditapplicationsystem.dto

import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import java.io.Serializable
import java.math.BigDecimal

/**
 * DTO for {@link br.com.jrpbjr.creditapplicationsystem.entity.Customer}
 */
data class CustomerUpdateDto(
    val firstName: String = "",
    val lastName: String = "",
    val income: BigDecimal = BigDecimal.ZERO,
    val zipCode: String = "",
    val street: String = ""
) : Serializable {
    fun toEntity(customer: Customer) : Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer
    }
}