import groovy.json.JsonOutput
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router

/**
 * Created by liurui on 2016/11/30.
 */
class HttpMain extends AbstractVerticle {

    static void main(String[] args) {
        Vertx.vertx().deployVerticle(HttpMain.newInstance())
    }

    @Override
    void start() throws Exception {
        def router = Router.router(vertx)

        def eb = vertx.eventBus()
        router.get("/test").handler({ ctx ->
            eb.send("gateway", new JsonObject([mess:"123"]), {
                if (it.succeeded()) {
                    println(it.result().body())
                    ctx.response().end(it.result().body().toString())
                } else {
                    println(it.cause().message)
                    // TODO something
                    ctx.response().end(it.cause().message)
                }
            })
            ctx.response().end("haha")
        })

        vertx.createHttpServer().requestHandler(router).listen(8080, {
            if (it.succeeded()) {
                println("success")
            } else {
                println(it.cause().message)
            }
        })
    }

}



