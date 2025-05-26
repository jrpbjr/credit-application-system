package br.com.jrpbjr.creditapplicationsystem.dto

import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import java.io.Serializable
import java.math.BigDecimal

/**
 * DTO for {@link br.com.jrpbjr.creditapplicationsystem.entity.Customer}
 */
data class CustomerView(
    val firstName: String = "",
    val lastName: String = "",
    val cpf: String = "",
    val income: BigDecimal = BigDecimal.ZERO,
    val email: String = "",
    val zipCode: String = "",
    val street: String = ""

) : Serializable {
    constructor(customer: Customer) : this(
        customer.firstName,
        customer.lastName,
        customer.cpf,
        customer.income,
        customer.email,
        customer.address.zipCode,
        customer.address.street
    )
}