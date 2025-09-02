plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.prove"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {

	implementation("com.tagitech:provelib:0.0.0") // maven local
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-docker-compose")
	implementation("org.hibernate.orm:hibernate-community-dialects:6.6.22.Final")
	implementation("org.springframework.modulith:spring-modulith-starter-core:1.3.2")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test:1.3.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.wrapper {
	gradleVersion = "9.0"
}

abstract class BootstrapProvelibTask @Inject constructor(
	private val execOps: ExecOperations,
	private val objects: ObjectFactory
) : DefaultTask() {

	private val provelibDirProp: DirectoryProperty = objects.directoryProperty().convention(project.layout.projectDirectory.dir("../provelib"))

	@TaskAction
	fun bootstrap() {
		val pDir = provelibDirProp.get().asFile
		if (!pDir.exists()) {
			println("Cloning provelib...")
			execOps.exec {
				workingDir = project.rootProject.projectDir.parentFile
				commandLine("git", "clone", "https://github.com/dkotrada/provelib.git")
			}
		} else {
			println("Updating provelib...")
			execOps.exec {
				workingDir = pDir
				commandLine("git", "pull")
			}
		}

		println("Publishing provelib to mavenLocal...")
		execOps.exec {
			workingDir = pDir
			commandLine("./gradlew", "publish")
		}
	}
}

tasks.register<BootstrapProvelibTask>("bootstrapProvelib") {
	group = "setup"
	description = "Bootstraps local development by cloning/updating and publishing provelib."
}
