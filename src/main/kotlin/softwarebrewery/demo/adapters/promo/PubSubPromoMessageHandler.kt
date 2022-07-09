package softwarebrewery.demo.adapters.promo

import softwarebrewery.demo.adapters.promo.MessageAttributes.Companion.CHANGE_TYPE
import softwarebrewery.demo.adapters.promo.MessageAttributes.Companion.CHANGE_TYPE_CREATE
import softwarebrewery.demo.adapters.promo.MessageAttributes.Companion.CHANGE_TYPE_DELETE
import softwarebrewery.demo.domain.*
import softwarebrewery.demo.infra.pubsub.*
import java.nio.*

@DrivingAdapter
class PubSubPromoMessageHandler(
    private val domainApi: DomainApi,
) : MessageHandler {

    override fun invoke(attributes: Map<String, String>, data: ByteBuffer) {
        val promotionMessage = PromotionMessage.fromJsonBytes(data)

        when (val changeType = attributes[CHANGE_TYPE]) {
            CHANGE_TYPE_CREATE -> domainApi.handle(promotionMessage.toDomainPromotionActivated())
            CHANGE_TYPE_DELETE -> domainApi.handle(promotionMessage.toDomainPromotionDeactivated())
            else -> throw UnsupportedOperationException(changeType)
        }
    }
}
