package org.tidepool.sdk

import java.net.URL

interface Environment {
	val url: URL
	val auth: AuthenticationServer
}

public enum class Environments(url: String, override val auth: AuthenticationServer) : Environment {
	Production("https://api.tidepool.org", AuthenticationServers.Production),
	Integration("https://external.integration.tidepool.org", AuthenticationServers.External),
	Dev1("https://dev1.dev.tidepool.org", AuthenticationServers.Development),
	Qa1("https://qa1.development.tidepool.org", AuthenticationServers.QA),
	Qa2("https://qa2.development.tidepool.org", AuthenticationServers.QA);

	override val url: URL = URL(url)
}