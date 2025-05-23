package br.com.jrpbjr.creditapplicationsystem.service

import br.com.jrpbjr.creditapplicationsystem.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer
    fun delete(id: Long)
}