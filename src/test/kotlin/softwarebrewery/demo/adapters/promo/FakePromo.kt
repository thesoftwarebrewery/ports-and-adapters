//package softwarebrewery.demo.adapters.promo
//
//import com.google.cloud.spring.pubsub.core.*
//import org.springframework.beans.factory.annotation.*
//import org.springframework.stereotype.Component
//import softwarebrewery.demo.technical.*
//
//@Component
//class FakePromo(
//    @Value("\${pubsub.promo.promotion-events.topic}") val promotionEventsTopic: String,
//    @Value("\${pubsub.promo.promotion-events.subscription.}") val promotionEventsSubscription: String,
//    private val pubSub: PubSubOperations,
//) {
//    fun publishes(message: PromotionMessage) {
//        throw UnsupportedOperationException()
//    }
//}
