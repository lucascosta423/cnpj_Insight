# CNPJInsight

Uma aplicação web moderna para consulta e exportação de dados de empresas brasileiras baseada em informações do CNPJ.

## 🚀 Funcionalidades

- **Consulta Avançada**: Filtragem por múltiplos critérios como CNAEs, naturezas jurídicas, estados, municípios
- **Exportação de Dados**: Geração de relatórios em diferentes formatos
- **Interface Responsiva**: Design moderno com Bootstrap 5
- **Filtros Inteligentes**: Sistema de seleção múltipla com Select2
- **Notificações**: Sistema de feedback visual para o usuário

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem principal
- **Spring Boot** - Framework principal
- **Spring MVC** - Para controladores web
- **Spring Data JPA** - Para acesso a dados
- **Jakarta EE** - Especificações empresariais

### Frontend
- **Thymeleaf** - Template engine
- **Bootstrap 5** - Framework CSS
- **Select2** - Plugin para seleções múltiplas
- **Bootstrap Icons** - Ícones
- **JavaScript (ES6+)** - Funcionalidades interativas

### Banco de Dados
- Configurado para trabalhar com bancos relacionais via JPA

## 📋 Pré-requisitos

- Java 21 ou superior
- Maven 3.8+ 
- Banco de dados compatível com JPA (MySQL, PostgreSQL, etc.)

## 🔧 Instalação

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/CNPJInsight.git
   cd CNPJInsight
   ```

2. **Configure o banco de dados**
   - Edite o arquivo `src/main/resources/application.properties`
   - Configure as credenciais do banco de dados

3. **Execute a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Ou no Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

4. **Acesse a aplicação**
   - Abra o navegador em `http://localhost:8080`

## 🎯 Como Usar

1. **Acesse a página principal**
2. **Configure os filtros desejados:**
   - CNAEs (Códigos de Atividade Econômica)
   - Naturezas Jurídicas
   - Estados e Municípios
   - Situação Cadastral
   - MEI (Microempreendedor Individual)
3. **Defina a quantidade de registros**
4. **Clique em "Exportar Dados"**
5. **Aguarde o processamento e download**

## 📁 Estrutura do Projeto

```aiignore
CNPJInsight/
├── .idea/                          # Configurações do IntelliJ IDEA
├── .mvn/                           # Maven Wrapper
├── exports/                        # Diretório para arquivos exportados
├── src/                           # Código fonte
│   ├── main/
│   │   ├── java/                  # Classes Java
│   │   └── resources/             # Recursos da aplicação
│   │       ├── static/            # Arquivos estáticos (CSS, JS, imagens)
│   │       └── templates/         # Templates Thymeleaf
│   └── test/                      # Testes unitários
├── target/                        # Arquivos compilados (gerado pelo Maven)
├── .gitattributes                 # Configurações do Git
├── .gitignore                     # Arquivos ignorados pelo Git
├── README.md                      # Documentação do projeto
├── DeploymentGuide.md             # Guia de deploy
├── HELP.md                        # Arquivo de ajuda
├── funcition                      # Arquivo de configuração
├── mvnw                          # Maven Wrapper (Unix/Linux)
├── mvnw.cmd                      # Maven Wrapper (Windows)
└── pom.xml                       # Configurações do Maven
```


### Principais Diretórios

- **`src/main/java/`**: Contém todo o código Java da aplicação (controllers, services, entities, etc.)
- **`src/main/resources/`**: Recursos da aplicação incluindo:
   - `templates/`: Templates HTML com Thymeleaf
   - `static/`: Arquivos CSS, JavaScript e imagens
   - `application.properties`: Configurações da aplicação
- **`exports/`**: Diretório onde são salvos os arquivos de dados exportados
- **`target/`**: Diretório gerado pelo Maven com os arquivos compilados
