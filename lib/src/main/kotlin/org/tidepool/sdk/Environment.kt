package org.tidepool.sdk

interface Environment {
    
    val envCode: String
        get() = toString()
    val auth: AuthenticationServer
}

enum class Environments(
    override val envCode: String,
    override val auth: AuthenticationServer
): Environment {
    Production(
        envCode = "tidepool",
        auth = AuthenticationServers.Production,
    ),
    Integration(
        envCode = "integration",
        auth = AuthenticationServers.External
    ),
    Dev1(
        envCode = "dev",
        auth = AuthenticationServers.Development,
    ),
    Qa1(
        envCode = "qa1",
        auth = AuthenticationServers.QA,
    ),
    Qa2(
        envCode = "qa2",
        auth = AuthenticationServers.QA,
    );
}

internal fun Environment.toInternal() = when (this) {
    Environments.Production  -> EnvironmentInternal.Production
    Environments.Integration -> EnvironmentInternal.Integration
    Environments.Dev1        -> EnvironmentInternal.Dev1
    Environments.Qa1         -> EnvironmentInternal.Qa1
    Environments.Qa2         -> EnvironmentInternal.Qa2
    
    else                     -> EnvironmentInternal.Qa1 // TODO
}
