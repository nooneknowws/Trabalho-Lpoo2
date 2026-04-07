-- Clear existing data (optional, for clean test runs)
DELETE FROM Contas;
DELETE FROM Clientes;
DELETE FROM Endereco;

-- Reset serial sequences (optional, for clean test runs)
ALTER SEQUENCE endereco_id_seq RESTART WITH 1;
ALTER SEQUENCE clientes_id_seq RESTART WITH 1;
ALTER SEQUENCE contas_id_seq RESTART WITH 1;

-- Insert Enderecos
INSERT INTO Endereco (rua, cep, complemento, estado, pais, numero) VALUES ('Rua das Flores', '80000000', 'Apto 101', 'PR', 'Brasil', 100); -- id=1
INSERT INTO Endereco (rua, cep, complemento, estado, pais, numero) VALUES ('Avenida Central', '80000001', 'Casa 5', 'PR', 'Brasil', 250); -- id=2
INSERT INTO Endereco (rua, cep, complemento, estado, pais, numero) VALUES ('Travessa da Paz', '80000002', 'Bloco B', 'SC', 'Brasil', 30);  -- id=3
INSERT INTO Endereco (rua, cep, complemento, estado, pais, numero) VALUES ('Rua da Praia', '80000003', 'Cobertura', 'RJ', 'Brasil', 500); -- id=4
INSERT INTO Endereco (rua, cep, complemento, estado, pais, numero) VALUES ('Alameda dos Anjos', '80000004', 'Sala 1', 'SP', 'Brasil', 75); -- id=5

-- Insert Clientes
-- Cliente 1: João Silva (no accounts)
INSERT INTO Clientes (nome, sobrenome, rg, cpf, salario, idEndereco) VALUES ('João', 'Silva', '1111111', '11111111111', 3500.00, 1); -- id=1
-- Cliente 2: Maria Souza (both types of accounts)
INSERT INTO Clientes (nome, sobrenome, rg, cpf, salario, idEndereco) VALUES ('Maria', 'Souza', '2222222', '22222222222', 7000.00, 2); -- id=2
-- Cliente 3: Pedro Almeida (only Conta Corrente)
INSERT INTO Clientes (nome, sobrenome, rg, cpf, salario, idEndereco) VALUES ('Pedro', 'Almeida', '3333333', '33333333333', 4500.00, 3); -- id=3
-- Cliente 4: Ana Costa (only Conta Investimento)
INSERT INTO Clientes (nome, sobrenome, rg, cpf, salario, idEndereco) VALUES ('Ana', 'Costa', '4444444', '44444444444', 8000.00, 4); -- id=4
-- Cliente 5: Carlos Pereira (no accounts)
INSERT INTO Clientes (nome, sobrenome, rg, cpf, salario, idEndereco) VALUES ('Carlos', 'Pereira', '5555555', '55555555555', 2800.00, 5); -- id=5


-- Insert Contas
-- Contas for Cliente 2 (Maria Souza - id=2)
INSERT INTO Contas (idCliente, tipo, deposito_inical, limite, numero_conta, montante_minimo, deposito_minimo, saldo)
VALUES (2, 'Corrente', 1500.00, 1000.00, 100001, NULL, NULL, 1500.00); -- Conta Corrente
INSERT INTO Contas (idCliente, tipo, deposito_inical, limite, numero_conta, montante_minimo, deposito_minimo, saldo)
VALUES (2, 'Investimento', 5000.00, NULL, 100002, 1000.00, 500.00, 5000.00); -- Conta Investimento

-- Contas for Cliente 3 (Pedro Almeida - id=3)
INSERT INTO Contas (idCliente, tipo, deposito_inical, limite, numero_conta, montante_minimo, deposito_minimo, saldo)
VALUES (3, 'Corrente', 800.00, 500.00, 100003, NULL, NULL, 800.00); -- Conta Corrente

-- Contas for Cliente 4 (Ana Costa - id=4)
INSERT INTO Contas (idCliente, tipo, deposito_inical, limite, numero_conta, montante_minimo, deposito_minimo, saldo)
VALUES (4, 'Investimento', 12000.00, NULL, 100004, 2000.00, 1000.00, 12000.00); -- Conta Investimento