plugins {
    id("java")
    war
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
}

tasks.war {
    archiveVersion.set("")
    archiveBaseName.set("javadev-hw10")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE // для уникання помилки з дублікатами
    from("src/main/webapp") {
        into("")
    }
}

tasks.test {
    useJUnitPlatform()
}

