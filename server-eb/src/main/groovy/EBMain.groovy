import io.reactiverse.pgclient.PgClient
import io.reactiverse.pgclient.PgPool
import io.reactiverse.pgclient.PgPoolOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject

/**
 * Created by liurui on 2016/11/30.
 */
class EBMain extends AbstractVerticle {

    static void main(String[] args) {
        Vertx.vertx().deployVerticle(EBMain.newInstance())
    }

    PgPool pgPool
    EventBus eb

    def channelName = 'flunk'

    @Override
    void start() throws Exception {
        eb = vertx.eventBus()

        pgPool = PgClient.pool(vertx, buildPgPoolOptions())
        // A simple query
        pgPool.query("SELECT * FROM t_test", { ar ->
            if (ar.succeeded()) {
                def result = ar.result()
                System.out.println("Got " + result.size() + " results ")
            } else {
                System.out.println("Failure: " + ar.cause().getMessage())
            }

            // Now close the pool
            pgPool.close()
        })
    }

    PgPoolOptions buildPgPoolOptions() {
        def port = config().getInteger("port", 5432)
        def host = config().getString("host", "192.168.12.99")
        def database = config().getString("database", "db_zdsapp")
        def user = config().getString("user", "zds")
        def password = config().getString("password", "hubushangdu16")
        def maxSize = config().getInteger("maxsize", 2)

        new PgPoolOptions()
                .setPort(port)
                .setHost(host)
                .setDatabase(database)
                .setUser(user)
                .setPassword(password)
                .setMaxSize(maxSize)
                .setCachePreparedStatements(true)
    }

}
