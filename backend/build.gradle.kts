import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	application
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("kapt") version "2.1.20"
}
val springAiVersion by extra("1.0.0-M8")

group = "edu.backend"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}


repositories {
	mavenCentral()
//	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation ("org.mapstruct:mapstruct:1.6.3")
	implementation("org.springframework.ai:spring-ai-starter-model-openai")
	implementation("org.springframework.ai:spring-ai-starter-model-anthropic")
	implementation("org.springframework.ai:spring-ai-pdf-document-reader")
	implementation ("org.springframework.ai:spring-ai-tika-document-reader")
	implementation("com.cloudinary:cloudinary-http44:1.39.0")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	kapt("org.mapstruct:mapstruct-processor:1.6.3")
	annotationProcessor ("org.mapstruct:mapstruct-processor:1.6.3")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
	}
}

kotlin {
	jvmToolchain(17)

}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
	getByName<Jar>("jar") {
		enabled = false
	}

	getByName<Zip>("distZip") {
		enabled = false
	}

	getByName<Tar>("distTar") {
		enabled = false
	}

	getByName<Zip>("bootDistZip") {
		enabled = false
	}

	getByName<Tar>("bootDistTar") {
		enabled = false
	}

	getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
		enabled = true
	}
}
