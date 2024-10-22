plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
    signing
}

java {
    // Apply a specific Java toolchain to ease working on different environments.
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    // Generate javadoc and sources jar for maven central publishing
    withJavadocJar()
    withSourcesJar()
}

val nameForArtifactAndJar by extra("ddi-lifecycle")

repositories {
    // Check maven local dependencies to avoid unnecessary network calls.
    mavenLocal()
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.xmlbeans:xmlbeans:5.2.1")
    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")
    //
    implementation("org.springframework:spring-beans:6.1.14")

    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.3")
    // XMLUnit
    testImplementation("org.xmlunit:xmlunit-assertj3:2.10.0")
}

sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }
        resources {
            srcDir("src/main/resources")
            srcDir("src/main/ddi-sources")
        }
    }
}

tasks.register("generateSources", type = JavaExec::class) {
    group = "build"
    description = "Generate java sources from DDI xsd"
    classpath = sourceSets["main"].compileClasspath
    mainClass = "org.apache.xmlbeans.impl.tool.SchemaCompiler"
    args(
            "-srconly",
            "-src",
            sourceSets["main"].java.sourceDirectories.asPath,
            "-d",
            sourceSets["main"].resources.sourceDirectories.toList()[0].toPath(),
            sourceSets["main"].resources.sourceDirectories.toList()[1].toPath()
    )
}

tasks.named("generateSources").configure {
    onlyIf {
        fileTree(sourceSets["main"].resources.sourceDirectories.toList()[0]) {
            include("**/*.xsb")
        }.isEmpty
    }
}

tasks.named("compileJava") {
    dependsOn("generateSources")
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName = nameForArtifactAndJar
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks {
    javadoc {
        (options as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = nameForArtifactAndJar
            from(components["java"])
            pom {
                name = "DDI Lifecycle Java"
                description = "Java classes for DDI Lifecycle"
                url = "https://github.com/InseeFr/DDI-Lifecycle-Java"
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/license/mit/"
                    }
                }
                developers {
                    developer {
                        id = "FBibonne"
                        name = "Fabrice Bibonne"
                    }
                    developer {
                        id = "nsenave"
                        name = "Nicolas Senave"
                        email = "nicolas.senave@insee.fr"
                    }
                }
                scm {
                    url = "https://github.com/InseeFr/DDI-Lifecycle-Java"
                    connection = "scm:git:git://github.com/InseeFr/DDI-Lifecycle-Java.git"
                    developerConnection = "scm:git:ssh://github.com/InseeFr/DDI-Lifecycle-Java.git"
                }
            }
        }
    }
}

// https://docs.gradle.org/current/userguide/signing_plugin.html
signing {
    isRequired = true
    // Environment variables for ascii-armored keys (to be used in CI)
    val privateGPGKey = System.getenv("GPG_SIGNING_KEY")
    val gpgPassword = System.getenv("GPG_PASSWORD")
    if (privateGPGKey != null && gpgPassword != null) {
        useInMemoryPgpKeys(privateGPGKey, gpgPassword)
    }
    // NB: If ascii-armored keys are not given, signing.keyId, signing.secretKeyRingFile, and signing.password
    // properties are used
    sign(publishing.publications["mavenJava"])
}

tasks.withType<Sign> {
    onlyIf {
        !version.toString().endsWith("SNAPSHOT")
    }
}
