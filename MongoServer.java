import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.omg.CORBA.portable.Delegate;

public class MongoServer {
    static Vertx vertx = Vertx.vertx();
    static JsonObject config = Vertx.currentContext().config();

    static JsonObject mongoconfig = new JsonObject()
            .put("connection_string", "mongodb://localhost:27017")
            .put("db_name", "Books");

    static MongoClient mongo = MongoClient.createShared(vertx, mongoconfig);

    public static void HandelCreateData(RoutingContext rc) {

        HttpServerResponse response = rc.response();
        JsonObject document = new JsonObject()
                .put("grade","A");
            mongo.insert("Books", document, res -> {
            if (res.succeeded()) {
                System.out.println("Inserted");
                rc.response().putHeader("content-type", "application/json").setStatusCode(200).end();
            } else {
                res.cause().printStackTrace();
                rc.response().putHeader("content-type", "application/json").setStatusCode(403).end(res.cause().getMessage());
            }
        });
            response.end();
    }

    public static void HandelDeleteData(RoutingContext rc) {

        String id = rc.request().getParam("id");
        HttpServerResponse response = rc.response();
        JsonObject del = new JsonObject().put("id", id);
            mongo.removeDocument("Books",del,res -> {
            if (res.succeeded()) {
                System.out.println("Delete Success");
                rc.response().putHeader("content-type", "application/json").setStatusCode(200).end();
            } else {
                System.out.println("Delete Failed");
                res.cause().printStackTrace();
                rc.response().putHeader("content-type", "application/json").setStatusCode(403).end(res.cause().getMessage());
            }
        });
            response.end();
    }

   public static void HandelUpdateData(RoutingContext rc) {
       JsonObject query = new JsonObject()
               .put("Rollno", "401");
       JsonObject up = new JsonObject().put("$set", new JsonObject()
               .put("Nickname", "Bhoot"));
       mongo.updateCollection("Books",query, up, res -> {
           if (res.succeeded()) {
               System.out.println("data updated !");
               rc.response().putHeader("content-type", "application/json").setStatusCode(200).end();
           } else {
               res.cause().printStackTrace();
               rc.response().putHeader("content-type", "application/json").setStatusCode(403).end(res.cause().getMessage());
           }
       });

            }


        public static void HandelListData(RoutingContext rc) {
        String id = rc.request().getParam("id");
        JsonArray arr = new JsonArray();
        JsonObject list = new JsonObject().put("id", id);
        mongo.find("Books", list, res -> {
            if (res.succeeded()) {
                for (JsonObject json : res.result()) {
                    arr.add(json);
                }
                rc.response().putHeader("content-type", "application/json").setStatusCode(200).end(arr.encodePrettily());
            } else {
                res.cause().printStackTrace();
                rc.response().putHeader("content-type", "application/json").setStatusCode(403).end(res.cause().getMessage());
            }
        });
    }

    private static  void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

}