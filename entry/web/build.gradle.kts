import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.spring") version "1.9.24"
}

dependencies {
    // 모듈
    implementation(project(":adapters:in-web"))
    implementation(project(":domain"))

    // 라이브러리
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks {
    withType<Jar> { enabled = false }
    withType<BootJar> { enabled = true }
}
