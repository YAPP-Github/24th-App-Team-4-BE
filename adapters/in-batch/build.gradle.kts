import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.spring") version "1.9.24"
}

dependencies {
    // 모듈
    implementation(project(":domain"))
    implementation(project(":application"))

    // 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("com.navercorp.spring:spring-boot-starter-batch-plus-kotlin:1.1.0")

    // 테스팅
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
