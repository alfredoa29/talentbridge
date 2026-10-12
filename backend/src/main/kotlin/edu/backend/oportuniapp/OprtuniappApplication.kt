package edu.backend.oportuniapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(CloudinaryProperties::class)
class OprtuniappApplication

fun main(args: Array<String>) {
	runApplication<OprtuniappApplication>(*args)
}
