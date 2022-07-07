package softwarebrewery.demo.infra.pubsub

import java.nio.*

interface MessageHandler {
    fun handle(attributes: Map<String, String>, data: ByteBuffer)
}
