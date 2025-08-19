package br.com.jrpbjr.creditapplicationsystem.controller

import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.api.CreditsApi
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.CreditDto
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.CreditView
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.CreditViewList
import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.Status as GeneratedStatus
import br.com.jrpbjr.creditapplicationsystem.service.impl.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) : CreditsApi {

    @PostMapping
    override fun saveCredit(@RequestBody creditDto: CreditDto): ResponseEntity<String> {
        val credit: Credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit ${credit.creditCode} - Customer ${credit.customer?.email} saved!")
    }

    @GetMapping
    override fun findAllCreditsByCustomerId(
        @RequestParam(value = "customerId") customerId: Long
    ): ResponseEntity<List<CreditViewList>> {
        val creditViewList: List<CreditViewList> = this.creditService.findAllByCustomer(customerId)
            .stream()
            .map { credit: Credit -> credit.toViewListDto() }
            .collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
    }

    @GetMapping("/{creditCode}")
    override fun findCreditByCode(
        @PathVariable creditCode: UUID,
        @RequestParam(value = "customerId") customerId: Long
    ): ResponseEntity<CreditView> {
        val credit: Credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.status(HttpStatus.OK).body(credit.toViewDto())
    }

    private fun CreditDto.toEntity(): Credit =
        Credit(
            creditValue = this.creditValue,
            dayFirstInstallment = this.dayFirstInstallment,
            numberOfInstallments = this.numberOfInstallments,
            customer = Customer(id = this.customerId)
        )

    private fun Credit.toViewListDto(): CreditViewList =
        CreditViewList(
            creditCode = this.creditCode,
            creditValue = this.creditValue,
            numberOfInstallments = this.numberOfInstallments
        )

    private fun Credit.toViewDto(): CreditView =
        CreditView(
            creditCode = this.creditCode,
            creditValue = this.creditValue,
            numberOfInstallments = this.numberOfInstallments,
            status = GeneratedStatus.valueOf(this.status.name),
            emailCustomer = this.customer?.email,
            incomeCustomer = this.customer?.income
        )
}