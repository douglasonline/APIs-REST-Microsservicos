# APIs REST Microsserviços

# Sobre o projeto

 Esse projeto spring contém microsserviços que se comunicação entre si

# Descrição do Projeto
## Microsserviço de Usuário (User): Lida com operações relacionadas ao cadastro e autenticação de usuários.

- Deve-se cadastrar um usuário.
- Deve-se Logar no sistema para gerar o Token.

## Microsserviço de Produto (Product): Responsável por gerenciar operações relacionadas a produtos, como adição, remoção, atualização e busca de produtos.

- O Microsserviço de produto (Product), já está setado com produtos

## Microsserviço de Carrinho de Compras (Shopping_Cart): Responsável por gerenciar o carrinho de compras dos usuários, permitindo adição e remoção de produtos. 

- Para a criação de um item no carrinho de compras e necessário que o usuário esteja logado no Microsserviço de usuário (User), para poder passar o Token é que o produto exista no Microsserviço de Produto (Product).

- Para remover um produto de um item do carrinho de compras e necessário que o usuário esteja logado no Microsserviço de usuário (User), para poder passar o Token após isso tem que passar o id de qual dos itens no carrinho de compras você quer remover um produto e depois passar o id do produto.  

- Para ver todos os itens no carrinho de compras não e necessário que o usuário esteja logado no Microsserviço de usuário (User).

## Microsserviço de Pagamento (Payment): Gerencia o processo de pagamento. 

- Para realizar a simulação de um pagamento e necessário que o usuário esteja logado no Microsserviço de usuário (User), para poder passar o Token e também e necessário passar o id do item que está no carrinho de compras por que o Microsserviço de pagamento (Payment), faz a comunicação com o Microsserviço de Carrinho de compras (Shopping_Cart), para poder finalizar a simulação do pagamento. 

- Faz a comunicação com RabbitMQ e manda uma confirmação para a fila.

## Microsserviço de Notificação (Notification): Responsável por gerenciar e enviar notificações para usuários

- Ler a fila do RabbitMQ e notifica o Usuario no Gmail  

# Tecnologias utilizadas
## Back end
- Java
- Maven 
- Spring Boot
- RabbitMQ

## Front end
- Swagger

## Banco de Dados Utilizado
- H2

# Como executar o projeto

## Back end
Pré-requisitos: Java, Maven, RabbitMQ

```bash
# Clonar repositório

# Executar primeiro o Microsserviço de Usuário que está na pasta User

# Executar o Microsserviço de produto que está na pasta Product

# Executar o Microsserviço de Carrinho de Compras que está na pasta Shopping_Cart

# Executar o Microsserviço de Pagamento que está na pasta Payment

# Executar o Microsserviço de Notificação que está na pasta Notification

```

# Como acessar o Swagger nos microsserviços
## Para acessar o Swagger do Microsserviço de Usuário após executar o projeto acesse o endereço 
- http://localhost:8082/swagger-ui/index.html#/

![Microsservico de Usuario Swagger](https://github.com/douglasonline/Imagens/blob/master/Microsservico_de_Usuario_Swagger.png) 

## Para acessar o Swagger do Microsserviço de Produto após executar o projeto acesse o endereço 
- http://localhost:8080/swagger-ui/index.html#/

![Microsservico de Produto Swagger Parte1](https://github.com/douglasonline/Imagens/blob/master/Microsservico_de_Produto_Swagger_Parte1.png)

![Microsservico de Produto Swagger Parte2](https://github.com/douglasonline/Imagens/blob/master/Microsservico_de_Produto_Swagger_Parte2.png)

## Para acessar o Swagger do Microsserviço de Carrinho de Compras após executar o projeto acesse o endereço 
- http://localhost:8082/swagger-ui/index.html#/

![Microsservico de Carrinho de Compras Swagger](https://github.com/douglasonline/Imagens/blob/master/Microsservico_de_Carrinho_de_Compras_Swagger.png) 

## Para acessar o Swagger do Microsserviço de Pagamento após executar o projeto acesse o endereço 
- http://localhost:8082/swagger-ui/index.html#/

![Microsservico de Pagamento Swagger](https://github.com/douglasonline/Imagens/blob/master/Microsservico_de_Pagamento_Swagger.png)  

# Como consumir o projeto

Estou utilizando o Postman para consumir a aplicação

- Primeiro deve-se cadastra um Usuário no Microsserviço de Usuário (User)
Utilizando o endereço
- http://localhost:8082/api/user/users

![Cadastrando Usuario](https://github.com/douglasonline/Imagens/blob/master/Cadastrando_Usuario.png)

- Após o cadastra de Usuário deve-se fazer login para poder obter o Token de acesso
Utilizando o endereço
- http://localhost:8082/api/user/login

![Login do Usuario](https://github.com/douglasonline/Imagens/blob/master/Login_do_Usuario.png)

- Podemos buscar todos os produtos no Microsserviço de Produto (Product)
Utilizando o endereço
- http://localhost:8080/product/all     

![Buscando Todos os Produtos](https://github.com/douglasonline/Imagens/blob/master/Buscando_Todos_os_Produtos.png)

- Para podermos cadastra um item no Microsserviço de Carrinho de Compras (Shopping_Cart), o produto desse item tem que existir no Microsserviço de Produto (Product) então o productId tem que ser um id valido é o Usuário deve-se está autenticado
Podemos adicionar um item no Microsserviço de Carrinho de Compras (Shopping_Cart) utilizando o endereço
- http://localhost:8081/shopping-cart/addItem  

![Autenticacao do Carrinho de Compras](https://github.com/douglasonline/Imagens/blob/master/Autenticacao_do_Carrinho_de_Compras.png)     

![Adicionando Item no Carrinho de Compras](https://github.com/douglasonline/Imagens/blob/master/Adicionando_Item_no_Carrinho_de_Compras.png)

- Para realizar o pagamento o Usuário precisa está autenticado é passar o id do item que está no carrinho de compras,
Utilizando o endereço
- http://localhost:8083/payment/makePayment/5

![Autenticacao do Pagamento](https://github.com/douglasonline/Imagens/blob/master/Autenticacao_do_Pagamento.png)

![Realiando Pagamento](https://github.com/douglasonline/Imagens/blob/master/Realiando_Pagamento.png)

- Após finalizar o pagamento o Microsserviço de Notificação (Notification), vai notifica-lo no Gmail

![Envio Para Gmail](https://github.com/douglasonline/Imagens/blob/master/Envio_Para_Gmail.png)


# Autor

Douglas

https://www.linkedin.com/in/douglas-j-b2194a232/

