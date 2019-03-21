package com.xzlzx


import groovy.util.logging.Slf4j
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler

/**
 * Created by liurui on 2016/11/30.
 */
@Slf4j
class HttpMain extends AbstractVerticle {

    static void main(String[] args) {
        Vertx.vertx().deployVerticle(HttpMain.newInstance())
    }

    @Override
    void start() throws Exception {
        def router = Router.router(vertx)

        def sharedData = vertx.sharedData()
        sharedData.getAsyncMap("test_map", {
            if (it.succeeded()) {
                println(it.result())
            } else {
                println(it.cause().message)
            }
        })
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
//            ctx.response().end("haha")
        })
        def staticHandler = StaticHandler.create()
        staticHandler.setAllowRootFileSystemAccess(true)
        def addr = "D:\\work\\vertx_demo\\server-http\\src\\main\\resources\\dist\\"
        staticHandler.setWebRoot(addr)
        router.route().handler(staticHandler)
        vertx.createHttpServer().requestHandler(router).listen(7788, {
            if (it.succeeded()) {
                log.info("success")
            } else {
                log.info(it.cause().message)
            }
        })
    }

}



