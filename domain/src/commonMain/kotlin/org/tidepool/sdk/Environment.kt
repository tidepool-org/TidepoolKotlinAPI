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
    Dev(
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
    ),
    Qa3(
        envCode = "qa3",
        auth = AuthenticationServers.QA,
    ),
    Qa4(
        envCode = "qa4",
        auth = AuthenticationServers.QA,
    ),
    Qa5(
        envCode = "qa5",
        auth = AuthenticationServers.QA,
    ),
    ;
}