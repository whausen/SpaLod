package info.ponciano.lab.spalodwfs.controller.ogc_api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import info.ponciano.lab.spalodwfs.controller.storage.StorageProperties;
import info.ponciano.lab.spalodwfs.model.DatasetDownlift;
import info.ponciano.lab.spalodwfs.model.Triplestore;
import info.ponciano.lab.spalodwfs.mvc.models.semantic.KB;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.util.UUID;

@RequestMapping("/api/spalodWFS")
@RestController
public class OGCAPIController {
    @Value("${spring.application.VITE_APP_GRAPH_DB}")
    private String graphDbUrl;
    @Value("${spring.application.GRAPHDB_QUERY_ENDPOINT}")
    private String GRAPHDB_QUERY_ENDPOINT;

    @Value("${spring.application.VITE_APP_API_BASE_URL}")
    private String apiBaseUrl;

    @Value("${spring.application.VITE_APP_FRONT_BASE_URL}")
    private String frontBaseUrl;

    /**
     * Return the landing page of the API
     * 
     * @return String[][] corresponding to the landing page
     */
    @GetMapping("/")
    public String landingPage() {

        String results = "{\"head\":{\"vars\":\n";
        results += "[\"Feature\", \"HTML\", \"JSON\"]},\"results\":{\"bindings\":[\n";
        results += "{\"Feature\": {\"value\": \"Conformance\"},\"HTML\": {\"value\": \"" + frontBaseUrl
                + "/spalodWFS/conformance\"}, \"JSON\": {\"value\": \""+apiBaseUrl+"/api/spalodWFS/conformance\"}},\n";
        results += "{\"Feature\": {\"value\": \"Collections\"},\"HTML\": {\"value\": \"" + frontBaseUrl
                + "/spalodWFS/collections\"}, \"JSON\": {\"value\": \""+apiBaseUrl+"/api/spalodWFS/collections\"}}\n";
        results += "]}}";

        return results;
    }

    /**
     * Return the list of the collections
     * 
     * @return String[][] corresponding to the collection list
     */
    @GetMapping("/collections")
    public String collectionsList() {
        System.out.println("***********" + "/collections" + "***********");
        String query = "SELECT DISTINCT ?collections ?name WHERE {\n?collections <http://www.w3.org/ns/dcat#dataset> ?dataset .\n ?collections <http://purl.org/dc/terms/title> ?name .}";
        System.out.println(query);
        String results;
        results = Triplestore.executeSelectQuery(query, GRAPHDB_QUERY_ENDPOINT);
        // results = Triplestore.get().executeSelectQuery(query);
        return results;
    }

    /**
     * Return a specific collection
     * 
     * @param collectionId Unique identifier of the collection
     * @return String[][] corresponding to the specific collection
     */
    @GetMapping("/collections/{collectionId}")
    public String collectionQuery(@PathVariable String collectionId) {
        System.out.println("***********" + "/collections/" + collectionId + "***********");
        String query = "SELECT ?title ?description ?publisher ?dataset WHERE {\n?collection <http://purl.org/dc/terms/title> ?title .\n?collection <http://purl.org/dc/terms/description> ?description .\n?collection <http://purl.org/dc/terms/publisher> ?publisher .\n?collection <http://www.w3.org/ns/dcat#dataset> ?dataset .\nFILTER(?collection = <"
                + KB.NS + "" + collectionId + ">)\n}";
        System.out.println(query);
        String results;
        results = Triplestore.executeSelectQuery(query, GRAPHDB_QUERY_ENDPOINT);
        // results = Triplestore.get().executeSelectQuery(query);
        return results;
    }

    /**
     * Return the list of the datasets of a specific collection
     * 
     * @param collectionId Unique identifier of the collection
     * @return String[][] corresponding to the specific collection
     */
    @GetMapping("/collections/{collectionId}/items")
    public String datasetList(@PathVariable String collectionId) {
        System.out.println("***********" + "/collections/" + collectionId + "/items/***********");
        String query = "SELECT ?dataset ?title ?description ?publisher ?distribution WHERE {\n?collection <http://www.w3.org/ns/dcat#dataset> ?dataset .\nFILTER(?collection = <"
                + KB.NS + "" + collectionId
                + ">)\n?dataset <http://purl.org/dc/terms/title> ?title .\n?dataset <http://purl.org/dc/terms/description> ?description .\n?dataset <http://purl.org/dc/terms/publisher> ?publisher .\n?dataset <http://www.w3.org/ns/dcat#distribution> ?distribution .\n}";
        System.out.println(query);
        String results;
        results = Triplestore.executeSelectQuery(query, GRAPHDB_QUERY_ENDPOINT);
        // results = Triplestore.get().executeSelectQuery(query);
        return results;
    }

    /**
     * Return the list of the items inside a specific dataset
     * 
     * @param collectionId Unique identifier of the collection
     * @param datasetId    Unique identifier of the dataset
     * @param bbox         Bounding box of the items
     * @param datetime     Date and time of the items
     * @return String[][] corresponding to the list of the items of the dataset
     */
    @GetMapping("/collections/{collectionId}/items/{datasetId}")
    public String datasetItems(@PathVariable String collectionId, @PathVariable String datasetId,
            @RequestParam(value = "bbox", required = false) String bbox,
            @RequestParam(value = "datetime", required = false) String datetime) {
        System.out.println("***********" + "/collections/" + collectionId + "/items/" + datasetId + "***********");
        DatasetDownlift dd = new DatasetDownlift(datasetId);
        JSONObject data = dd.getData();

        String res = new StorageProperties().getLocation() + "/" + UUID.randomUUID().toString() + ".json";
        try (FileWriter file = new FileWriter(res)) {
            file.write(data.toString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Set the appropriate Content-Type header based on the file's MIME type
        Path path = new File(res).toPath();
        // Set the Content-Disposition header to prompt the user to download the file
        String result = path.getFileName().toString();
        // headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + res);
        System.out.println("results: " + result);
        return "/files/" + result;
    }

    /**
     * Return the conformance
     * 
     * @return String[][] corresponding to the conformance
     */
    @GetMapping("/conformance")
    public String conformance() {
        String results = "{\"head\":{\"vars\":\n";
        results += "[\"Feature\", \"URL\"]},\"results\":{\"bindings\":[\n";
        results += "{\"Feature\": {\"value\": \"Core of OGC API Records 1.0\"},\"URL\": {\"value\": \"http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/record-core\"}},\n";
        results += "{\"Feature\": {\"value\": \"JSON of OGC API Records 1.0\"},\"URL\": {\"value\": \"http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/json\"}},\n";
        results += "{\"Feature\": {\"value\": \"HTML of OGC API Records 1.0\"}, \"URL\": {\"value\": \"http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/html\"}}\n";
        results += "]}}";
        System.out.println(results);

        return results;
    }
}