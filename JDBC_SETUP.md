# Configuração do MySQL Connector/J

Para que o projeto funcione com JDBC e MySQL, é necessário adicionar o driver JDBC do MySQL ao classpath do projeto.

## Passos para configurar:

### 1. Baixar o MySQL Connector/J
- Acesse: https://dev.mysql.com/downloads/connector/j/
- Baixe a versão mais recente do MySQL Connector/J (arquivo .zip)
- Extraia o arquivo .zip e encontre o arquivo `mysql-connector-j-<versão>.jar`

### 2. Adicionar ao classpath no Eclipse
1. Clique com o botão direito no projeto → Properties
2. Selecione "Java Build Path"
3. Vá para a aba "Libraries"
4. Clique em "Add External JARs..."
5. Navegue até o local onde você extraiu o connector e selecione o arquivo `.jar`
6. Clique em "Apply" e depois em "OK"

### 3. Adicionar ao classpath no IntelliJ IDEA
1. Clique com o botão direito no projeto → Open Module Settings
2. Vá para "Libraries"
3. Clique no "+" → "Java"
4. Navegue até o arquivo `.jar` do connector
5. Clique em "OK"

### 4. Adicionar ao classpath via linha de comando
Se compilar via linha de comando, adicione o JAR ao classpath:
```
javac -cp .;mysql-connector-j-8.0.33.jar src/**/*.java
java -cp .;mysql-connector-j-8.0.33.jar controller.CtrlPrograma
```

## Antes de executar o projeto

1. Certifique-se de que o MySQL está rodando no XAMPP
2. Crie o banco de dados `Campo_minado` no MySQL:
   ```sql
   CREATE DATABASE Campo_minado;
   ```
3. Execute a classe `DaoCriacaoTabela` para criar as tabelas:
   - Execute o método `main` da classe `model.dao.DaoCriacaoTabela`
   - Isso criará as tabelas `usuario` e `partida` automaticamente

## Estrutura do banco de dados

### Tabela usuario
- id (INT, AUTO_INCREMENT, PRIMARY KEY)
- nome (VARCHAR(30))
- login (VARCHAR(50))
- senha_hash (VARCHAR(64))
- total_partidas (INT)
- vitorias (INT)
- derrotas (INT)
- melhor_tempo_facil (INT)
- melhor_tempo_medio (INT)
- melhor_tempo_dificil (INT)

### Tabela partida
- id (INT, AUTO_INCREMENT, PRIMARY KEY)
- usuario_id (INT, FOREIGN KEY REFERENCES usuario(id))
- modo (VARCHAR(5)) - valores: "8x8", "12x12", "16x16"
- tempo (INT)
- resultado (CHAR(7)) - valores: "Vitoria" ou "Derrota"
- data_partida (DATE)
