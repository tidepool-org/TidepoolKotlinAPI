package org.tidepool.sdk

import java.net.URL

interface AuthenticationServer {
    
    val url: URL
}

enum class AuthenticationServers(url: String) : AuthenticationServer {
    Development("https://auth.dev.tidepool.org"),
    QA("https://auth.qa2.tidepool.org"),
    External("https://auth.external.tidepool.org"),
    Production("https://auth.tidepool.org");
    
    override val url: URL = URL(url)
}