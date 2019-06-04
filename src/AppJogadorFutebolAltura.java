
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.BindingSet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Aplicação exibe lista de países da América do Sul. Quando o usuário seleciona
 * um país, a aplicação consulta as alturas de todos os jogadores de futebol
 * (rdf:type = dbo:SoccerPlayer) deste país e exibe o jogador mais baixo, o mail
 * alto, a altura média e o número de jogadores encontrados.
 * 
 * @author Eraldo R. Fernandes (eraldo@facom.ufms.br)
 *
 */
public class AppJogadorFutebolAltura extends Application {
	/**
	 * Janela principal da aplicação.
	 */
	private Stage janelaPrincipal;

	/**
	 * ComboBox para exibição dos países da América do Sul.
	 */
	private ComboBox<String> cbPaises;

	@Override
	public void start(Stage primaryStage) {
		// Referência para a janela principal.
		janelaPrincipal = primaryStage;

		// Título da janela.
		janelaPrincipal.setTitle("Altura de Jogadores");

		// Botão de busca: chama método buscar() quando for clicado.
		Button btnBuscar = new Button("Buscar altura dos jogadores");
		btnBuscar.setOnMouseClicked(e -> buscar());

		// Controle que exibe a lista de países.
		cbPaises = new ComboBox<>();

		// Obtém lista da combobox.
		List<String> paises = cbPaises.getItems();

		// Obtém lista de países da DBpedia e preenche combobox.
		for (BindingSet bs : ConsultaDBpedia.getPaisesAmericaDoSul())
			paises.add(bs.getValue("pais").toString());

		// Cria container vertical (existem vários outros) e insere os controles.
		VBox container = new VBox(new Label("Selecione um país:"), cbPaises, btnBuscar);

		// Inclui container na janela principal.
		janelaPrincipal.setScene(new Scene(container));

		// Exibe janela principal.
		janelaPrincipal.show();
	}

	/**
	 * Obtém todos os jogadores de futebol do país selecionado pelo usuário e exibe
	 * o jogador mais alto, o mais baixo, a altura média e o número total de
	 * jogadores considerados.
	 */
	private void buscar() {
		// Obtém país selecionado. Este valor é uma IRI completa.
		String pais = cbPaises.getValue();

		// Variáveis para armazenar os resultados da busca.
		double min = Double.MAX_VALUE, max = Double.MIN_NORMAL, avg = 0;
		String sMin = null, sMax = null;

		// Contador de jogadores.
		int count = 0;

		/*
		 * Como o método de busca de países retorna IRIs completos (sem usar os
		 * prefixos), é necessário envolver este valor entre '<' e '>' para inserirmos
		 * ele em uma consulta SPARQL.
		 */
		pais = "<" + pais + ">";

		// Obtém e processa todos os jogadores do país.
		for (BindingSet bs : ConsultaDBpedia.getJogadoresComAlturaNascidosNoPais(pais)) {
			/*
			 * O método usado acima retorna dois valores para cada jogador: o IRI do jogador
			 * e sua altura.
			 * 
			 * Do IRI, usaremos apenas o 'local name' que é aquele nome que aparece depois
			 * do domínio da DPpedia (ou da Wikipedia), por exemplo, 'Lionel_Messi'.
			 * 
			 * A altura é um literal do tipo double. Para converter este valor para um
			 * double Java, precisamos realizar as operações abaixo.
			 */
			String person = ((IRI) bs.getValue("person")).getLocalName();
			double height = ((Literal) bs.getValue("height")).doubleValue();

			/*
			 * Filtramos valores incorretos. Existem alguns jogadores com altura igual a
			 * zero e outros com a altura em centimétros ao invés de metros. Estes valores
			 * deixam os resultaos incorretos. Portanto, vamos desonsiderar valores abaixo
			 * de 1m ou acima de 3m.
			 */
			if (height > 3 || height < 1)
				continue;

			// Computa os valores que queremos exibir.
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

		// Média das alturas.
		avg = avg / count;

		// Cria mensagem com as informações calculadas.
		String msg = "Número de jogadores: %d\nMédia de altura: %f\n";
		msg += "Jogador mais baixo: %s (%f)\n";
		msg += "Jogador mais alto: %s (%f)";
		msg = String.format(msg, count, avg, sMin, min, sMax, max);

		// Exibe mensagem em uma janela popup.
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Pesquisa Concluída");
		alert.setHeaderText(String.format("Dados de %s", pais));
		alert.setContentText(msg);
		alert.showAndWait();
	}

	/**
	 * Lança aplicação.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
