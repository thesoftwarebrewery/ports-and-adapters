package softwarebrewery.app.adapters.application

import org.springframework.jdbc.core.namedparam.*

enum class LinkTrigger { OFFER, PROMO }

class JdbcLinkRequestRepo(
    private val db: NamedParameterJdbcTemplate,
) {

    fun insert(trigger: LinkTrigger, key: String) {
        val query = "insert into link_requests (trigger, key) values (:trigger, :key)"
        val params = mapOf("trigger" to trigger.name, "key" to key)
        db.update(query, params)
    }

    fun remove(trigger: LinkTrigger, key: String) {
        val query = "delete from link_requests where trigger = :trigger and key = :key"
        val params = mapOf("trigger" to trigger.name, "key" to key)
        db.update(query, params)
    }
}
