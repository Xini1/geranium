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

    compileOnly(project(":geranium-core"))
    compileOnly(Dependencies.springBootStarterAop)

    testCompileOnly(project(":geranium-core"))
    testCompileOnly(Dependencies.junitApi)
    testRuntimeOnly(Dependencies.junitEngine)
    testImplementation(Dependencies.assertj)
    testImplementation(testFixtures(project(":geranium-core")))
    testImplementation(Dependencies.springBootStarterAop)
    testImplementation(Dependencies.springBootStarterTest)

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}