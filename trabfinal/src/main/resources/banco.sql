create table Endereco(
id serial PRIMARY KEY,
rua varchar(255),
cep varchar(8),
complemento varchar(255),
estado varchar(55),
pais varchar(55),
numero integer
);
create table Clientes (
 id serial PRIMARY KEY,
 nome varchar(255),
 sobrenome varchar(255),
 rg varchar(9),
 cpf varchar(11),
 salario numeric,
 idEndereco integer references Endereco(id)
);
create table Contas (
id serial PRIMARY KEY,
idCliente integer references Clientes(id),
tipo varchar(20),
deposito_inical numeric,
limite numeric,
numero_conta uuid,
montante_minimo numeric,
deposito_minimo numeric,
saldo numeric
);