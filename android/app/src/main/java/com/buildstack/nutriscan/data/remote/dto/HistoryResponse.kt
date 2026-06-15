package com.buildstack.nutriscan.data.remote.dto

data class HistoryResponse(
    val success: Boolean,
    val count: Int,
    val data: List<ScanResultDto>
)
