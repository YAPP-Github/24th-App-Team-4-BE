import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
    `java-test-fixtures`
}

dependencies {
    // 모듈
    implementation(project(":application"))
    implementation(project(":domain"))

    // 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    // 테스팅
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.1")

    // 테스트 픽쳐
    testFixturesImplementation("org.springframework.boot:spring-boot-starter-test")
    testFixturesImplementation("org.testcontainers:testcontainers:1.19.7")
    testFixturesImplementation("org.testcontainers:mysql:1.19.7")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
