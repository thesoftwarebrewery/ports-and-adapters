package softwarebrewery.app.domain

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

@Target(ANNOTATION_CLASS, CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class InboundAdapter

@Target(ANNOTATION_CLASS, CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class OutboundAdapter

@Target(ANNOTATION_CLASS, CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class InboundPort

@Target(ANNOTATION_CLASS, CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class OutboundPort

@Target(ANNOTATION_CLASS, CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class DomainEvent
