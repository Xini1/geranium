object Versions {

    const val lombok = "1.18.20"
    const val slf4j = "1.7.30"
    const val junit = "5.7.0"
    const val assertj = "3.19.0"
    const val springBoot = "2.5.2"
}

object Dependencies {

    const val lombok = "org.projectlombok:lombok:${Versions.lombok}"
    const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val junitApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
    const val assertj = "org.assertj:assertj-core:${Versions.assertj}"
    const val springBootStarterAop = "org.springframework.boot:spring-boot-starter-aop:${Versions.springBoot}"
    const val springBootStarterTest = "org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}"
}