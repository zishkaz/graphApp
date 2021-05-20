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
    java.srcDirs("src/kotlin")
    resources.srcDirs("src/resources")
}

sourceSets.test {
    java.srcDirs("src/test")
}


application {
    mainClass.set("ru.team10.graphApp.MainApp")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    //JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.testfx:testfx-core:4.0.16-alpha")
    testImplementation("org.testfx:testfx-junit5:4.0.16-alpha")

    //Leiden
    implementation("com.github.CWTSLeiden:networkanalysis:1.1.0")

    //Neo4j
    implementation("org.neo4j.driver", "neo4j-java-driver", "4.2.5")

    
    implementation("no.tornado:tornadofx:1.7.20") {
        exclude("org.jetbrains.kotlin")
    }
    implementation("org.xerial", "sqlite-jdbc", "3.34.0")

    //Logging
    implementation("io.github.microutils", "kotlin-logging-jvm", "2.0.6")
    implementation("org.slf4j", "slf4j-api", "1.7.29")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("ch.qos.logback:logback-core:1.2.3")

    implementation("com.sksamuel.hoplite:hoplite-core:1.4.0")
    implementation("com.sksamuel.hoplite:hoplite-yaml:1.4.1")
    implementation("org.yaml", "snakeyaml", "1.8")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.15.2")

}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

javafx {
    version = "11.0.2"
    modules("javafx.controls")
}
