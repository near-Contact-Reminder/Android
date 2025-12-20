package com.alarmy.near.messaging

import com.google.firebase.messaging.FirebaseMessagingService

class FcmService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}
