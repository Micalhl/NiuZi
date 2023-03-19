plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
    kotlin("jvm") version "1.6.10"
    id("net.mamoe.mirai-console") version "2.14.0"
}

taboolib {
    install("common")
    install("common-5")
    install("module-configuration")
    install("module-database")
    install("module-lang")
    install("platform-application")
    classifier = null
    version = "6.0.10-98"

    options("skip-kotlin-relocate")
}

repositories {
    mavenCentral()
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/gradle-plugin")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}