package com.buildstack.nutriscan.domain.usecase.scan

import com.buildstack.nutriscan.domain.model.ScanResult
import com.buildstack.nutriscan.domain.repository.ScanRepository
import javax.inject.Inject

class GetScanByIdUseCase @Inject constructor(
    private val repository: ScanRepository
) {
    suspend operator fun invoke(id: String): Result<ScanResult> {
        return repository.getScanById(id)
    }
}
