import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.5.0"
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

repositories {
    jcenter()
    maven{url = uri("https://jitpack.io" )}
}

sourceSets.main {
    java.srcDirs("src")
    resources.srcDirs("src/resources")
}


application {
    mainClass.set("ru.team10.graphApp.MainApp")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    //Leiden
    implementation("com.github.CWTSLeiden:networkanalysis:1.1.0")

    //Neo4j
    implementation("org.neo4j.driver", "neo4j-java-driver", "4.2.5")

    
    implementation("no.tornado:tornadofx:1.7.20") {
        exclude("org.jetbrains.kotlin")
    }
    implementation("org.xerial", "sqlite-jdbc", "3.34.0")

    // Logging
    implementation("io.github.microutils", "kotlin-logging-jvm", "2.0.6")
    implementation("org.slf4j", "slf4j-simple", "1.7.29")

    implementation("com.sksamuel.hoplite:hoplite-core:1.4.0")
    implementation("com.sksamuel.hoplite:hoplite-yaml:1.4.1")
    implementation("org.yaml", "snakeyaml", "1.8")

}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

javafx {
    version = "11.0.2"
    modules("javafx.controls")
}
