package cbr.authservice.service

import cbr.authservice.repository.VerificationTokenRepo
import cbr.entity.User
import cbr.entity.VerificationToken
import cbr.exception.CustomException
import cbr.exception.ErrorResponse
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import java.util.stream.Collectors

@Slf4j
@Service
 class VerificationTokenService(
    private val repository: VerificationTokenRepo,
    private val authService: AuthService,
    @Value("\${emailVerification.expiration}") private val expiration: String) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)


    fun createVerificationToken(user:User,value:String ){
        val token = VerificationToken()
        token.value = value
        token.user = user
        val cal = Calendar.getInstance()
        cal.time = Timestamp(cal.time.time)
        cal.add(Calendar.MINUTE, expiration.toInt())
        token.expiryDate = Date(cal.time.time)
        log.info("Verification token has been sent and created for ${user.email}")

        repository.save(token)

    }
    fun verifyToken(value:String): Boolean {
        val token = repository.findByValue(value)
        val cal = Calendar.getInstance()
        if (token!=null && ( token.expiryDate.time - cal.time.time) >0){
            val user =  token.user
            user.isVerified = true
            authService.updateUser(user)
            repository.delete(token)
            log.info("${user.username} has been verified")
            return true
        }
        val ex = "Verification token is not found or expired"
        throw CustomException(ex, HttpStatus.BAD_REQUEST, ErrorResponse(ex))
    }
    @Scheduled(fixedDelay = 1000*60*24)
    //delete expired tokens and their users if not verified
     fun removeAllExpired(){
        val expiredTokens =  repository.getAllExpired()
        val notVerifiedUsers = expiredTokens.stream()
            .filter { el -> !el.user.isVerified }
            .map(VerificationToken::getUser)
            .collect(Collectors.toList())
    // tokens will be deleted on cascade
        authService.deleteNotVerifiedUsers(notVerifiedUsers)
        repository.deleteAll(expiredTokens)
    }
}