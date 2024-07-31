package com.bada.weolbu

import com.bada.weolbu.auth.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class WeolbuApplication

fun main(args: Array<String>) {
	runApplication<WeolbuApplication>(*args)
}
