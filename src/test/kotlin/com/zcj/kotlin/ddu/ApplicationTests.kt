package com.zcj.kotlin.ddu.com.zcj.kotlin.ddu

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class ApplicationTests {

	@Test
	fun contextLoads() {
	}
	@Test
	fun getSubject(){
        val date = Date()
        val sdf = SimpleDateFormat("MMdd");
        println("ELP日报【查从杰】-17"+sdf.format(date))
    }

}
