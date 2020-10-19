package com.happy.alarms

import org.joda.time.DateTime
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailAlarm(
    val from: String,
    val properties: Properties,
    val username: String,
    val password: String,
    val personal: String = from
) : Alarm {

    private val session: Session = Session.getDefaultInstance(properties, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    })


    companion object {
        fun buildQQDefaultEmailSender(): EmailAlarm {
            val properties = System.getProperties()
            properties["mail.smtp.host"] = "smtp.qq.com"
            properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory";
            properties["mail.smtp.socketFactory.fallback"] = "false";
            properties["mail.smtp.port"] = "465";
            properties["mail.smtp.socketFactory.port"] = "465";
            properties["mail.smtp.auth"] = "true";
            properties["mail.smtp.starttls.enable"] = "true";
            properties["mail.smtp.starttls.required"] = "true";
            return EmailAlarm(
                "1678167835@qq.com",
                properties,
                "1678167835@qq.com",
                "vlpibvezgxgvceec",
                "你爹"
            )
        }
    }

    override fun send(title: String, content: String, tos: List<String>) {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(from, personal))
        tos.forEach {
            message.addRecipient(Message.RecipientType.TO, InternetAddress(it))
        }
        message.subject = title
        message.setText(content)
        Transport.send(message)

        println(" 发送邮件 $content " + DateTime.now().toString("HH:mm:ss"))
    }

}
