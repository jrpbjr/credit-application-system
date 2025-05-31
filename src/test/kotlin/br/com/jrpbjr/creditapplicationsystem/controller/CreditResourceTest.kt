package br.com.jrpbjr.creditapplicationsystem.controller

import br.com.jrpbjr.creditapplicationsystem.dto.request.CreditDto
import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import br.com.jrpbjr.creditapplicationsystem.repository.CreditRepository
import br.com.jrpbjr.creditapplicationsystem.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun `should create credit and return 201 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomer())
        val creditDto: CreditDto = builderCreditDto(customerId = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isString)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find all credits by customer id and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomer())
        val credit1: Credit = creditRepository.save(builderCredit(customer = customer))
        val credit2: Credit = creditRepository.save(builderCredit(customer = customer))

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(1000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(12))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").isString)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by credit code and customer id and return 200 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomer())
        val credit: Credit = creditRepository.save(builderCredit(customer = customer))

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").value(credit.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(1000.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(12))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("jrpbjr@email.com"))
            .andDo(MockMvcResultHandlers.print())
    }


    private fun builderCustomer(
        firstName: String = "Jose",
        lastName: String = "Roberto",
        cpf: String = "28475934625",
        email: String = "jrpbjr@email.com",
        password: String = "1234",
        zipCode: String = "000000",
        street: String = "Rua do jota, 123",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = br.com.jrpbjr.creditapplicationsystem.entity.Address(
            zipCode = zipCode,
            street = street
        ),
        income = income
    )

    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(1000.0),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(30),
        numberOfInstallments: Int = 12,
        customerId: Long
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )

    private fun builderCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(1000.0),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(30),
        numberOfInstallments: Int = 12,
        customer: Customer
    ) = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )
}
