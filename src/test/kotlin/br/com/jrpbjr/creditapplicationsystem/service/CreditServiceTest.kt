package br.com.jrpbjr.creditapplicationsystem.service


import br.com.jrpbjr.creditapplicationsystem.entity.Credit
import br.com.jrpbjr.creditapplicationsystem.entity.Customer
import br.com.jrpbjr.creditapplicationsystem.repository.CreditRepository
import br.com.jrpbjr.creditapplicationsystem.service.impl.CreditService
import br.com.jrpbjr.creditapplicationsystem.service.impl.CustomerService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should create credit`() {
        //given
        val credit: Credit = buildCredit()
        val customer: Customer = CustomerServiceTest.buildCustomer()

        every { customerService.findById(any()) } returns customer
        every { creditRepository.save(any()) } returns credit

        //when
        val actual: Credit = creditService.save(credit)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(credit)
        verify(exactly = 1) { customerService.findById(credit.customer?.id!!) }
        verify(exactly = 1) { creditRepository.save(credit) }
    }

    @Test
    fun `should find all credits by customer`() {
        //given
        val customerId: Long = 1L
        val expectedCredits: List<Credit> = listOf(buildCredit(), buildCredit())

        every { creditRepository.findAllByCustomerId(customerId) } returns expectedCredits

        //when
        val actual: List<Credit> = creditService.findAllByCustomer(customerId)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).hasSize(2)
        Assertions.assertThat(actual).containsExactlyElementsOf(expectedCredits)
        verify(exactly = 1) { creditRepository.findAllByCustomerId(customerId) }
    }

    @Test
    fun `should find credit by credit code`() {
        //given
        val customerId: Long = 1L
        val creditCode: UUID = UUID.randomUUID()
        val credit: Credit = buildCredit(customer = CustomerServiceTest.buildCustomer(id = customerId))

        every { creditRepository.findByCreditCode(creditCode) } returns credit

        //when
        val actual: Credit = creditService.findByCreditCode(customerId, creditCode)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(credit)
        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }

    @Test
    fun `should throw RuntimeException when credit code not found`() {
        //given
        val customerId: Long = 1L
        val invalidCreditCode: UUID = UUID.randomUUID()

        every { creditRepository.findByCreditCode(invalidCreditCode) } returns null

        //when/then
        Assertions.assertThatExceptionOfType(RuntimeException::class.java)
            .isThrownBy { creditService.findByCreditCode(customerId, invalidCreditCode) }
            .withMessage("Credit code $invalidCreditCode not found")
        verify(exactly = 1) { creditRepository.findByCreditCode(invalidCreditCode) }
    }

    @Test
    fun `should throw RuntimeException when customerId doesn't match credit customer`() {
        //given
        val customerId: Long = 1L
        val creditCode: UUID = UUID.randomUUID()
        val credit: Credit = buildCredit(customer = CustomerServiceTest.buildCustomer(id = 2L))

        every { creditRepository.findByCreditCode(creditCode) } returns credit

        //when/then
        Assertions.assertThatExceptionOfType(RuntimeException::class.java)
            .isThrownBy { creditService.findByCreditCode(customerId, creditCode) }
            .withMessage("Contact admin")
        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }

    companion object {
        fun buildCredit(
            creditValue: BigDecimal = BigDecimal("700.00"),
            dayFirstInstallment: LocalDate = LocalDate.parse("2025-05-05"),
            numberOfInstallments: Int = 10,
            customer: Customer = CustomerServiceTest.buildCustomer()
        ): Credit = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customer = customer
        )
    }
}
