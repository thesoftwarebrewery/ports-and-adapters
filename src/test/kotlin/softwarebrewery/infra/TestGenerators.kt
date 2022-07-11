package softwarebrewery.infra

fun namedRandom(prefix: String, length: Int = 5) =
    prefix + "-" + randomString(length)

fun randomString(length: Int = 5): String {
    val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}
