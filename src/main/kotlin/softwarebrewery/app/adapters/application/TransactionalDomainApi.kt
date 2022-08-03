package softwarebrewery.app.adapters.application

import softwarebrewery.app.domain.*
import org.springframework.transaction.annotation.*

@Transactional
class TransactionalDomainApi(domainApi: DomainApi) : DomainApi by domainApi
