import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.spring") version "1.9.24"
}

extra["springCloudVersion"] = "2023.0.3"

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.google.firebase:firebase-admin:8.1.0")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.69")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    implementation("com.google.firebase:firebase-admin:8.1.0")

}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}
