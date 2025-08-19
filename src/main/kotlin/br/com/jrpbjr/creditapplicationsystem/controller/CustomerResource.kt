package br.com.jrpbjr.creditapplicationsystem.controller

import br.com.jrpbjr.creditapplicationsystem.entity.Address
import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.api.CustomersApi
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.CustomerDto
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.CustomerUpdateDto
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.CustomerView
import br.com.jrpbjr.creditapplicationsystem.service.impl.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customers")
class CustomerResource(
    private val customerService: CustomerService
) : CustomersApi {

    @PostMapping
    override fun saveCustomer(@RequestBody customerDto: CustomerDto): ResponseEntity<CustomerView> {
        val savedCustomer: Customer = this.customerService.save(customerDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer.toViewDto())
    }

    @GetMapping("/{id}")
    override fun findCustomerById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(customer.toViewDto())
    }

    @DeleteMapping("/{id}")
    override fun deleteCustomer(@PathVariable id: Long): ResponseEntity<Unit> {
        this.customerService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping
    override fun updateCustomer(
        @RequestParam(value = "customerId") customerId: Long,
        @RequestBody customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(customerId)
        val customerToUpdate: Customer = customerUpdateDto.applyToEntity(customer)
        val customerUpdated: Customer = this.customerService.save(customerToUpdate)
        return ResponseEntity.status(HttpStatus.OK).body(customerUpdated.toViewDto())
    }

    private fun CustomerDto.toEntity(): Customer =
        Customer(
            firstName = this.firstName,
            lastName = this.lastName,
            cpf = this.cpf,
            income = this.income,
            email = this.email,
            password = this.password,
            address = Address(
                zipCode = this.zipCode,
                street = this.street
            )
        )

    private fun Customer.toViewDto(): CustomerView =
        CustomerView(
            firstName = this.firstName,
            lastName = this.lastName,
            cpf = this.cpf,
            income = this.income,
            email = this.email,
            zipCode = this.address.zipCode,
            street = this.address.street,
            id = this.id
        )

    private fun CustomerUpdateDto.applyToEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer
    }
}

