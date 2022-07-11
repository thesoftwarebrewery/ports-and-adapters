package softwarebrewery.app.adapters.application

import org.springframework.transaction.annotation.*
import softwarebrewery.app.domain.*

@Transactional
class TransactionalDomainApi(domainApi: DomainApi) : DomainApi by domainApi
