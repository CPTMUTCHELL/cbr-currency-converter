package cbr.authservice.repository

import cbr.entity.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepo:JpaRepository<VerificationToken,Int> {
    fun findByValue(value:String):VerificationToken?
    @Query("select t  from VerificationToken t where t.expiryDate < CURRENT_TIMESTAMP ")
    fun getAllExpired():List<VerificationToken>

}