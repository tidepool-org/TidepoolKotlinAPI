package org.tidepool.sdk.repository

import io.ktor.client.HttpClient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.tidepool.sdk.api.BlobApi
import org.tidepool.sdk.dto.blob.DeviceLogContentDto
import org.tidepool.sdk.dto.blob.toDomain
import org.tidepool.sdk.dto.blob.toDto
import org.tidepool.sdk.di.provideBlobApi
import org.tidepool.sdk.mapList
import org.tidepool.sdk.model.blob.BlobStatus
import org.tidepool.sdk.model.blob.DeviceLogContent
import org.tidepool.sdk.runCatchingNetworkExceptions
import org.tidepool.sdk.util.md5Base64
import org.tidepool.sdk.util.toTidepoolTimeString
import kotlin.time.Instant

class BlobRepositoryImpl(
    private val environmentRepository: EnvironmentRepository,
    private val httpClient: HttpClient,
    private val json: Json,
) : BlobRepository {

    private val blobApi: BlobApi
        get() = provideBlobApi(environmentRepository.getKtorfit(httpClient))

    override suspend fun listBlobs(
        userId: String,
        mediaType: String?,
        status: BlobStatus?,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        blobApi.listBlobs(
            sessionToken = sessionToken,
            userId = userId,
            mediaType = mediaType,
            status = status?.toDto(),
        )
    }.mapList { it.toDomain() }
    
//    override suspend fun createBlob(
//        userId: String,
//        contentType: String,
//        digest: String,
//        content: ByteArray,
//        sessionToken: String
//    ) = runCatchingNetworkExceptions {
//        val requestBody = content.toRequestBody(contentType.toMediaType())
//        blobApi.createBlob(
//            sessionToken = sessionToken,
//            userId = userId,
//            contentType = contentType,
//            digest = digest,
//            content = requestBody,
//        )
//    }.map { it.toDomain() }
    
    override suspend fun deleteAllBlobs(
        userId: String,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        blobApi.deleteAllBlobs(sessionToken = sessionToken, userId = userId)
    }
    
    override suspend fun getBlobMetadata(
        blobId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        blobApi.getBlobMetadata(sessionToken = sessionToken, blobId = blobId)
    }.map { it.toDomain() }
    
    override suspend fun deleteBlob(
        blobId: String,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        blobApi.deleteBlob(sessionToken = sessionToken, blobId = blobId)
    }
    
//    override suspend fun getBlobContent(
//        blobId: String,
//        sessionToken: String,
//    ) = runCatchingNetworkExceptions {
//        val responseBody = blobApi.getBlobContent(
//            sessionToken = sessionToken,
//            blobId = blobId,
//        )
//        responseBody.bytes()
//    }
    
    override suspend fun uploadDeviceLogs(
        userId: String,
        startAtTime: Instant,
        endAtTime: Instant,
        logs: List<DeviceLogContent>,
        sessionToken: String,
    ) = runCatchingNetworkExceptions {
        // Encode once: the Digest header is the MD5 of these exact bytes, and they are what is sent.
        val body = json.encodeToString(
            ListSerializer(DeviceLogContentDto.serializer()),
            logs.map { it.toDto() },
        ).encodeToByteArray()
        blobApi.uploadDeviceLogs(
            sessionToken = sessionToken,
            userId = userId,
            digest = "MD5=${md5Base64(body)}",
            startAtTime = startAtTime.toTidepoolTimeString(),
            endAtTime = endAtTime.toTidepoolTimeString(),
            body = body,
        )
    }.map { it.toDomain() }
    
    override suspend fun listDeviceLogs(
        userId: String,
        startAtTime: Instant,
        endAtTime: Instant,
        sessionToken: String
    ) = runCatchingNetworkExceptions {
        blobApi.listDeviceLogs(
            sessionToken = sessionToken,
            userId = userId,
            startAtTime = startAtTime.toTidepoolTimeString(),
            endAtTime = endAtTime.toTidepoolTimeString()
        )
    }.mapList { it.toDomain() }
}