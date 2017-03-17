# Micro Search Engine
Treino de API de busca e indexação

## Técnologias utilizadas

### Linguagem
+ Java

### Build e environment
+ Java8 (jdk1.8)
+ Tomcat7
+ Maven

### Bibliotécas e APIs
+ Lucene
+ Jersey
+ Gson
+ Junit


## Como usar
Para poder ver a Micro Search Engine em ação é preciso rodar o projeto Maven em um servlet container java, 
de preferencia o Tomcat7 no qual o projeto foi desenvolvido e testado.
Com a aplicação rodando é possível realizar três tipos de operação nela, uma de indexação e duas de busca:

### Indexação
Para indexar entidades é preciso ralizar uma requisição HTTP do tipos POST para a url da aplicação passando
um JSON com o formato `{"title: "foo", "topic": "boo"}` no seu *body*, e setando o **Content-Type** da sua 
*header* para **application/json**.

*e.g* `curl -H "Content-Type: application/json" -X POST 
http://localhost:8090/MySearchEngine/srcheng/entities -d '{"title": "Some title", "type": "TOPIC"}'
`
## Busca com um parametro 
É possível  buscar objetos indexados realizando um requisição HTTP do tipo GET para a url da aplicação passando 
uma **parte** ou o conteúdo **completo** de uma palavra que esteja no *title* de um objeto indexado como 
parametro *q* da url. É importante frisar que se for passado mais de uma palavra, com espaço entre elas, a busca 
não irá funcionar.

*e.g* `curl -XGET 'http://localhost:8090/MySearchEngine/srcheng/entities/?q=som'`

## Busca com dois parametros
Tembém é possível realizar uma busca que filtre os resultados pelos dois elementos de um objeto indexado
passando um parametro *entity_type* adicional à url contendo a palavra **completa** do *topic* desse objeto.
Nesse caso o segundo paremtro não aceita uma fração da palavra como o primeiro.

*e.g* `curl -XGET 'http://localhost:8090/MySearchEngine/srcheng/entities/?q=som&entity_type=topic'`

**PS:** Foi usado o *curl* como exemplo de uso, porém durante o desenvolvimento a aplicação foi testada
usando a ferramenta [Postman](https://www.getpostman.com/) da Google e metodos ajax por JavaScript, portanto 
essas operações são totalmente funções contanto que sejam **respeitadas** as instruções de uso, como o 
*Content-Type* e o formato do JSON na requisição POST e estrutura dos parametros nas requsições GET.

## Implementação
A implementação da API foi feita utilizando a biblioteca de busca Lucene para o motor de busca e JAX-RS
implementando Jersey para manter o webservice. A API foi construída em torno de com quatro classes principais 
uma para manter os dados, uma para indexação, uma para busca, e uma para disponibilizar os endpoints. Também 
foi criada uma classe para definir o modelo do objeto persistido, um Enum para encapsulamento de constantes,  
uma classe para configuração do webservice e uma classe de testes simples. Irei descorrer um pouco sobre cada 
uma dessas classes abaixo, porém a descrição técnica detalhada de todos os métodos, atributos e construtores estão 
disponiveis no [javadoc](https://github.com/FrFroes/my-search-engine/tree/master/MySearchEngine/doc) da API.

### Principais

#### Persistencia de dados - MemoryIndex
Está classe serve para manter os dados indexados em memória durante o tempo de execução da aplicação.
Uma classe com um método e um atributo estático que a torna acessivel para as outras classes. É também 
um Singleton que faz com que ela se mantenha "viva" durante todo o tempo de execução da aplicação mesmo 
estando em um webservice.

#### Indexação - IndexingHandler
Classe que cuida da indexação de Documents, tipo de objeto que a biblioteca Lucene usa para manter dados.
Ela é responsável por pegar o objeto do tipo Entity e a partir dele adicionar campos a um Document para ser 
indexado ao Directory. Ela usa os objetos Field da API Lucene para definir o objeto Document instanciado e 
o indexa no Directory utilizando um IndexWriter, também da API Lucene. O IndexWriter é um objeto que recebe 
uma configuração a partir de um Analyzer, objeto que o Lucene usa para saber quais palavras serão tokenizadas 
e quais serão stopwords.

#### Busca - SearchingHandler
Classe responsável por buscar Documents já indexados no Directory. Esta classe usa um IndexSearcher instaciado a 
partir de um IndexReader, ambos são objetos Lucene que servem para buscar Fields e ler Fields, respectivamente.
Ela pega um ou dois parametros de busca e a partir disso define condicionalmente uma Query. Uma Query na
implementação Lucene é o tipo de objeto usado pelo IndexSearcher para saber quais palavras buscar em um Directory.
Também são usados outros conceitos Lucene como TopDocs e Hits para obter instancias de Documents indexados. A Partir
desses Documents são criadas instanciad e Entity que por fim serão adicionadas a uma ArrayList que pode ser retornada à 
outra classe.

#### Endpoints - EntityWS
Está classe cuida apenas da comunicação ente o client e as classes de indexação e busca. Ela usa a especifição
JAX-RS com a implementação Jersey para definir métodos GET e POST que por sua vez retornam e recebem JSONs, 
respctivamente. Ela usa a Biblioteca Gson do Google para converter JSONs em Entitys e Arraylists de Entitys em 
JSON atraves de reflexão. É a classe que instancia IndexingHandler e IndexingSearcher recebendo e passando dados 
para elas.

### Auxiliares

#### Modelo - Entity
POJO simples com a estrutura base dos objetos indexados.

#### Constantes - FiedsNameEnum
Enum que centraliza os nomes dos Fields usados na criação dos o Documents que serão indexados.

#### WebService - ApplicationClass
Configuração básica de um WebService JAX-RS.

#### Testes - EngineTests
Testes de integração simples para facilita a validação de funcionamento do motor de busca

## Considerações
Concluir esse desáfio foi uma tarefa muito desafiadora e divertida aonde consegui me aprofundar um pouco e entender 
mais a complexidade de como funciona um mecanismo de busca de dados. Aprendi conceitos interessantes como indice invertido
e me familiarizei com a biblioteca Lucene, que eu não conhecia. Concluindo, realizar esse teste agregou muito na minha
experiencia em progrmação já que obtive noções importantissímas que vou levar para o resto da minha carreira.

## Referencias
+ [www.quora.com](https://www.quora.com/Information-Retrieval-What-is-inverted-index)
+ [https://lucene.apache.org/core/](https://lucene.apache.org/core/6_0_1/index.html)
+ http://www.lucenetutorial.com/
+ [http://www.devmedia.com.br/](http://www.devmedia.com.br/alta-performance-na-indexacao-com-apache-lucene-parte-ii/4876)
+ [http://www.tutorialspoint.com/](http://www.tutorialspoint.com/lucene/)
+ http://stackoverflow.com/ <3
