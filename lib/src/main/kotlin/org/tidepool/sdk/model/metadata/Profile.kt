package org.tidepool.sdk.model.metadata

import kotlinx.serialization.Serializable

@Serializable
open class Profile(val fullName: String? = null)