# Sistema de Gestão de Estacionamento

Este é um sistema de gerenciamento de estacionamento desenvolvido em **Java** e **MySQL**. Ele permite o cadastro, consulta, edição e remoção de veículos, além de um sistema de login para autenticação de usuários.
O sistema foi desenvolvido para facilitar o controle de entrada e saída de veículos em um estacionamento.

## Funcionalidades

- **Cadastro de veículos**: Permite registrar novos veículos no estacionamento, informando o modelo, a placa e a cor.
- **Consulta de veículos**: Exibe uma lista dos veículos registrados no estacionamento, com possibilidade de buscar por modelo, placa ou cor.
- **Edição de veículos**: Permite editar os dados de um veículo já cadastrado.
- **Remoção de veículos**: Permite remover um veículo da lista.
- **Login de usuários**: Sistema de login com autenticação para acessar a tela de gerenciamento.

## Executar Projeto

- **Sem banco de dados**: Só abrir a pasta **executavel_sem_db** e abrir o executavel **.jar**. 
- **Com banco de dados**: É um pouco mais complexo pois eu utilizei um banco de dados **MySql** local, ou seja, você vai ter que acessar a pasta **codigo-fonte** depois a **mysql-criacao**
  nela terá o script sql de criação do banco de dados, lembre-se de quando for cria-lo no **MySql Workbench** não colocar usuario nem senha, depois disso é abrir a pasta **executavel_db**
  e executar o **.jar**. OBS: Na tela de login o email predefinido é o "usuario@gmail" e a senha é "senha123" eles são definidos no script sql caso queira altera-lo.

