package org.tidepool.sdk.model.data

import org.tidepool.sdk.dto.data.DataSourceDto
import org.tidepool.sdk.dto.data.NewDataSourceDto

// DataSource mappers
internal fun DataSourceDto.toDomain(): DataSource = DataSource(
    createdTime = createdTime,
    dataSetIds = dataSetIds,
    earliestDataTime = earliestDataTime,
    id = id,
    lastImportTime = lastImportTime,
    latestDataTime = latestDataTime,
    modifiedTime = modifiedTime,
    providerName = providerName,
    providerSessionId = providerSessionId,
    providerType = providerType,
    state = state
)

internal fun DataSource.toDto(): DataSourceDto = DataSourceDto(
    createdTime = createdTime,
    dataSetIds = dataSetIds,
    earliestDataTime = earliestDataTime,
    id = id,
    lastImportTime = lastImportTime,
    latestDataTime = latestDataTime,
    modifiedTime = modifiedTime,
    providerName = providerName,
    providerSessionId = providerSessionId,
    providerType = providerType,
    state = state
)

// NewDataSource mappers
internal fun NewDataSourceDto.toDomain(): NewDataSource = NewDataSource(
    providerName = providerName,
    providerSessionId = providerSessionId,
    providerType = providerType
)

internal fun NewDataSource.toDto(): NewDataSourceDto = NewDataSourceDto(
    providerName = providerName,
    providerSessionId = providerSessionId,
    providerType = providerType
)