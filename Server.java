import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class Server {
    public static void main(String args[])
    {
        Vertx vertx=Vertx.vertx();

        HttpServer httpServer=vertx.createHttpServer();

        Router router= Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.get("/data").handler(MongoServer::HandelListData);
        router.put("/data/update").handler(MongoServer::HandelUpdateData);
        router.post("/data/create").handler(MongoServer::HandelCreateData);
        router.delete("/data/delete").handler(MongoServer::HandelDeleteData);

        vertx.createHttpServer().requestHandler(router::accept).listen(1234);
    }


}