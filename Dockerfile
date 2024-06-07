 # Utiliza o Maven na versão 3.8.2 para executar o comando mvn clean install -X -DskipTests a seguir.
FROM maven:3.8.2

# Copia todo o conteúdo do local onde está o arquivo Dockerfile (ou seja, na raiz do projeto, que por sua vez, contém
# todos os arquivos e recursos do projeto) para a pasta raiz da imagem do container do Docker que será criado.
# O primeiro ponto final do comando COPY corresponde ao local onde está o arquivo Dockerfile e o
# segundo ponto final do comando COPY corresponde à pasta raiz da imagem do container do Docker que será criado.
COPY . .

# Cria um arquivo executável para a aplicacaofinanceira. Este arquivo executável será salvo na pasta target e se chamará
# aplicacaofinanceira-0.0.1-SNAPSHOT.jar.
# A opção -X adiciona mensagens de log que podem ser úteis para monitorar a execução do comando mvn clean install.
# A opção -DskipTests faz com que os testes automatizados não sejam executados no processo de criação do arquivo
# executável, para que o processo de geração do arquivo executável possa ser feito em menos tempo.
RUN mvn clean install -X -DskipTests

# Determina a versão do JDK do Java (21) que será utilizada para executar a aplicacaofinanceira.
FROM openjdk:21

# Determina a porta onde a aplicacaofinanceira será exposta em um servidor.
EXPOSE 8080

# Adiciona o arquivo executável gerado aplicacaofinanceira-0.0.1-SNAPSHOT.jar (renomeando-o para aplicacaofinanceira.jar)
# à pasta raiz da imagem do container do Docker que será criado.
ADD target/aplicacaofinanceira-0.0.1-SNAPSHOT.jar aplicacaofinanceira.jar

# Corresponde ao comando que será executado para que a aplicacaofinanceira sejá disponibilizada em um servidor.
# O comando, no caso, seria java -jar aplicacaofinanceira.jar .
ENTRYPOINT [ "java", "-jar","aplicacaofinanceira.jar" ]