package softwarebrewery.app

import com.tngtech.archunit.core.importer.*
import com.tngtech.archunit.junit.*
import com.tngtech.archunit.lang.*
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*

@AnalyzeClasses(
    packagesOf = [ApplicationArchitectureTest::class],
    importOptions = [ImportOption.DoNotIncludeTests::class],
)
class ApplicationArchitectureTest {

    @ArchTest
    val `domain layer is independent of other layers`: ArchRule =
        classes()
            .that().resideInAPackage("..domain..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                "..domain..",
                "java..",
                "kotlin..",
                "org.jetbrains.annotations",
                // don't want this
                // "org.springframework.transaction.annotation",
            )
            .because("domain layer should be ignorant of technical implementation details")
}
