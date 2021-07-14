plugins {
    java
}

group = "by.geranium"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    compileOnly("org.slf4j:slf4j-api:1.7.30")
    implementation("org.springframework.boot:spring-boot-starter-aop:2.5.2")
    implementation("org.springframework.boot:spring-boot-starter-test:2.5.2")

    testCompileOnly("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.19.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}