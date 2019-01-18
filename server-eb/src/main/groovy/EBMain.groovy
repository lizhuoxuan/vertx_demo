import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject

/**
 * Created by liurui on 2016/11/30.
 */
class EBMain extends AbstractVerticle {

    static void main(String[] args) {
        Vertx.vertx().deployVerticle(EBMain.newInstance())
    }

    @Override
    void start() throws Exception {
        def eb = vertx.eventBus()

        eb.consumer("gateway", {
            println(it.body())
            it.reply(new JsonObject([mess:"i received"]))
        })

        println("ok")
    }

}
