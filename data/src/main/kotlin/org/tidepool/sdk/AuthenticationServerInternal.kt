package org.tidepool.sdk

enum class AuthenticationServerInternal(val url: String){
    Development("https://auth.dev.tidepool.org"),
    QA("https://auth.qa2.tidepool.org"),
    External("https://auth.external.tidepool.org"),
    Production("https://auth.tidepool.org");
}