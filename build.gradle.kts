import java.net.URI

plugins {
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

group = "fr.insee.ddi"
version = "1.0.1"

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://oss.sonatype.org/content/repositories/snapshots/"))

            val mavenUserProp = System.getenv("OSSRH_USERNAME") ?: findProperty("ossrh.username")
            val mavenPwdProp = System.getenv("OSSRH_PASSWORD") ?: findProperty("ossrh.password")

            if (mavenUserProp != null && mavenPwdProp != null) {
                println("Maven user: $mavenUserProp")
                username = "$mavenUserProp"
                password = "$mavenPwdProp"
            }
        }
    }
}
