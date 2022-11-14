package hu.bme.customerqueueappbackend.config

import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration.AccessLevel.PRIVATE
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeansConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper().apply {
            configuration.apply {
                isFieldMatchingEnabled = true
                fieldAccessLevel = PRIVATE
            }
        }
    }
}
