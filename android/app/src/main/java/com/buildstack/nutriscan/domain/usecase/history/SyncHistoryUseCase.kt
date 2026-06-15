package com.buildstack.nutriscan.domain.usecase.history

import com.buildstack.nutriscan.domain.repository.ScanRepository
import javax.inject.Inject

class SyncHistoryUseCase @Inject constructor(
    private val repository: ScanRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.syncHistory()
    }
}
