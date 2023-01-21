package cbr.authservice.repository

import cbr.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepo: JpaRepository<User,Int> {
    @Query("select u from User  u where u.email = ?1 or u.username = ?1")
    fun findByUsernameOrEmail(username: String): User?
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?

}