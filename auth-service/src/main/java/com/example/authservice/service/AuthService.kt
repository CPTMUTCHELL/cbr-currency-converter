package com.example.authservice.service

import com.example.authservice.dto.UserRegistrationDto
import com.example.authservice.dto.UserRoleDto
import com.example.authservice.mapper.UserRoleDtoMapper
import com.example.authservice.repository.RoleRepo
import com.example.authservice.repository.UserRepo
import com.example.authservice.service.event.RegistrationEvent
import com.example.entity.Role
import com.example.entity.User
import com.example.exception.CustomException
import com.example.exception.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service

class AuthService @Autowired @Lazy constructor(
    private val userRepository: UserRepo,
    private val encoder: BCryptPasswordEncoder,
    private val roleRepo: RoleRepo,
    private val mapper: UserRoleDtoMapper,
    private val eventPublisher: ApplicationEventPublisher
) : UserDetailsService {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Throws(CustomException::class)
    fun getUser(username: String): User {
        val ex = "User not found"
        val user = userRepository.findByUsernameOrEmail(username)
        return user ?: throw CustomException(ex, HttpStatus.NOT_FOUND, ErrorResponse(ex))
    }


    fun getRoles(): List<Role> = roleRepo.findAll()

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(s: String): UserDetails {
        val user = getUser(s)
        val verified = user.isVerified
        if (!verified) {
            val ex = "User's email is not verified"
            throw CustomException(ex, HttpStatus.UNAUTHORIZED, ErrorResponse(ex))
        }

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            mapRolesToAuth(user.roles)
        )
    }


    @Throws(CustomException::class)
    fun save(user: UserRegistrationDto): User {
        if (userRepository.findByUsername(user.username) != null) {
            val ex = "User with this username already exists!"
            throw CustomException(ex, HttpStatus.CONFLICT, ErrorResponse(ex))
        } else if (userRepository.findByEmail(user.email) != null) {
            val ex = "User with this email already exists!"
            throw CustomException(ex, HttpStatus.CONFLICT, ErrorResponse(ex))
        } else {
            val userToSave = User()
            userToSave.username = user.username
            val roles = ArrayList<Role>()
            roles.add(roleRepo.findById(3).get()) //USER
            userToSave.roles = roles
            userToSave.password = encoder.encode(user.password)
            userToSave.email = user.email
            userRepository.save(userToSave)
            eventPublisher.publishEvent(RegistrationEvent(userToSave))
            log.info("${user.username} has been created")

            return userToSave
        }
    }

    fun updateUser(user: User) {
        userRepository.save(user)
    }

    private fun mapRolesToAuth(roles: Collection<Role>): List<GrantedAuthority> {
        return roles.stream()
            .map { role -> SimpleGrantedAuthority("${role.id}-${role.name}") }
            .collect(Collectors.toList())
    }

    fun findAll(): List<User> = userRepository.findAll()


    @Throws(CustomException::class)
    fun deleteUserById(id: Int) {
        val userToDelete: Optional<User> = userRepository.findById(id)
        if (userToDelete.isPresent) {
            val authentication = SecurityContextHolder.getContext().authentication
            val minRoleIdOfUser = getMinRoleIdOfUser(getUser(authentication.name))
            val minRoleIdOfUserToDelete = getMinRoleIdOfUser(userToDelete.get())
            if (minRoleIdOfUser < minRoleIdOfUserToDelete)
                userRepository.delete(userToDelete.get())
            else {
                val msg = "insufficient rights to remove ${userToDelete.get().username}"
                throw CustomException(msg, HttpStatus.UNAUTHORIZED, ErrorResponse(msg))
            }
        }
    }

    @Throws(CustomException::class)
    fun updateRole(userDto: UserRoleDto): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val minRoleIdOfUser = getMinRoleIdOfUser(getUser(authentication.name))
        val userToChange = getUser(userDto.username)

        val minRoleIdOfUserToChange = getMinRoleIdOfUser(userToChange)
        return if (minRoleIdOfUser < minRoleIdOfUserToChange) {
            mapper.updateRoles(userDto, userToChange)
            userRepository.save(userToChange)
        } else {
            val msg = "insufficient rights to change ${userDto.username}"
            throw CustomException(msg, HttpStatus.UNAUTHORIZED, ErrorResponse(msg))
        }
    }

    private fun getMinRoleIdOfUser(user: User): Int {
        return user.roles.stream().map(Role::getId).min(Int::compareTo).get()
    }

    fun deleteNotVerifiedUsers(users: List<User>) {
        userRepository.deleteAll(users)
    }
}