package $package;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import com.mongodb.client.FindIterable; 
import com.mongodb.client.MongoCollection; 
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientURI;  

import java.util.Iterator; 
import org.bson.Document; 
import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential; 
import static com.mongodb.client.model.Filters.eq;


/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpTrigger-Java". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTrigger-Java&code={your function key}
     * 2. curl "{your host}/api/HttpTrigger-Java?name=HTTP%20Query&code={your function key}"
     * Function Key is not needed when running locally, to invoke HttpTrigger deployed to Azure, see here(https://docs.microsoft.com/en-us/azure/azure-functions/functions-bindings-http-webhook#authorization-keys) on how to get function key for your app.
     */
    @FunctionName("food")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String fruit = request.getQueryParameters().get("fruit");
        
        if (fruit == null) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND).build();
        }

        // Creating a Mongo client 
        //MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
        MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://test-database-stacy:W9pz0N3IUCM69iKT73nG5S1BROEuGvrAMgylWWkclTA1cm8qT37HGYxU0zHZ5NpwP60it8S8OWLPArxLA7CvRQ==@test-database-stacy.documents.azure.com:10255/?ssl=true&replicaSet=globaldb"));
        
        // Creating Credentials 
        // MongoCredential credential; 
        // credential = MongoCredential.createCredential("sampleUser", "myDb", 
        //     "password".toCharArray()); 
        System.out.println("Connected to the database successfully");  
        
        // Accessing the database 
        MongoDatabase database = mongo.getDatabase("db"); 
        //System.out.println("Credentials ::"+ credential); 
        
        // Retieving a collection
        MongoCollection<Document> collection = database.getCollection("coll"); 
        System.out.println("Collection myCollection selected successfully");
        
        // Getting the iterable object 
        //FindIterable<Document> iterDoc = collection.find(); 
        //int i = 1; 
      
        Document myDoc = collection.find(eq("fruit",fruit)).first(); 

        if (myDoc == null) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND).build();
        } else {
            //return request.createResponseBuilder(HttpStatus.OK).body("Hello, friend " + name).build();
            return request.createResponseBuilder(HttpStatus.OK).header("Content-Type","application/json").body(myDoc.toJson()).build();

        }
    }
}
