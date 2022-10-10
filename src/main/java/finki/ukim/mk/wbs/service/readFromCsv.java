package finki.ukim.mk.wbs.service;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;

import java.io.*;

public class readFromCsv {
    public static String searchCsvLine(String searchString) throws IOException {
        String resultRow = null;
//           BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\velic\\Desktop\\testcsv\\src\\final2.csv"));
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Fakultet__________JAVA\\WBS_project\\final2.csv"))));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(",");
            if(values[2].startsWith(searchString)) {
                resultRow = line;
                return resultRow;
            }
        }
        br.close();
        return resultRow;
    }
    public static void addMovieProperties(String movieName, Resource resourceMovie) throws IOException {
        String []rez=searchCsvLine(movieName).split(",");
        resourceMovie.addProperty(new PropertyImpl("http://dbpedia.org/ontology/genre"),rez[8]);
        resourceMovie.addProperty(new PropertyImpl("http://schema.org/URL"),"https://www.imdb.com/title/"+rez[0]);

    }


}
