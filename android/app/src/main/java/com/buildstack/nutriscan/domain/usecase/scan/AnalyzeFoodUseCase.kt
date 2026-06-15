package com.buildstack.nutriscan.domain.usecase.scan

import com.buildstack.nutriscan.domain.model.ScanResult
import com.buildstack.nutriscan.domain.repository.ScanRepository
import java.io.File
import javax.inject.Inject

class AnalyzeFoodUseCase @Inject constructor(
    private val repository: ScanRepository
) {
    suspend operator fun invoke(imageFile: File): Result<ScanResult> {
        return repository.analyzeFood(imageFile)
    }
}
