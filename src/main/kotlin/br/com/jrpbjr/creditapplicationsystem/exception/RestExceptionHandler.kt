package br.com.jrpbjr.creditapplicationsystem.exception

import br.com.jrpbjr.creditapplicationsystem.generated.application.web.dto.Error
import jakarta.servlet.http.HttpServletRequest
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<Error> {
        val details: MutableMap<String, String> = LinkedHashMap()
        ex.bindingResult.fieldErrors.forEach { fe: FieldError ->
            details[fe.field] = fe.defaultMessage ?: "Invalid value"
        }
        val body = Error(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = "Dados inválidos",
            path = request.requestURI,
            details = details
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccess(
        ex: DataAccessException,
        request: HttpServletRequest
    ): ResponseEntity<Error> {
        val body = Error(LocalDateTime.now(), HttpStatus.CONFLICT.value(), "Conflict", ex.mostSpecificCause.message ?: ex.message ?: "Conflict! Consult the documentation", request.requestURI, emptyMap())
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusiness(
        ex: BusinessException,
        request: HttpServletRequest
    ): ResponseEntity<Error> {
        val body = Error(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Erro de negócio",
            path = request.requestURI,
            // evite duplicar a mensagem; use um código/razão se quiser
            details = emptyMap()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<Error> {
        val body = Error(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Parâmetros inválidos",
            path = request.requestURI,
            details = emptyMap()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<Error> {
        val body = Error(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = ex.message ?: "Erro inesperado",
            path = request.requestURI,
            details = mapOf("exception" to ex.javaClass.simpleName)
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body)
    }
}