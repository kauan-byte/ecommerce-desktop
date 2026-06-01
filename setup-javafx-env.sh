#!/bin/bash

set -euo pipefail

echo "=================================================="
echo " AJUSTANDO O RELÓGIO DO PC"
echo "=================================================="


echo "Sincronizando relógio..."

sudo timedatectl set-ntp true

sleep 10

timedatectl status

date

# ==================================================
# REMOÇÃO COMPLETA DE AMBIENTES ANTIGOS
# ==================================================

echo "=================================================="
echo " REMOVENDO INSTALAÇÕES ANTIGAS"
echo "=================================================="

# --------------------------------------------------
# PARAR SERVIÇOS
# --------------------------------------------------

sudo systemctl stop mysql || true

# --------------------------------------------------
# REMOVER PACOTES
# --------------------------------------------------

echo "Removendo pacotes antigos..."

sudo apt purge -y \
    openjdk-* \
    default-jdk \
    maven \
    mysql-server \
    mysql-client \
    mysql-common \
    mysql-workbench* \
    netbeans* \
    scenebuilder* \
    openjfx* \
    libopenjfx* \
    default-mysql-server \
    default-mysql-client || true

# --------------------------------------------------
# REMOVER DEPENDÊNCIAS ÓRFÃS
# --------------------------------------------------

sudo apt autoremove -y
sudo apt autoclean -y

# --------------------------------------------------
# REMOVER DIRETÓRIOS EM /OPT
# --------------------------------------------------

echo "Removendo diretórios antigos..."

sudo rm -rf /opt/netbeans*
sudo rm -rf /opt/javafx*
sudo rm -rf /opt/scenebuilder*
sudo rm -rf /opt/SceneBuilder*

# --------------------------------------------------
# REMOVER CONFIGURAÇÕES DO USUÁRIO
# --------------------------------------------------

echo "Removendo configurações antigas..."

rm -rf ~/.netbeans
rm -rf ~/.m2
rm -rf ~/SceneBuilder-Ikonli
rm -rf ~/Projetos/JavaFXTemplate

# --------------------------------------------------
# REMOVER MYSQL COMPLETAMENTE
# --------------------------------------------------

echo "Removendo dados antigos do MySQL..."

sudo apt purge -y mysql-server mysql-client mysql-common
sudo apt autoremove -y

sudo rm -rf /etc/mysql
sudo rm -rf /var/lib/mysql
sudo rm -rf /var/log/mysql

# --------------------------------------------------
# REMOVER ARQUIVOS TEMPORÁRIOS
# --------------------------------------------------

rm -rf ~/Downloads/javafx-setup

# --------------------------------------------------
# ATUALIZAR REPOSITÓRIOS
# --------------------------------------------------

sudo apt update

echo "=================================================="
echo " LIMPEZA FINALIZADA"
echo "=================================================="

echo ""

echo "=================================================="
echo " INSTALANDO WGET CURL UNZIP TAR GIT"
echo "=================================================="

sudo apt install -y wget curl unzip tar git


echo "=================================================="
echo " INSTALANDO AMBIENTE JAVA + JAVAFX COMPLETO"
echo "=================================================="

# ==================================================
# ATUALIZAÇÃO DO SISTEMA
# ==================================================

sudo apt update && sudo apt upgrade -y

# ==================================================
# INSTALAR OPENJDK MAIS RECENTE
# ==================================================

echo "Instalando OpenJDK..."

sudo apt install -y openjdk-25-jdk

java -version

# ==================================================
# INSTALAR MAVEN
# ==================================================

echo "Instalando Maven..."

sudo apt install -y maven

mvn -version

# ==================================================
# INSTALAR MYSQL SERVER + WORKBENCH
# ==================================================


echo "Instalando MySQL Server..."

sudo apt update

sudo apt install -y mysql-server

sleep 10
sudo systemctl enable mysql
sudo systemctl restart mysql

echo "Configurando autenticação do MySQL..."

# Aguarda inicialização completa


# Configura senha do root e autenticação por senha
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'ifsuldeminas'; FLUSH PRIVILEGES;"

echo "Testando acesso MySQL..."

mysql -u root -p'ifsuldeminas' -e "SELECT VERSION();"

echo "MySQL configurado com sucesso!"


echo "Baixando MySQL Workbench..."

# ==================================================
# CRIAR DIRETORIO TEMPORARIO
# ==================================================

mkdir -p ~/Downloads/javafx-setup
cd ~/Downloads/javafx-setup

WORKBENCH_URL="https://dev.mysql.com/get/Downloads/MySQLGUITools/mysql-workbench-community_8.0.47-1ubuntu24.04_amd64.deb"

wget -O mysql-workbench.deb $WORKBENCH_URL

echo "Instalando dependências..."

sudo apt install -y libatk1.0-0 libgtk-3-0 libcairo2 libpango-1.0-0 \
libzip4 libsecret-1-0 libnss3

echo "Instalando MySQL Workbench..."

sudo dpkg -i mysql-workbench.deb || sudo apt --fix-broken install -y

echo "MySQL Workbench instalado!"

# ==================================================
# BAIXAR NETBEANS MAIS RECENTE
# ==================================================

echo "Baixando NetBeans..."

NETBEANS_URL="https://github.com/codelerity/netbeans-packages/releases/download/v29-build1/apache-netbeans_29-1_amd64.deb"

