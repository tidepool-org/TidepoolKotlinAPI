package org.tidepool.sdk.model.summary

import org.tidepool.sdk.dto.summary.SummaryTypeDto

enum class SummaryType {
    Cgm,
    Bgm,
    Continuous
}

internal fun SummaryType.toDto(): SummaryTypeDto = when (this) {
    SummaryType.Cgm -> SummaryTypeDto.Cgm
    SummaryType.Bgm -> SummaryTypeDto.Bgm
    SummaryType.Continuous -> SummaryTypeDto.Con
}

internal fun SummaryTypeDto.toDomain(): SummaryType = when (this) {
    SummaryTypeDto.Cgm -> SummaryType.Cgm
    SummaryTypeDto.Bgm -> SummaryType.Bgm
    SummaryTypeDto.Con -> SummaryType.Continuous
}