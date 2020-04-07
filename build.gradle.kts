import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    application
    idea
    kotlin("jvm") version "1.3.71"

    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"

    // gradle dependencyUpdates -Drevision=release
    id("com.github.ben-manes.versions") version "0.28.0"
}

repositories {
    jcenter()
}

val javaVer = JavaVersion.VERSION_11

val coroutinesVer = "1.3.3"
val kotlinLoggingVer = "1.7.9"
val springDataAerospike = "2.3.3.RELEASE"
val testcontainersVer = "1.13.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVer")

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVer")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("com.aerospike:spring-data-aerospike:$springDataAerospike")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.testcontainers:testcontainers:$testcontainersVer")

    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.projectreactor:reactor-tools")
}

val appName = "app"
val appVer by lazy { "0.0.1-${gitRev()}" }

group = "app"
version = appVer

application {
    mainClassName = "app.AppKt"
    applicationName = appName
}

java {
    sourceCompatibility = javaVer
    targetCompatibility = javaVer
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(javaVer)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

springBoot {
    buildInfo {
        properties {
            artifact = "$appName-$appVer.jar"
            version = appVer
            name = appName
        }
    }
}

tasks {
    withType(KotlinCompile::class).configureEach {
        kotlinOptions {
            jvmTarget = javaVer.toString()
            freeCompilerArgs = listOf("-progressive")
        }
    }

    withType(JavaCompile::class).configureEach {
        options.isFork = true
    }

    withType(Test::class).configureEach {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2)
            .takeIf { it > 0 } ?: 1

        testLogging {
            showExceptions = true
            exceptionFormat = TestExceptionFormat.FULL
            showStackTraces = true
            showCauses = true
            showStandardStreams = true
            events = setOf(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
                TestLogEvent.STANDARD_OUT,
                TestLogEvent.STANDARD_ERROR
            )
        }

        reports.html.isEnabled = false

        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "6.3"
        distributionType = Wrapper.DistributionType.ALL
    }

    bootJar {
        manifest {
            attributes("Multi-Release" to true)
        }

        archiveBaseName.set(appName)
        archiveVersion.set(appVer)

        if (project.hasProperty("archiveName")) {
            archiveFileName.set(project.properties["archiveName"] as String)
        }
    }

    register<Delete>("cleanOut") {
        delete("out")
    }

    clean {
        dependsOn("cleanOut")
    }

    withType(DependencyUpdatesTask::class) {
        resolutionStrategy {
            componentSelection {
                all {
                    val rejected = listOf(
                        "alpha", "beta", "rc", "cr", "m",
                        "preview", "b", "ea", "eap"
                    ).any { q ->
                        candidate.version.matches(
                            Regex("(?i).*[.-]$q[.\\d-+]*")
                        )
                    }
                    if (rejected) {
                        reject("Release candidate")
                    }
                }
            }
        }

        checkForGradleUpdate = true
        outputFormatter = "json"
        outputDir = "build/dependencyUpdates"
        reportfileName = "report"
    }
}

fun gitRev() = ProcessBuilder("git", "rev-parse", "--short", "HEAD").start().let { p ->
    p.waitFor(100, TimeUnit.MILLISECONDS)
    p.inputStream.bufferedReader().readLine() ?: "none"
}

