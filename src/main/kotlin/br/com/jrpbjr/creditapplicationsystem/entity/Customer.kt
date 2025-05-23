package br.com.jrpbjr.creditapplicationsystem.entity

data class Customer(
    var firstName: String = "",
    var lastName: String = "",
    val cpf: String = "",
    var email: String = "",
    val password: String = "",
    var addresses: Address = Address(),
    var credits: List<Credit> = mutableListOf(),
    var id: Long? = null
)
