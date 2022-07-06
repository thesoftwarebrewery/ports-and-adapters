package softwarebrewery.demo.domain

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

@Target(ANNOTATION_CLASS, CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class PrimaryPort

@Target(ANNOTATION_CLASS, CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class SecondaryPort

@Target(ANNOTATION_CLASS, CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class DomainEvent
