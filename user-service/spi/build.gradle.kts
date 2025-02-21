import com.google.protobuf.gradle.id

plugins {
    id("java-library")
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
    id("com.google.protobuf")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
    dependencyManagement {
        imports {
            mavenBom("org.springframework.grpc:spring-grpc-dependencies:${property("springGrpcVersion")}")
        }
    }
}

dependencies {
    implementation(project(":common"))
    api("io.grpc:grpc-servlet-jakarta:${dependencyManagement.importedProperties["grpc.version"]}")
    api("io.grpc:grpc-services:${dependencyManagement.importedProperties["grpc.version"]}")
    api("io.grpc:grpc-stub:${dependencyManagement.importedProperties["grpc.version"]}")
    api("org.springframework.grpc:spring-grpc-spring-boot-starter:${property("springGrpcVersion")}")
    testApi("org.springframework.grpc:spring-grpc-test:${property("springGrpcVersion")}")
    implementation("org.springframework.security:spring-security-core")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${dependencyManagement.importedProperties["protobuf-java.version"]}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${dependencyManagement.importedProperties["grpc.version"]}"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc") {
                    option("jakarta_omit")
                    option("@generated=omit")
                }
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}