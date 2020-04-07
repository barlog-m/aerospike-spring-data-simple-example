package app.repository

import app.model.Trx
import org.springframework.data.aerospike.repository.ReactiveAerospikeRepository
import java.util.UUID

interface TrxRepository : ReactiveAerospikeRepository<Trx, UUID>
