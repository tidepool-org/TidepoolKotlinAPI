package org.tidepool.sdk.repository

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient

internal fun EnvironmentRepository.getKtorfit(httpClient: HttpClient): Ktorfit = Ktorfit.Builder()
    .httpClient(httpClient)
    .baseUrl(
        url = checkNotNull(
            value = getEnvironment()
                ?.url
                ?.let {
                    if (it.endsWith("/")) {
                        "https://$it"
                    } else {
                        "https://$it/"
                    }
                },
            lazyMessage = { "No environment set" },
        )
    )
    .build()