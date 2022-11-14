package hu.bme.customerqueueappbackend.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class TestDataInitializer(
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
    }
}