wget -O netbeans.deb $NETBEANS_URL

echo "Instalando NetBeans..."

sudo dpkg -i netbeans.deb || sudo apt --fix-broken install -y

# ==================================================
# CRIAR ATALHO NETBEANS
# ==================================================

echo "Criando atalho do NetBeans..."

sudo tee /usr/share/applications/netbeans.desktop > /dev/null <<EOF
[Desktop Entry]
Version=1.0
Type=Application
Name=NetBeans
Exec=/usr/bin/netbeans
Icon=/usr/share/pixmaps/netbeans.png
Categories=Development;IDE;
Terminal=false
EOF

# ==================================================
# BAIXAR JAVAFX SDK (AZUL)
# ==================================================

echo "Baixando JavaFX SDK..."

JAVAFX_URL="https://cdn.azul.com/zulu/bin/zulu25.34.17-ca-jdk25.0.3-linux_x64.tar.gz"

wget -O javafx.tar.gz $JAVAFX_URL

tar -xzf javafx.tar.gz

JAVAFX_DIR=$(find . -maxdepth 1 -type d -name "zulu*" | head -n 1)

sudo mv $JAVAFX_DIR /opt/javafx-sdk

# ==================================================
# BAIXAR SCENE BUILDER
# ==================================================

echo "Baixando Scene Builder..."

SCENE_BUILDER_URL="https://download2.gluonhq.com/scenebuilder/26.0.0/install/linux/SceneBuilder-26.0.0.deb"

wget -O scenebuilder.deb $SCENE_BUILDER_URL

echo "Instalando Scene Builder..."

sudo dpkg -i scenebuilder.deb || sudo apt --fix-broken install -y

# ==================================================
# CONFIGURAR SCENE BUILDER NO NETBEANS
# ==================================================

echo "Configurando integração Scene Builder..."

mkdir -p ~/.netbeans/26/config/Preferences/org/netbeans/modules/javafx2/scenebuilder

cat > ~/.netbeans/26/config/Preferences/org/netbeans/modules/javafx2/scenebuilder/prefs.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties version="1.0">
<entry key="selectedHome">/opt/SceneBuilder</entry>
</properties>
EOF

# ==================================================
# CRIAR TEMPLATE MAVEN JAVAFX
# ==================================================

echo "Criando template Maven JavaFX..."

mkdir -p ~/Projetos/JavaFXTemplate/src/main/java/com/example
mkdir -p ~/Projetos/JavaFXTemplate/src/main/resources/com/example

cd ~/Projetos/JavaFXTemplate

# ==================================================
# POM.XML
# ==================================================

cat > pom.xml <<EOF
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>JavaFXTemplate</artifactId>
    <version>1.0</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <javafx.version>25</javafx.version>
    </properties>

    <dependencies>

        <!-- JavaFX -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>\${javafx.version}</version>
        </dependency>

        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>\${javafx.version}</version>
        </dependency>

        <!-- Ikonli -->
        <dependency>
            <groupId>org.kordamp.ikonli</groupId>
            <artifactId>ikonli-javafx</artifactId>
            <version>12.3.1</version>
        </dependency>

        <dependency>
            <groupId>org.kordamp.ikonli</groupId>
            <artifactId>ikonli-fontawesome5-pack</artifactId>
            <version>12.3.1</version>
        </dependency>

        <!-- MYSQL DRIVER -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.3.0</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>

            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>

                <configuration>
                    <mainClass>com.example.App</mainClass>
                </configuration>
            </plugin>

        </plugins>
    </build>

</project>
EOF

# ==================================================
# APP.JAVA
# ==================================================

cat > src/main/java/com/example/App.java <<EOF
package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();

        root.getChildren().add(
            new Label("JavaFX + Ikonli funcionando!")
        );

        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.setTitle("JavaFX Template");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
EOF

# ==================================================
# FORÇAR DOWNLOAD DAS DEPENDENCIAS MAVEN
# ==================================================

echo "Baixando dependências Maven..."

mvn clean install

# ==================================================
# COPIAR JARS IKONLI PARA PASTA FACIL
# ==================================================

#echo "Preparando JARs do Ikonli..."

#mkdir -p ~/SceneBuilder-Ikonli

#cp ~/.m2/repository/org/kordamp/ikonli/ikonli-core/12.3.1/*.jar ~/SceneBuilder-Ikonli/
#cp ~/.m2/repository/org/kordamp/ikonli/ikonli-javafx/12.3.1/*.jar ~/SceneBuilder-Ikonli/
#cp ~/.m2/repository/org/kordamp/ikonli/ikonli-fontawesome5-pack/12.3.1/*.jar ~/SceneBuilder-Ikonli/

echo "=================================================="
echo " INSTALAÇÃO FINALIZADA!"
echo "=================================================="

java -version
mvn -version

echo ""
echo "Próximos passos:"
echo ""
echo "1) Abrir o Scene Builder"
echo "2) Ir em:"
echo "   Library -> JAR/FXML Manager"
echo ""
echo "3) Adicionar os JARs das 3 pastas:"
echo "   ~/.m2/repository/org/kordamp/ikonli"
echo ""
echo "4) Reiniciar o Scene Builder"
echo ""
echo "5) Os componentes Ikonli estarão disponíveis"
echo ""