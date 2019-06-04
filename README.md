# Altura de Jogadores de Futebol de um País
Este é um projeto exemplo para o trabalho prático da disciplina de Estrutura de Dados e Programação Orientada a Objetos (semestre 2019/1) da [FACOM/UFMS](http://facom.ufms.br).
Neste projeto,
uma aplicação baseada em [JavaFX](https://openjfx.io/) exibe a lista de países da América do Sul
  (usando a biblioteca [rdf4j](https://rdf4j.eclipse.org/) para acessar a DBpedia)
  e o usuário pode escolher um dos países.
Em seguida,
  a aplicação consulta a altura de todos os jogadores de futebol do país selecionado
    e exibe o jogador mais alto, o mais baixo, a média das alturas e a quantidade de jogadores encontrados.

## Classes do Projeto
Este projeto inclui duas classes: ``ConsultaDBpedia`` e ``AppJogadorFutebolAltura``.
A classe ``ConsultaDBpedia`` inclui alguns métodos estáticos que realizam consultas à DBpedia 
  usando a biblioeteca rdf4j.
Além destes métodos (que são usados pela aplicação),
  esta classe também inclui um programa (método ``main``) que testa os métodos providos.
A classe ``AppJogadorFutebolAltura`` implementa a aplicação usando JavaFX.
Esta classe depende da primeira classe para realizar as consultas à DBpedia.

## Predicados e Consultas SPARQL usados
A aplicação provida neste projeto realiza duas consultas SPARQL à DBpedia.
A primeira consulta obtém a lista de todos os países da América Latina,
  como pode ser visto abaixo.
```
SELECT DISTINCT ?pais WHERE {
  ?pais rdf:type dbo:Country .
  ?pais dct:subject dbc:Countries_in_South_America .
}
```
A primeira tripla indica que a variável **?pais** deve ser do tipo país (**rdf:type dbo:Country**).
A segunda tripla indica que a variável **?pais** deve ser um país da América do Sul (**dct:subject dbc:Countries_in_South_America**).
Provavelmente,
a primeira tripla é desnecessária,
  pois todo país da América do Sul (**dbc:Countries_in_South_America**) é do tipo país.

A segunda consulta obtém os jogadores (e suas alturas) do país selecionado.
```
SELECT DISTINCT ?person ?height WHERE {
  ?person rdf:type dbo:SoccerPlayer .
  ?person dbo:height ?height .
  ?person dbo:birthPlace ?city .
  ?city dbo:country <IRI do país selecionado> .
}
```
Observe que a consulta seleciona duas variáveis **?person** e **?height** pois,
  além de calcular a altura média,
    também queremos identificar os jogadores mais alto e mais baixo.
A primeia tripla indica que a variável **?person** é um jogador de futebol (**rdf:type dbo:SoccerPlayer**).
A segunda tripla associa a variável **?height** à altura (**dbo:height**) da variável **?person**.
A terceira e a quarta triplas associam a variável **?person** ao país escolhido (**<IRI do país selecionado>**).
Observem no código que para inserir uma IRI completa (sem usar um prefixo) em uma consulta SPARQL,
  como é o caso aqui,
    precisamos envolver esta IRI entre **<** e **>**.

## JavaFX
JavaFX é um padrão (e uma biblioteca) para desenvolvimento de aplicações gráficas em Java.
A história do desenvolvimento de aplicações gráficas em Java é longa e complicada.
Durante toda a história da linguagem Java,
  foram propostas diversas bibliotecas e padrões diferentes.
Nenhuma biblioteca conseguiu se tornar o padrão de fato.
A tentativa mais recente (e talvez mais bem sucedida, por enquanto) é o JavaFX.

Pra quem usa o Java 8 da própria Oracle,
  o JavaFX já vem embutido na JRE, 
    ou seja, não é necessário baixar nem configurar nenhuma biblioteca adicional.
Se você usa o Java 8 da OpenJDK ou mesmo uma versão mais recenete do Java (>8),
  então o JavaFX não vem incluído na JRE nem na JDK.
Neste caso,
é necessário baixar o [OpenJFX](https://openjfx.io/) que,
  desde a versão 11 do Java,
    é a implementação de referência do JavaFX.

## DBpedia
A [DBpedia](http://dbpedia.org/) é uma base de dados pública composta por dados extraídos de artigos da Wikipedia.
Os dados são extraídos automaticamente das [infoboxes](https://en.wikipedia.org/wiki/Help:Infobox) 
  de artigos da Wikipedia
    e armazenados em uma [base de triplas](https://en.wikipedia.org/wiki/Triplestore).
Estas triplas são baseadas no conceito de [ontologia](https://en.wikipedia.org/wiki/Ontology_(information_science)).
Ontologia é algo bem complexo 
  e não está no escopo deste projeto explorar este conceito de maneira aprofundada.
Usaremos a ontologia definida pela DBpedia de uma maneira direta e simples,
  o que não deixará de ser algo desafiador e, ao mesmo tempo, com grande potencial.

## rdf4j
A biblioteca [rdf4j](https://rdf4j.eclipse.org/) provê diversas funcionalidades para gerenciar dados de uma base de triplas
  (ou seja, de uma ontologia).
Neste trabalho,
utilizaremos esta biblioteca para realizar consultas na DBpedia.
As principais consultas que realizaremos são baseadas na linguagem de busca [SPARQL](https://en.wikipedia.org/wiki/SPARQL)
  que é inspirada na linguagem SQL para consulta em base de dados relacional.

## Dependências
A biblioteca rdf4j e suas dependências estão na pasta lib deste projeto.
Estas são as principais dependências.
Entretanto,
como mencionado acima,
  este projeto também depende do JavaFX.
Esta biblioteca não está incluída neste projeto.
Se você usa alguma versão do JDK que não inclua o JavaFX,
  será necessário instalar/baixar esta biblioteca.
