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
    compileOnly(Dependencies.slf4j)
}