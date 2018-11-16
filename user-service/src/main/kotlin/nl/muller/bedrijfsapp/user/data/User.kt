package nl.muller.bedrijfsapp.user.data

import  com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "User", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
data class User(
        @NotNull @Size(min = 4, max = 30) val email : String,
        @NotNull @Size(min = 4, max = 100) @JsonIgnore val password : String,
        @Size(max = 50) var firstName : String? = null,
        @Size(max = 50) var lastName : String? = null,
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) val id: Long = 0) {

    fun name() = "$firstName $lastName"

    override fun toString(): String {
        return javaClass.simpleName + ": " + email
    }
}