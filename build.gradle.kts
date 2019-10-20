import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer

val akkaVersion = "2.5.25"
val akkaHttpVersion = "10.1.10"
val logbackVersion = "1.2.3"
val scalaMinorVersion = "2.13.1"
val scalaVersion = "2.13"
val sparkDariaVersion = "0.31.0-s_${scalaVersion}"
val sparkVersion = "2.4.3"

group = "com.goticks"
version = "1.0-SNAPSHOT"


plugins {
    id("idea")
    id("java")
    id("scala")
    id("cz.alenkacz.gradle.scalafmt") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("com.github.maiflai.scalatest") version "0.25"
    kotlin("jvm") version "1.3.20"
}

repositories {
    jcenter()
    maven(url = "http://dl.bintray.com/spark-packages/maven")
    maven(url = "http://repo.artima.com/releases")
}

dependencies {
    //Scala
    compileOnly("org.scala-lang:scala-library:$scalaMinorVersion")
    compileOnly("org.scala-lang:scala-reflect:$scalaMinorVersion")
    compileOnly("org.scala-lang:scala-compiler:$scalaMinorVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Akka
    implementation("com.typesafe.akka:akka-actor_${scalaVersion}:${akkaVersion}")
    implementation("com.typesafe.akka:akka-http_${scalaVersion}:${akkaHttpVersion}")
    implementation("com.typesafe.akka:akka-http-core_${scalaVersion}:${akkaHttpVersion}")
    implementation("com.typesafe.akka:akka-http-spray-json_${scalaVersion}:${akkaHttpVersion}")
    implementation("com.typesafe.akka:akka-slf4j_${scalaVersion}:${akkaVersion}")
    implementation("com.typesafe.akka:akka-stream_${scalaVersion}:${akkaVersion}")

    // Test
    testImplementation("com.typesafe.akka:akka-testkit_${scalaVersion}:${akkaVersion}")
    testImplementation("org.scalatest:scalatest_${scalaVersion}:3.0.8")
    testImplementation("org.scalamock:scalamock_${scalaVersion}:4.4.0")
    testImplementation("org.mockito:mockito-core:2.18.3")
    testImplementation("log4j:log4j:1.2.17")
    testImplementation("org.pegdown:pegdown:1.6.0")
}

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes(mapOf("Main-Class" to "com.goticks.Main"))
        }
        transform(AppendingTransformer::class.java) {
            resource = "reference.conf"
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

scalafmt {
    configFilePath = ".scalafmt.conf"
}
