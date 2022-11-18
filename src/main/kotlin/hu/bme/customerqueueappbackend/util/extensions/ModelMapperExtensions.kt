package hu.bme.customerqueueappbackend.util.extensions

import org.modelmapper.ModelMapper
import org.modelmapper.TypeToken

inline fun <T, reified R> T.toDto(mapper: ModelMapper): R =
    mapper.map(this, R::class.java)

inline fun <T, reified R> List<T>.toDto(mapper: ModelMapper): List<R> {
    val listType = object : TypeToken<List<R>>() {}.type
    return mapper.map(this, listType)
}
