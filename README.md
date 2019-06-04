# Altura de Jogadores de Futebol de um País
Este é um projeto exemplo para o trabalho prático da disciplina de Estrutura de Dados e Programação Orientada a Objetos (semestre 2019/1) da [FACOM/UFMS](http://facom.ufms.br).
Neste projeto,
uma aplicação baseada em [JavaFX](https://openjfx.io/) exibe a lista de países da América do Sul
  (usando a biblioteca [rdf4j](https://rdf4j.eclipse.org/) para acessar a DBpedia)
  e o usuário pode escolher um dos países.
Em seguida,
  a aplicação consulta a altura de todos os jogadores de futebol do país selecionado
    e exibe o jogador mais alto, o mais baixo, a média das alturas e a quantidade de jogadores encontrados.

## Predicados e Consultas SPARQL usados
Esta aplicação realiza duas consultas SPARQL à DBpedia.
A primeira consulta obtém a lista de todos os países da América Latina:
```
SELECT DISTINCT ?pais WHERE {
  ?pais rdf:type dbo:Country .
  ?pais dct:subject dbc:Countries_in_South_America .
}
```
A restrição **?pais rdf:type dbo:Country** indica que o sujeito é um país.
A restrição **?pais dct:subject dbc:Countries_in_South_America** indica que o sujeito é um país da América do Sul.

A segunda consulta obtém os jogadores (e suas alturas) do país selecionado.
```
SELECT DISTINCT ?person ?height WHERE {
  ?person rdf:type dbo:SoccerPlayer .
  ?person dbo:height ?height .
  ?person dbo:birthPlace ?city .
  ?city dbo:country <IRI do país selecionado> .
}
```
A primeia tripla indica que o sujeito **?person** é um jogador de futebol.
A segunda tripla associa a altura (**dbo:height**) do sujeito **?person** à variável **?height**.
A terceira e a quarta triplas associam a variável **?person** ao país escolhido (**<IRI do país selecionado>**).

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
