package hu.bme.customerqueueappbackend.dto

import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken

class ServiceTypeDto (
    val id: String = "",
    val name: String = "",
    val handleTime: Int = 0
)
