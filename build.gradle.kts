plugins {
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    group = "fr.insee.ddi"
    version = "1.1.0"
}

tasks.register("printVersion") {
    doLast {
        println(project.version)
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://oss.sonatype.org/content/repositories/snapshots/"))

            val mavenUserProp = System.getenv("OSSRH_USERNAME") ?: findProperty("ossrh.username")
            val mavenPwdProp = System.getenv("OSSRH_PASSWORD") ?: findProperty("ossrh.password")

            if (mavenUserProp != null && mavenPwdProp != null) {
                username = "$mavenUserProp"
                password = "$mavenPwdProp"
            }
        }
    }
}
