package app.repository

import app.BaseIT
import app.service.TrxFactoryService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class TrxRepositoryTest : BaseIT() {
    @Autowired
    private lateinit var factory: TrxFactoryService

    @Autowired
    private lateinit var repository: TrxRepository

    @Test
    fun saveReadTest() {
        val initialTrx = factory.create()

        val savedTrx = repository.save(initialTrx).block()

        assertNotNull(savedTrx)

        val readTrx = repository.findById(savedTrx.id).block()

        assertEquals(initialTrx.copy(id = savedTrx.id), readTrx)
    }
}
