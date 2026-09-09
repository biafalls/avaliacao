SET COLLATION PORTUGUESE_BRAZIL STRENGTH PRIMARY;

CREATE TABLE funcionario (
	rowid BIGINT AUTO_INCREMENT PRIMARY KEY, 
	nm_funcionario VARCHAR(255) NOT NULL
);

INSERT INTO funcionario (nm_funcionario) VALUES 
('João'), ('Maria'), ('José'), ('Joana');

CREATE TABLE agenda (
	rowid BIGINT AUTO_INCREMENT PRIMARY KEY, 
	nm_agenda VARCHAR(255) NOT NULL, 
	periodo_disponivel ENUM('MANHA', 'TARDE', 'AMBOS') NOT NULL
);

INSERT INTO agenda (nm_agenda, periodo_disponivel) VALUES
 	('Atendimento', 'MANHA'),
    ('Reuniões', 'TARDE'),
    ('Treinamentos', 'AMBOS'),
    ('Consultoria', 'AMBOS');

CREATE TABLE compromisso (
	rowid BIGINT AUTO_INCREMENT PRIMARY KEY, 
	rowid_funcionario BIGINT NOT NULL, 
	rowid_agenda BIGINT NOT NULL, 
	dt_compromisso DATE NOT NULL, 
	hr_compromisso TIME NOT NULL, 
	
	CONSTRAINT fk_funcionario FOREIGN KEY (rowid_funcionario) REFERENCES funcionario(rowid),
	CONSTRAINT fk_agenda FOREIGN KEY (rowid_agenda) REFERENCES agenda(rowid)
);

INSERT INTO compromisso (rowid_funcionario, rowid_agenda, dt_compromisso, hr_compromisso) VALUES
	(1, 1, '2026-09-01', '09:00:00'),
    (2, 2, '2026-09-01', '14:00:00'),
    (3, 3, '2026-09-02', '10:30:00'),
    (4, 3, '2026-09-02', '15:30:00'),
    (1, 3, '2026-09-05', '13:00:00'),
    (2, 1, '2026-09-10', '08:30:00');
   