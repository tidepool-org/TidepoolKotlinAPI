package org.tidepool.sdk.model.auth;

import kotlinx.serialization.SerialName

enum class Realm {
    @SerialName("dev1")
    Dev1,
    
    @SerialName("qa1")
    Qa1,
    
    @SerialName("qa2")
    Qa2,
    
    @SerialName("qa3")
    Qa3,
    
    @SerialName("qa4")
    Qa4,
    
    @SerialName("qa5")
    Qa5,
    
    @SerialName("integration")
    Integration,
    
    @SerialName("tidepool")
    Tidepool
}