import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.spring") version "1.9.24"
}

dependencies {
    // 모듈
    implementation(project(":domain"))

    // 라이브러리
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-tx")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // 테스팅
    testImplementation("io.mockk:mockk:1.13.7")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
