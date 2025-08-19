import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.openapi.generator") version "7.5.0"
}

val basePackage = "br.com.jrpbjr.creditapplicationsystem.generated"

val openApiSpec = layout.projectDirectory.file("src/main/resources/openapi/api-docs.yaml")

tasks.named<GenerateTask>("openApiGenerate") {
    generatorName.set("kotlin-spring")
    inputSpec.set(openApiSpec.asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)

    packageName.set("$basePackage.application.web")
    apiPackage.set("$basePackage.application.web.api")
    modelPackage.set("$basePackage.application.web.dto")

    configOptions.set(
        mapOf(
            "useSpringBoot3" to "true",
            "useJakartaEe" to "true",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "hideGenerationTimestamp" to "true",
            "openApiNullable" to "false",
            "exceptionHandler" to "false",
            "dateLibrary" to "java8",
            "modelLocalDateTimeType" to "java.time.LocalDateTime",
            "localDateTimeType" to "java.time.LocalDateTime",
            "modelPropertyNaming" to "original",
            "enumPropertyNaming" to "UPPERCASE",
            "gradleBuildFile" to "false"
        )
    )

    typeMappings.set(
        mapOf(
            "date"          to "java.time.LocalDate",
            "date-time"     to "java.time.LocalDateTime",
            "DateTime"      to "java.time.LocalDateTime",
            "OffsetDateTime" to "java.time.LocalDateTime",
            "decimal"       to "java.math.BigDecimal",
            "number"        to "java.math.BigDecimal",
            "uuid"          to "java.util.UUID"
        )
    )
    importMappings.set(
        mapOf(
            "BigDecimal"      to "java.math.BigDecimal",
            "LocalDate"       to "java.time.LocalDate",
            "LocalDateTime"   to "java.time.LocalDateTime",
            "OffsetDateTime"  to "java.time.LocalDateTime",
            "UUID"            to "java.util.UUID"
        )
    )
}

sourceSets.named("main") {
    java.srcDir(layout.buildDirectory.dir("generated/openapi/src/main/kotlin").get().asFile)
}

tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}

tasks.named<Delete>("clean") {
    delete(layout.buildDirectory.dir("generated/openapi"))
}


group = "br.com.jrpbjr"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.4")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}