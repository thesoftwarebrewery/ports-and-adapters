package softwarebrewery.demo.adapters.promo

import softwarebrewery.demo.infra.pubsub.*
import java.nio.*

class PubSubPromotionEventListener : MessageHandler {

    override fun handle(attributes: Map<String, String>, data: ByteBuffer) {
        TODO("Not yet implemented")
    }
}
