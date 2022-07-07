package softwarebrewery.demo.adapters.promo

import softwarebrewery.demo.adapters.promo.PromotionMessage.Companion.CHANGE_TYPE_ATTRIBUTE
import softwarebrewery.demo.adapters.promo.PromotionMessage.Companion.CHANGE_TYPE_CREATE
import softwarebrewery.demo.adapters.promo.PromotionMessage.Companion.CHANGE_TYPE_DELETE
import softwarebrewery.demo.domain.api.*
import softwarebrewery.demo.infra.pubsub.*
import java.nio.*

class PubSubPromotionMessageHandler(
    private val domainApi: DomainApi,
) : MessageHandler {

    override fun invoke(attributes: Map<String, String>, data: ByteBuffer) {
        val promotionMessage = PromotionMessage.fromJsonBytes(data)

        when (val changeType = attributes[CHANGE_TYPE_ATTRIBUTE]) {
            CHANGE_TYPE_CREATE -> domainApi.handle(promotionMessage.toDomainPromotionActivated())
            CHANGE_TYPE_DELETE -> domainApi.handle(promotionMessage.toDomainPromotionDeactivated())
            else -> throw UnsupportedOperationException(changeType)
        }
    }
}
