package softwarebrewery

import com.tngtech.archunit.core.importer.*
import com.tngtech.archunit.junit.*
import com.tngtech.archunit.lang.*
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*
import com.tngtech.archunit.library.Architectures.*
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.*

@AnalyzeClasses(
    packagesOf = [ApplicationArchitectureTest::class],
    importOptions = [ImportOption.DoNotIncludeTests::class],
)
class ApplicationArchitectureTest {

    @ArchTest
    val `domain layer is independent of other layers`: ArchRule =
        classes()
            .that().resideInAPackage("..app.domain..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                "..app.domain..",
                "java..",
                "kotlin..",
                "org.jetbrains.annotations",
                // don't want this
                // "org.springframework.transaction.annotation",
            )
            .because("domain layer must be ignorant of technical implementation details")

    @ArchTest
    val `domain layer ports only depend on domain model`: ArchRule =
        noClasses()
            .that().resideInAPackage("..app.domain.ports..")
            .should().accessClassesThat().resideOutsideOfPackages(
                "..app.domain..",
                "java..",
                "kotlin..",
            )
            .because("domain layer ports must express themselves in term of the domain")

    @ArchTest
    val `adapters are independent from other adapters`: ArchRule =
        slices()
            .matching("..app.adapters.(*)")
            .should().notDependOnEachOther()

    @ArchTest
    val `infra is independent from application specific code`: ArchRule =
        noClasses()
            .that().resideInAPackage("softwarebrewery.infra..")
            .should().accessClassesThat().resideInAnyPackage(
                "softwarebrewery.app..",
            )
            .because("decoupling infra from application specifics makes them portable/reusable cross applications")

    @ArchTest
    val `layer dependencies`: ArchRule =
        layeredArchitecture()
            .layer("infra").definedBy(
                "java..", "kotlin..", "kotlinx..", "com..", "org..", "mu..", /* our */ "..infra.."
            )
            .layer("domain").definedBy("..app.domain..")
            .layer("adapters").definedBy("..app.adapters..")
            .layer("composition").definedBy("..app")
            .whereLayer("infra").mayOnlyBeAccessedByLayers("adapters", "composition")
            .whereLayer("adapters").mayOnlyAccessLayers("infra", "domain")
            .whereLayer("adapters").mayOnlyBeAccessedByLayers("composition")
            .whereLayer("domain").mayOnlyBeAccessedByLayers("adapters", "composition")
}
