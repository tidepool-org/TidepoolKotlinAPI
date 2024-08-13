package org.tidepool.sdk

import java.net.URL

interface Environment {
    
    val url: URL
    val envCode: String
        get() {
            return toString()
        }
    val auth: AuthenticationServer
}

public enum class Environments(
    url: String,
    override val envCode: String,
    override val auth: AuthenticationServer
) : Environment {
    
    Production("https://api.tidepool.org", "tidepool", AuthenticationServers.Production),
    Integration(
        "https://external.integration.tidepool.org",
        "integration",
        AuthenticationServers.External
    ),
    Dev1("https://dev1.dev.tidepool.org", "dev", AuthenticationServers.Development),
    Qa1("https://qa1.development.tidepool.org", "qa1", AuthenticationServers.QA),
    Qa2("https://qa2.development.tidepool.org", "qa2", AuthenticationServers.QA);
    
    override val url: URL = URL(url)
}