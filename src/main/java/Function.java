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
    @FunctionName("HttpTrigger-Java")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String query = request.getQueryParameters().get("name");
        String name = request.getBody().orElse(query);

        // Creating a Mongo client 
        MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
        //MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://common-hotel-database:DQOdD1h89DDIvzQAv8DnNB7asjeLAks0LqYryUXsDG3O8R2FTjb1FpiAGeHSnOb2gVmbyet2RzKMIDy1BVhZ7Q==@common-hotel-database.documents.azure.com:10255/?ssl=true&replicaSet=globaldb"));
    
        // Creating Credentials 
        MongoCredential credential; 
        credential = MongoCredential.createCredential("sampleUser", "myDb", 
            "password".toCharArray()); 
        System.out.println("Connected to the database successfully");  
        
        // Accessing the database 
        MongoDatabase database = mongo.getDatabase("myDb"); 
        System.out.println("Credentials ::"+ credential); 
        
        // Retieving a collection
        MongoCollection<Document> collection = database.getCollection("myCollection"); 
        System.out.println("Collection myCollection selected successfully");
        
        // Getting the iterable object 
      FindIterable<Document> iterDoc = collection.find(); 
      int i = 1; 

      // Getting the iterator 
      Iterator it = iterDoc.iterator(); 
    
      while (it.hasNext()) {  
         System.out.println(it.next());  
      i++; 
      }

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, friend " + name).build();
        }
    }
}
