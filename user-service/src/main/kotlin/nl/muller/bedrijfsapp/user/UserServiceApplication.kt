package nl.muller.bedrijfsapp.user

import nl.muller.bedrijfsapp.user.data.User
import nl.muller.bedrijfsapp.user.data.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
class UserServiceApplication {
    @Bean
    fun databaseInitializer(userRepository: UserRepository) = CommandLineRunner {
        userRepository.save(createUser("admin@admin.com", "admin", "admin", "admin"))
        userRepository.save(createUser("user@user.com", "user", "user", "user"))
    }

    private fun createUser(email: String, password: String, firstName: String, lastName: String) : User {
        return User(email, password, firstName, lastName)
    }
}

fun main(args: Array<String>) {
    runApplication<UserServiceApplication>(*args) {    }
}

