package org.tidepool.sdk

enum class EnvironmentInternal(
    val url: String,
    val envCode: String,
    val auth: AuthenticationServerInternal
) {
    
    Production("https://api.tidepool.org", "tidepool", AuthenticationServerInternal.Production),
    Integration(
        "https://external.integration.tidepool.org",
        "integration",
        AuthenticationServerInternal.External
    ),
    Dev1("https://dev1.dev.tidepool.org", "dev", AuthenticationServerInternal.Development),
    Qa1("https://qa1.development.tidepool.org", "qa1", AuthenticationServerInternal.QA),
    Qa2("https://qa2.development.tidepool.org", "qa2", AuthenticationServerInternal.QA);
}

internal fun Environment.toInternal() = when (this) {
    Environments.Production  -> EnvironmentInternal.Production
    Environments.Integration -> EnvironmentInternal.Integration
    Environments.Dev         -> EnvironmentInternal.Dev1
    Environments.Qa1         -> EnvironmentInternal.Qa1
    Environments.Qa2         -> EnvironmentInternal.Qa2
    
    else                     -> EnvironmentInternal.Qa1 // TODO
}
