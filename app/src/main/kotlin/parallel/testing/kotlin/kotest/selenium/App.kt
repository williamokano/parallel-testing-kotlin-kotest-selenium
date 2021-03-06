/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package parallel.testing.kotlin.kotest.selenium

import com.svetylkovo.krool.krool
import com.svetylkovo.krool.kroolContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import java.util.concurrent.atomic.AtomicInteger

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

class Db {
    init {
        println("Connecting to database...")
        Thread.sleep(1000)
    }

    fun getUrls(id: Int): List<String> {
        println("Selecting URLs from database for ID $id on ${Thread.currentThread().name}")
        Thread.sleep(2000)
        return (1..2).map { "http://urlservice.com/id/$id/page/$it" }
    }

    fun close() {
        println("Closing Db connection")
    }
}

fun main() {
    println(App().greeting)

    runBlocking {
        val webDriverPool = krool(20) { ChromeDriver() }
        val counter = AtomicInteger(0)
        try {
            (400..600).map { id ->
                async(Dispatchers.IO) {
                    webDriverPool.use { webDriver ->
                        println("Driver running for instance: $id")
                        webDriver.get("http://www.jornalsmantiqueira.com.br/jornal/noticias-descricao.php?id_noticia=$id")

                        val title = webDriver.findElement(By.xpath("/html/body/div[3]/div[2]/div/span/strong"))
                        println("$id: ${title.text}")
                        counter.incrementAndGet()
                    }
                }
            }.awaitAll()

        } finally {
            println("How many actually executed? ${counter.get()}")
            webDriverPool.closeWith { it.quit() }
        }

        kroolContext.close()
    }
}
