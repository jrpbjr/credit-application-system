package br.com.jrpbjr.creditapplicationsystem.controller

import br.com.jrpbjr.creditapplicationsystem.dto.CreditDto
import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import br.com.jrpbjr.creditapplicationsystem.service.impl.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {

    @PostMapping
    fun saveCredit(@RequestBody creditDto: CreditDto):String {
        val credit: Credit = this.creditService.save(creditDto.toEntity())
        return ("Credit ${credit.creditCode} - Customer ${credit.customer?.email} saved!")
    }

}