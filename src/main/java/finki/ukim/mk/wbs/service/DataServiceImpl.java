package finki.ukim.mk.wbs.service;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DataServiceImpl  implements DataService{
    private Model model;
    private Resource resourceActor;

    public void makeModel(String urlPerson){
        this.model=getActorModel(urlPerson);
        this.resourceActor=model.getResource(urlPerson);
    }

    @Override
    public void printModel() {
        model.write(System.out,"TURTLE");
    }

    @Override
    public Actor getActorInfo() {
        Property mAbstract=model.getProperty("http://dbpedia.org/ontology/abstract");
        Property birthName=model.getProperty("http://dbpedia.org/ontology/birthName");
        Property birthDate=model.getProperty("http://dbpedia.org/ontology/birthDate");
        Property thumbnail=model.getProperty("http://dbpedia.org/ontology/thumbnail");
        String actorAbstract;
        if(resourceActor.getProperty(mAbstract)!=null)
            actorAbstract= resourceActor.getProperty(mAbstract).getObject().toString();
        else actorAbstract="Not available actor abstract";
        String name;
        if(resourceActor.getProperty(birthName)!=null)
             name= resourceActor.getProperty(birthName).getObject().toString();
        else name="Not Available actor birth name";
        String date;
        if(resourceActor.getProperty(birthDate)!=null)
            date= resourceActor.getProperty(birthDate).getObject().toString();
        else date="Not Available actor birth date";
        String image;
        if(resourceActor.getProperty(thumbnail)!=null)
            image = resourceActor.getProperty(thumbnail).getObject().toString();
        else image="Not Available actor image";
        List<String> movies=this.getMoviesNames();
        return new Actor(actorAbstract,name,date,image,movies);
    }

    @Override
    public Movie getMovieInfo(String movieUrl,String movie) throws IOException {

        Resource resourceMovie=model.getResource(movieUrl);
        readFromCsv.addMovieProperties(movie,resourceMovie);

        Property mAbstract=model.getProperty("http://dbpedia.org/ontology/abstract");
        Property director=model.getProperty("http://dbpedia.org/ontology/director");
        Property composer=model.getProperty("http://dbpedia.org/ontology/musicComposer");
        Property thumbnail=model.getProperty("http://dbpedia.org/ontology/thumbnail");
        Property movieName=model.getProperty("http://dbpedia.org/property/name");
        Property movieGenre=model.getProperty("http://dbpedia.org/ontology/genre");
        Property MovieImdbUrl=model.getProperty("http://schema.org/URL");
        String movieAbstract;
        if(resourceMovie.getProperty(mAbstract)!=null)
            movieAbstract= resourceMovie.getProperty(mAbstract).getObject().toString();
        else movieAbstract="Not available movie abstract";

        String directorName;
        if(resourceMovie.getProperty(director)!=null)
            directorName= resourceMovie.getProperty(director).getObject().toString();
        else directorName="Not Available movie director";

        String composerUrl;
        if(resourceMovie.getProperty(composer)!=null) {
            composerUrl = resourceMovie.getProperty(composer).getObject().toString();
        }
        else composerUrl="Not Available movie composer";

        String image;
        if(resourceMovie.getProperty(thumbnail)!=null)
            image = resourceMovie.getProperty(thumbnail).getObject().toString();
        else image="Not Available movie image";

        String name;
        if(resourceMovie.getProperty(movieName)!=null) {
            int len=resourceMovie.getProperty(movieName).getObject().toString().length();
            name = resourceMovie.getProperty(movieName).getObject().toString().substring(0,len-3);
        }
        else name="Not Available movie name";

        String genre;
        if(resourceMovie.getProperty(movieGenre)!=null)
            genre = resourceMovie.getProperty(movieGenre).getObject().toString();
        else genre="Not Available movieGenre";

        String imdbUrl;
        if(resourceMovie.getProperty(MovieImdbUrl)!=null)
            imdbUrl = resourceMovie.getProperty(MovieImdbUrl).getObject().toString();
        else imdbUrl="Not Available movieGenre";


        List<String> actors=this.getActorNames(resourceMovie);

        return new Movie(movieAbstract,name,image,composerUrl,directorName,genre,actors,imdbUrl);
    }

    public Composer getComposerInfo(String composerUrl) {
        Resource composerLink = model.getResource(composerUrl);

        Property sameAs = model.getProperty("http://www.w3.org/2002/07/owl#sameAs");
        Resource musicBrainzLink = model.listStatements(
                new SimpleSelector(composerLink, sameAs, (Object) null) {
                    @Override
                    public boolean selects(Statement s) {
                        return s.getObject().toString().startsWith("http://musicbrainz.org/artist/");
                    }
                }).nextStatement().getResource();
        return composerMusicBrainzInfo(musicBrainzLink.toString());
    }

    public Composer composerMusicBrainzInfo(String musicBrainzSameAsLink) {
        Model model = this.getGraph(musicBrainzSameAsLink);
        String composerName = model.listStatements(new SimpleSelector(model.getResource(musicBrainzSameAsLink), model.getProperty("http://schema.org/name"), (Object) null))
                .next().getObject().toString();
        String composerBirthDate = model.listStatements(new SimpleSelector(model.getResource(musicBrainzSameAsLink), model.getProperty("http://schema.org/birthDate"), (Object) null))
                .next().getObject().toString();
        List<String> albums=new ArrayList<>();

        model.listResourcesWithProperty(model.getProperty("http://schema.org/byArtist")).forEach(
                k-> albums.add(k.getProperty(model.getProperty("http://schema.org/name")).getObject().toString()));

        return new Composer(composerName,composerBirthDate,albums);
    }



    public List<String> getMoviesNames() {
        Property starring = this.model.getProperty("http://dbpedia.org/ontology/starring");
        List<String> moviesNames=new ArrayList<>();
        model.listStatements(new SimpleSelector(null, starring, (Object) null))
                .forEachRemaining(current -> {
                    String movieName = current.getSubject().toString().substring(28).replace("_", " ");
                    moviesNames.add(movieName);
                });
        return moviesNames.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getActorNames(Resource movie) {
        Property starring = this.model.getProperty("http://dbpedia.org/ontology/starring");
        List<String> actorNames=new ArrayList<>();
        model.listStatements(new SimpleSelector(movie, starring, (Object) null))
                .forEachRemaining(current -> {
                    String movieName = current.getObject().toString().substring(28).replace("_", " ");
                    actorNames.add(movieName);
                });
        return actorNames;
    }



    private Model getActorModel(String urlPerson) {
        Model personModel = getGraph(urlPerson);
        Model modelWithMoviesAndComposer;

        String SPARQLEndpoint = "https://dbpedia.org/sparql";
        String queryString = "prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "prefix dbr: <http://dbpedia.org/resource/>"
                + "prefix dbo: <http://dbpedia.org/ontology/>"
                + "prefix dbp: <http://dbpedia.org/property/>"
                + "prefix owl: <http://www.w3.org/2002/07/owl#>"
                + "CONSTRUCT "
                + "WHERE {"
                + "?movie dbo:starring <" + urlPerson + ">."
                + "?movie dbo:musicComposer ?composer."
                + "?movie dbo:abstract ?abstract."
                + "?movie dbo:director ?director."
                + "?movie dbo:starring ?actors."
                + "?movie dbo:thumbnail ?thumbnail."
                + "?movie dbp:name ?name."
                + "?composer owl:sameAs ?mB ."
//                + """filter(regex(?mB ,"http://musicbrainz.org*","i" ))"""
//                + "filter(regex(?mB,\"http://musicbrainz.org.artist*\",\"i\"))"
                + "}";
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(SPARQLEndpoint, query)) {
            modelWithMoviesAndComposer = qexec.execConstruct();
        }
        return personModel.union(modelWithMoviesAndComposer);
    }

    private Model getGraph(String urlResource) {
        Model model = ModelFactory.createDefaultModel();

        RDFParser.source(urlResource)
                .httpAccept("application/ld+json")
                .parse(model.getGraph());
        return model;
    }
}
