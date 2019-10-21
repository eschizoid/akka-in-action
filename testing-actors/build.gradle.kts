import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer

group = "com.goticks"
version = "1.0-SNAPSHOT"

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
    configFilePath = "./.scalafmt.conf"
}
