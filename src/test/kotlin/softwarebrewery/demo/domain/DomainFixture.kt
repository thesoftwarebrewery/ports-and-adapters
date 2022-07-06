package softwarebrewery.demo.domain

class DomainFixture {

    private val domainHandler = SimpleDomainHandler(
        offerRepository = InMemOfferRepository(),
        domainEventListener = InMemDomainEventListener(),
    )

    fun handle(vararg messages: Any) = messages.forEach { dispatch(it, domainHandler) }

    private fun dispatch(message: Any, receiver: Any) {
        val receiverType = receiver.javaClass
        val handlerMethod = receiverType.methods.singleOrNull {
            it.name.equals("handle")
                    && it.parameterCount == 1 && it.parameterTypes[0].isInstance(message)
        } ?: throw UnsupportedOperationException("'${receiverType.name}' has no 'handle(${message.javaClass.name})'")

        handlerMethod.invoke(receiver, message)
    }

    fun assertNotifiedOf(vararg events: Any) {
        TODO("Not yet implemented")
    }
}
