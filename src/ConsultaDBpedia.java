import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

/**
 * Provê métodos de consulta à DBpedia usando SPARQL usando a biblioeteca rdf4j.
 * 
 * @author Eraldo R. Fernandes (eraldo@facom.ufms.br)
 *
 */
public class ConsultaDBpedia {

	/**
	 * Retorna string com lista de prefixos comuns.
	 * 
	 * @return
	 */
	private static String getPrefixes() {
		String prefixes = "";
		prefixes += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
		prefixes += "PREFIX rdf: <" + RDF.NAMESPACE + "> \n";
		prefixes += "PREFIX rdfs: <" + RDFS.NAMESPACE + "> \n";
		prefixes += "PREFIX dbo: <http://dbpedia.org/ontology/> \n";
		prefixes += "PREFIX dbr: <http://dbpedia.org/resource/> \n";
		prefixes += "PREFIX dct: <http://purl.org/dc/terms/> \n";
		prefixes += "PREFIX dbc: <http://dbpedia.org/resource/Category:> \n";
		return prefixes;
	}

	/**
	 * Retorna lista de jogadores nascidos em Campo Grande.
	 * 
	 * @return
	 */
	public static List<BindingSet> getJogadoresNascidosEmCampoGrande() {
		Repository repo = new SPARQLRepository("http://dbpedia.org/sparql");
		repo.init();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = getPrefixes();

			queryString += "SELECT DISTINCT ?person WHERE {";
			queryString += "	?person rdf:type dbo:SoccerPlayer .";
			queryString += "	?person dbo:birthPlace dbr:Campo_Grande .";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
				return QueryResults.asList(result);
			}
		} finally {
			repo.shutDown();
		}
	}

	/**
	 * Retorna lista de jogadores nascidos em Mato Grosso do Sul.
	 * 
	 * @return
	 */
	public static List<BindingSet> getJogadoresNascidosEmMS() {
		Repository repo = new SPARQLRepository("http://dbpedia.org/sparql");
		repo.init();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = getPrefixes();

			queryString += "SELECT DISTINCT ?person WHERE {";
			queryString += "	?person rdf:type dbo:SoccerPlayer .";
			queryString += "	?person dbo:birthPlace ?city .";
			queryString += "	?city dbo:isPartOf dbr:Mato_Grosso_do_Sul .";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
				return QueryResults.asList(result);
			}
		} finally {
			repo.shutDown();
		}
	}

	/**
	 * Retorna lista de jogadores nascidos no país dado.
	 * 
	 * @param pais
	 * @return
	 */
	public static List<BindingSet> getJogadoresNascidosNoPais(String pais) {
		Repository repo = new SPARQLRepository("http://dbpedia.org/sparql");
		repo.init();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = getPrefixes();

			queryString += "SELECT DISTINCT ?person WHERE {";
			queryString += "	?person rdf:type dbo:SoccerPlayer .";
			queryString += "	?person dbo:birthPlace ?city .";
			queryString += "	?city dbo:country " + pais + " .";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
				return QueryResults.asList(result);
			}
		} finally {
			repo.shutDown();
		}
	}

	/**
	 * Obtém altura do jogador dado.
	 * 
	 * @param jogador
	 * @return
	 */
	public static double getAlturaJogador(IRI jogador) {
		Repository repo = new SPARQLRepository("http://dbpedia.org/sparql");
		repo.init();

		try (RepositoryConnection conn = repo.getConnection()) {
			ValueFactory vf = repo.getValueFactory();
			RepositoryResult<Statement> rr = conn.getStatements(jogador,
					vf.createIRI("http://dbpedia.org/ontology/", "height"), null);
			if (rr.hasNext())
				return ((Literal) rr.next().getObject()).doubleValue();
			return Double.NaN;
		} finally {
			repo.shutDown();
		}
	}

	/**
	 * Retorna lista de jogadores, juntamente com suas alturas, nascidos no país
	 * dado.
	 * 
	 * @param pais
	 * @return
	 */
	public static List<BindingSet> getJogadoresComAlturaNascidosNoPais(String pais) {
		Repository repo = new SPARQLRepository("http://dbpedia.org/sparql");
		repo.init();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = getPrefixes();

			queryString += "SELECT DISTINCT ?person ?height WHERE {";
			queryString += "	?person rdf:type dbo:SoccerPlayer .";
			queryString += "	?person dbo:birthPlace ?city .";
			queryString += "	?person dbo:height ?height .";
			queryString += "	?city dbo:country " + pais + " .";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
				return QueryResults.asList(result);
			}
		} finally {
			repo.shutDown();
		}
	}

	/**
	 * Retorna lista de países da América do Sul.
	 * 
	 * @return
	 */
	public static List<BindingSet> getPaisesAmericaDoSul() {
		Repository repo = new SPARQLRepository("http://dbpedia.org/sparql");
		repo.init();

		try (RepositoryConnection conn = repo.getConnection()) {
			String queryString = getPrefixes();

			queryString += "SELECT DISTINCT ?pais WHERE {";
			queryString += "	?pais rdf:type dbo:Country .";
			queryString += "	?pais dct:subject dbc:Countries_in_South_America .";
			queryString += "}";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
				return QueryResults.asList(result);
			}
		} finally {
			repo.shutDown();
		}
	}

	/**
	 * Testa os métodos da classe.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		for (BindingSet bs : getJogadoresNascidosEmCampoGrande())
			System.out.println(bs.getValue("person"));

		for (BindingSet bs : getJogadoresNascidosEmMS())
			System.out.println(bs.getValue("person"));

		for (BindingSet bs : getJogadoresNascidosNoPais("dbr:Brazil"))
			System.out.println(bs.getValue("person"));

		for (BindingSet bs : getJogadoresNascidosNoPais("dbr:Brazil")) {
			IRI person = (IRI) bs.getValue("person");
			System.out.println(person.getLocalName() + " : " + getAlturaJogador(person));
		}

		for (BindingSet bs : getJogadoresComAlturaNascidosNoPais("dbr:Brazil")) {
			IRI person = (IRI) bs.getValue("person");
			double height = ((Literal) bs.getValue("height")).doubleValue();
			System.out.println(person.getLocalName() + " : " + height);
		}

		for (BindingSet bsPais : getPaisesAmericaDoSul()) {
			double min = Double.MAX_VALUE, max = Double.MIN_NORMAL, avg = 0;
			String sMin = null, sMax = null;
			int count = 0;
			for (BindingSet bs : getJogadoresComAlturaNascidosNoPais("<" + bsPais.getValue("pais") + ">")) {
				String person = ((IRI) bs.getValue("person")).getLocalName();
				double height = ((Literal) bs.getValue("height")).doubleValue();
				if (height > 3 || height < 1)
					continue;
				avg += height;
				if (height < min) {
					min = height;
					sMin = person;
				}
				if (height > max) {
					max = height;
					sMax = person;
				}
				++count;
			}

			avg = avg / count;

			System.out.printf("%s: %f/%s (min) %f (avg) %f/%s (max) %d (count)\n", bsPais.getValue("pais"), min, sMin,
					avg, max, sMax, count);
		}
	}
}
