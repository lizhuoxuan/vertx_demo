import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;

public class Proxy extends AbstractVerticle {


    public static void main(String[] args) {
        Vertx.vertx().deployVerticle("Proxy",new DeploymentOptions().setInstances(4));
    }

    @Override
    public void start() throws Exception {
        HttpClientOptions options = new HttpClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(7788)
                .setKeepAlive(true)
                .setMaxPoolSize(100);

        HttpClient client = vertx.createHttpClient(options);


        HttpServer server = vertx.createHttpServer();

        server.requestHandler(req -> {

            HttpServerResponse response = req.response();
            response.setChunked(true);

            HttpClientRequest request = client.request(req.method(), req.uri(), res -> {
                response.headers().setAll(res.headers());
                response.setStatusCode(res.statusCode());


                res.handler(response::write);
                res.endHandler(v -> response.end());
            });

            request.headers().setAll(req.headers());

            req.handler(data -> {
                request.write(data);
            });

            req.endHandler(v -> {
                request.end();
            });


        }).listen(5050);

    }
}
