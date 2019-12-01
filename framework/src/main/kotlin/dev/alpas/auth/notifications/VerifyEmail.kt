package dev.alpas.auth.notifications

import dev.alpas.Container
import dev.alpas.auth.AuthConfig
import dev.alpas.auth.Authenticatable
import dev.alpas.ozone.orAbort
import dev.alpas.mailing.MailMessage
import dev.alpas.make
import dev.alpas.notifications.Notification
import dev.alpas.notifications.channels.MailChannel
import dev.alpas.notifications.channels.NotificationChannel
import dev.alpas.routing.UrlGenerator
import kotlin.reflect.KClass

class VerifyEmail(private val container: Container) :
    Notification<Authenticatable> {
    override fun channels(): List<KClass<out NotificationChannel>> {
        return listOf(MailChannel::class)
    }

    override fun toMail(notifiable: Authenticatable): MailMessage {
        val expiration = container.make<AuthConfig>().emailVerificationExpiration
        val verificationUrl =
            container.make<UrlGenerator>().signedRoute("verification.verify", mapOf("id" to notifiable.id), expiration)
        return MailMessage()
            .toEmail(notifiable.email.orAbort())
            .subject("Verify Email")
            .view(
                "auth.emails.verify",
                mapOf(
                    "verificationUrl" to verificationUrl?.toExternalForm(),
                    "username" to notifiable.properties["name"]
                )
            )
    }
}
