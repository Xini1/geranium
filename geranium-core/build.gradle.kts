plugins {
    java
}

group = "by.geranium"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(Dependencies.lombok)
    annotationProcessor(Dependencies.lombok)

    compileOnly(Dependencies.slf4j)
    implementation("org.springframework.boot:spring-boot-starter-aop:2.5.2")
    implementation("org.springframework.boot:spring-boot-starter-test:2.5.2")

    testCompileOnly(Dependencies.junitApi)
    testRuntimeOnly(Dependencies.junitEngine)
    testImplementation(Dependencies.assertj)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}