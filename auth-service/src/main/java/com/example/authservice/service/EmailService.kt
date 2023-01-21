package com.example.authservice.service

import com.example.authservice.service.event.RegistrationEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*


@Service
class EmailService(
    private val mailSender:JavaMailSender,
    private val verificationTokenService: VerificationTokenService,
    @Value("\${spring.mail.username}") private val fromEmail:String,
    @Value("\${emailVerification.link}") private val link:String,
    @Value("\${emailVerification.expiration}") private val expiration:String,) {


    @EventListener(RegistrationEvent::class)
    fun sendVerificationEmail(event:RegistrationEvent){
        val user = event.user
        val token = UUID.randomUUID().toString()
        val msg = SimpleMailMessage()
        msg.from = fromEmail
        msg.setTo(user.email)
        msg.subject = "Cbr. Email verification"
        msg.text="Hello. In order to use cbr converter you have to verify your email.\n" +
                "The link will expire in $expiration minutes\n" +
                "Verification link: $link$token"

        try {
            //if smtp didn't send msg, don't create token
            mailSender.send(msg)
            verificationTokenService.createVerificationToken(user, token)

        }catch (e:RuntimeException){
            throw RuntimeException(e.message)
        }

    }





}