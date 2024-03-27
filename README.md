# Avaliação técnica do processo seletivo da empresa Senior para vaga de Desenvolvedor Back-End Java.

Vaga: Desenvolvedor Back-End Java  

Proposta: Desenvolvimento de API Rest para realização de vendas de produtos/serviços.

PROVA NIVEL II

# Tecnologias utilizadas:

#### 1. Tecnologias mínimas:

* Banco de dados PostgreSQL
* Java 17
* Maven
* Spring
* JPA
* REST com JSON

#### 2. Tecnologias Extras:

* Bean Validation
* JUnit 5
* Mockito
* Swagger
* Docker

# Requisitos da prova:

#### 1. Requisitos mínimos para o nível II:

* Deverá ser desenvolvido um cadastro (Create/Read/Update/Delete/List com paginação) para as seguintes entidades: produto/serviço, pedido e itens de pedido.

* Todos as entidades deverão ter um ID único do tipo UUID gerado automaticamente 

* No cadastro de produto/serviço deverá ter uma indicação para diferenciar um produto de um serviço.

* Deverá ser possível aplicar um percentual de desconto no pedido, porém apenas para os itens que sejam produto (não serviço); o desconto será sobre o valor total dos produtos

* Somente será possível aplicar desconto no pedido se ele estiver na situação Aberto (Fechado bloqueia).

* Não deve ser possível excluir um produto/serviço se ele estiver associado a algum pedido.

* Não deve ser possível adicionar um produto desativado em um pedido.


#### 2. Requisitos extras baseado do nível III:

* As entidades deverão utilizar Bean Validation.

* Deverá ser implementado um ControllerAdvice para customizar os HTTP Response das requisições (mínimo BAD REQUEST).


# Aplicação

nome: api-venda

porta: 8084

OBS: Na primeira execução do projeto todas as tabelas do banco de dados usadas na API serão geradas automaticamente via Hibernate.

# Como executar projeto.
Abaixo será apresentado alguns exemplos de entrada para testar API.

### Produto

## Criar Produto (POST)
Endpoint: http://localhost:8084/produtos
```
{
    "nome": "Nome Teste",
    "descricao": " Nome teste",
    "valorUnitario": 100,
    "quantidade": 3,
    "tipoDemanda": "SERVICO"
}
```

## Atualizar Produto (PUT): 
Endpoint: http://localhost:8084/produtos/{UUID}
```
{
    "nome": "Teste [UPDATE]",
    "descricao": " teste  [UPDATE]",
    "valorUnitario": 300,
    "quantidade": 6,
    "tipoDemanda": "PRODUTO",
    "ativo": false
}
```

### Pedido

## Criar Pedido (POST)
Endpoint: http://localhost:8084/pedidos
```
{
    "formaPagamento": "BOLETO ",
    "statusPedido": "ABERTO",
    "percentualDesconto": 30.00,
    "itens":[
        {
          "produto":{
                "id": "8e675ffa-153b-4e42-8850-7d29aa73d1bc",
                "tipoDemanda": "PRODUTO"
            }  
        },
        {
          "produto":{
                "id": "ce5ea860-7ada-4917-ab4f-9e8573881642",
                "tipoDemanda": "PRODUTO"
           }   
        },
        {
        "produto":{
                "id": "1d8bc619-796c-401e-8f04-10b0326706b4",
                "tipoDemanda": "SERVICO"
            }  
        } 
    ]
}
```

## Atualizar Pedido (PUT)
Endpoint: http://localhost:8084/pedidos/{UUID}
```
{
    "formaPagamento": "CARTAO_DEBITO",
    "statusPedido": "ABERTO",
    "percentualDesconto": 22.00
}
```

### Item de Pedido

## Criar Item de Pedido (POST)
Endpoint: http://localhost:8084/itens
```
{
    "pedido": {
        "id":"65a5fe5f-7a14-4685-9b03-55ea482ed7d7"
    },
    "produto": {
        "id": "8e675ffa-153b-4e42-8850-7d29aa73d1bc"
    }
}
```

## Atualizar Item de Pedido (PUT)
Endpoint: http://localhost:8084/itens/{UUID}

Será possível atualizar o pedido e/ou produto do Item.
```
{
    "pedido": {
        "id":"65a5fe5f-7a14-4685-9b03-55ea482ed7d7"
    },
    "produto": {
        "id": "1d8bc619-796c-401e-8f04-10b0326706b4"
    }
}
```

# Banco de Dados:
O nome do banco utilizado na aplicação: vendas.

Na primeira execução do projeto todas as tabelas do banco de dados usadas na API serão geradas automaticamente via Hibernate.

#### 1. O modelo do banco (MER) utlizado no projeto:

![ModeloER](https://github.com/LPMoraes/api-venda/assets/10091268/74a9a1fb-fa10-45c2-b2a4-ff1df0fb020b)

#### 2. Configuração de padrão para conectar ao banco.

![conf-banco](https://github.com/LPMoraes/api-venda/assets/10091268/7f12697d-472d-441d-b657-2165403fe32f)

#### 3 Utilizando o *Docker* podemos criar uma instância do *Postgres* para testar a API.
   
##### 3.1 Baixar IMAGEM via DockerHub. Link da IMAGEM usada neste projeto: https://hub.docker.com/_/postgres
```
   docker pull postgres
```   
##### 3.2 Para iniciar uma instância localmente a partir da imagem baixada:
```
  docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
```


# Swagger:

Após executar a aplicação serão aprensatadas todos os *endpoints* da API:
Link: http://localhost:8084/swagger-ui/index.html#/

# Testes unitários

Foram realizados os teste unitarios utilizando *JUnit 5* juntamente com *Mockito*.

A imagem mostrar a cobertura 100% completa para os pacotes *resource* e *service*.
![Teste-unitarios](https://github.com/LPMoraes/api-venda/assets/10091268/c92d989a-4205-4889-bf44-e8d5f22c3bd2)



