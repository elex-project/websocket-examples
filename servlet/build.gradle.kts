plugins {
    java
    war
}
repositories {
    mavenCentral()
}
configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
    testCompileOnly {
        extendsFrom(testAnnotationProcessor.get())
    }
}
tasks.test {
    useJUnitPlatform()
}
dependencies {
    providedCompile ("javax.servlet:javax.servlet-api:4.0.1")
    implementation("javax.websocket:javax.websocket-api:1.1")

    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.jetbrains:annotations:20.1.0")

    compileOnly("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.16")

    testImplementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}
