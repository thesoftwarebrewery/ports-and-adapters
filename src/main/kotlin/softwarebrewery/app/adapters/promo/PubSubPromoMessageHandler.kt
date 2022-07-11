package softwarebrewery.app.adapters.promo

import softwarebrewery.app.adapters.promo.MessageAttributes.Companion.CHANGE_TYPE
import softwarebrewery.app.adapters.promo.MessageAttributes.Companion.CHANGE_TYPE_CREATE
import softwarebrewery.app.adapters.promo.MessageAttributes.Companion.CHANGE_TYPE_DELETE
import softwarebrewery.app.domain.*
import softwarebrewery.infra.pubsub.*
import java.nio.*

@DrivingAdapter
class PubSubPromoMessageHandler(
    private val domainApi: DomainApi,
) : MessageHandler {

    override fun invoke(attributes: Map<String, String>, data: ByteBuffer) {
        val externalPromotionMessage = ExternalPromotionMessage.fromJsonBytes(data)

        when (val changeType = attributes[CHANGE_TYPE]) {
            CHANGE_TYPE_CREATE -> domainApi.handle(externalPromotionMessage.toDomainPromotionActivated())
            CHANGE_TYPE_DELETE -> domainApi.handle(externalPromotionMessage.toDomainPromotionDeactivated())
            else -> throw UnsupportedOperationException(changeType)
        }
    }
}
