package com.buildstack.nutriscan.domain.usecase.history

import com.buildstack.nutriscan.domain.model.ScanResult
import com.buildstack.nutriscan.domain.repository.ScanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScanHistoryUseCase @Inject constructor(
    private val repository: ScanRepository
) {
    operator fun invoke(): Flow<List<ScanResult>> {
        return repository.getScanHistory()
    }
}
