package softwarebrewery.demo.infra.jdbc

import mu.*
import org.springframework.beans.factory.annotation.*
import org.springframework.jdbc.core.*
import org.springframework.jdbc.core.namedparam.*
import org.springframework.test.context.*

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@TestExecutionListeners(
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
    listeners = [ResetDatabaseTestListener::class],
)
annotation class ResetDbForEachTest

class ResetDatabaseTestListener : TestExecutionListener {

    private val log = KotlinLogging.logger { }

    @Suppress("SpringJavaAutowiredMembersInspection")
    @Autowired
    private lateinit var db: NamedParameterJdbcTemplate

    override fun beforeTestClass(testContext: TestContext) =
        testContext.applicationContext.autowireCapableBeanFactory.autowireBean(this)

    override fun beforeTestMethod(testContext: TestContext) = resetDatabase()

    override fun afterTestClass(testContext: TestContext) = resetDatabase()

    private fun resetDatabase() {
        emptyTables(db, schema = "public", ignoreTables = setOf("flyway_schema_history"))
        restartSequences(db, schema = "public")
    }

    private fun emptyTables(db: NamedParameterJdbcTemplate, schema: String, ignoreTables: Set<String>) {
        val tablesToTruncate = db.query(
            "select table_name from information_schema.tables where table_schema = '$schema'",
            SingleColumnRowMapper(String::class.java)
        ).filter { tableName -> ignoreTables.none { it.equals(tableName, ignoreCase = true) } }

        tablesToTruncate.forEach { table ->
            log.info { "Truncating table '$schema.$table'" }
            db.update("truncate table $schema.$table", emptyMap<String, String>())
        }
    }

    private fun restartSequences(db: NamedParameterJdbcTemplate, schema: String) {
        db.query(
            "select sequence_name from information_schema.sequences where sequence_schema = '$schema'",
            SingleColumnRowMapper(String::class.java)
        ).forEach { sequence ->
            log.info { "Restarting sequence '$schema.$sequence' to 1" }
            db.update("alter sequence $schema.$sequence restart", emptyMap<String, String>())
        }
    }
}
