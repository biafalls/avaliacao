SET COLLATION PORTUGUESE_BRAZIL STRENGTH PRIMARY;

CREATE TABLE funcionario (rowid bigint auto_increment, nm_funcionario VARCHAR(255));
INSERT INTO funcionario (nm_funcionario) VALUES ('João'), ('Maria'), ('José'), ('Joana');