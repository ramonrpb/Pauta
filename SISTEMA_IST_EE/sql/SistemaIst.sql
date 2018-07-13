CREATE TABLE turno
(
  id integer NOT NULL,
  nome character varying(30) NOT NULL,
  CONSTRAINT "PK_TURNO" PRIMARY KEY (id),
  CONSTRAINT "AK_TURNO" UNIQUE (nome)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE disciplina
(
  nome character varying(150) NOT NULL,
  codigo character varying(10) NOT NULL,
  id serial NOT NULL,
  CONSTRAINT "PK_DISCIPLINA" PRIMARY KEY (id),
  CONSTRAINT "AK_DISCIPLINA" UNIQUE (codigo)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE professor
(
  id serial NOT NULL,
  nome character varying(200) NOT NULL,
  matricula numeric NOT NULL,
  email character varying(150) NOT NULL,
  CONSTRAINT "PK_PROFESSOR" PRIMARY KEY (id),
  CONSTRAINT "AK_PROFESSOR_EMAIL" UNIQUE (email),
  CONSTRAINT "AK_PROFESSOR_MATRICULA" UNIQUE (matricula)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE aluno
(
  id serial NOT NULL,
  nome character varying(200) NOT NULL,
  matricula numeric NOT NULL,
  email character varying(150) NOT NULL,
  CONSTRAINT "PK_ALUNO" PRIMARY KEY (id),
  CONSTRAINT "AK_ALUNO_EMAIL" UNIQUE (email),
  CONSTRAINT "AK_ALUNO_MATRICULA" UNIQUE (matricula)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE aula
(
  id serial NOT NULL,
  dia date NOT NULL,
  CONSTRAINT "PK_AULA" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE aluno_aula
(
  id_aluno serial NOT NULL,
  id_aula serial NOT NULL,
  presenca boolean NOT NULL,
  id serial NOT NULL,
  CONSTRAINT "PK_ALUNO_AULA" PRIMARY KEY (id),
  CONSTRAINT "FK_ALUNO" FOREIGN KEY (id_aluno)
      REFERENCES aluno (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "FK_AULA" FOREIGN KEY (id_aula)
      REFERENCES aula (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "AK_ALUNO_AULA" UNIQUE (id_aluno, id_aula)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE turma
(
  id serial NOT NULL,
  id_professor serial NOT NULL,
  id_disciplina serial NOT NULL,
  id_turno integer NOT NULL,
  id_aula serial NOT NULL,
  CONSTRAINT "PK_TURMA" PRIMARY KEY (id),
  CONSTRAINT "FK_AULA" FOREIGN KEY (id_aula)
      REFERENCES aula (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "FK_DISCIPLINA" FOREIGN KEY (id_disciplina)
      REFERENCES disciplina (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "FK_PROFESSOR" FOREIGN KEY (id_professor)
      REFERENCES professor (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "FK_TURNO" FOREIGN KEY (id_turno)
      REFERENCES turno (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE aluno_turma
(
  id serial NOT NULL,
  id_aluno serial NOT NULL,
  id_turma serial NOT NULL,
  CONSTRAINT "PK_ALUNO_TURMA" PRIMARY KEY (id),
  CONSTRAINT "FK_ALUNO" FOREIGN KEY (id_aluno)
      REFERENCES aluno (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "FK_TURMA" FOREIGN KEY (id_turma)
      REFERENCES turma (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "AK_ALUNO_TURMA" UNIQUE (id_aluno, id_turma)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE avaliacao
(
  id serial NOT NULL,
  nome character varying(30) NOT NULL,
  peso integer NOT NULL,
  id_turma serial NOT NULL,
  CONSTRAINT "PK_AVALIACAO" PRIMARY KEY (id),
  CONSTRAINT "FK_TURMA" FOREIGN KEY (id_turma)
      REFERENCES turma (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "AK_AVALIACAO" UNIQUE (nome)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE aluno_avaliacao
(
  id_aluno integer NOT NULL,
  id_avaliacao integer NOT NULL,
  nota double precision[] NOT NULL,
  id serial NOT NULL,
  CONSTRAINT "FK_ALUNO" FOREIGN KEY (id_aluno)
      REFERENCES aluno (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "FK_AVALIACAO" FOREIGN KEY (id_avaliacao)
      REFERENCES avaliacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);



--1.1----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

CREATE TABLE public.configuracao
(
   nom_configuracao character varying(100), 
   valor_configuracao character varying(200), 
   CONSTRAINT unique_nom_configuracao UNIQUE (nom_configuracao)
) 
WITH (
  OIDS = FALSE
);

insert into configuracao values('BD_VERSAO_SISTEMA_IST', '1.0');

ALTER TABLE aluno DROP COLUMN nome;
ALTER TABLE aluno DROP COLUMN email;

  
ALTER TABLE professor DROP COLUMN nome;
ALTER TABLE professor DROP COLUMN email;

ALTER TABLE ator RENAME login  TO email;

  --Atualizando a vers�o do BD
update configuracao set valor_configuracao = '1.1';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------



--1.2----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

CREATE TABLE public.ano_letivo
(
   id serial NOT NULL, 
   ano numeric NOT NULL, 
   semestre numeric NOT NULL, 
   CONSTRAINT "PK_ANO_LETIVO" PRIMARY KEY (id)
) 
WITH (
  OIDS = FALSE
)
;

ALTER TABLE turma
  ADD COLUMN id_ano_letivo serial NOT NULL;
ALTER TABLE turma
  DROP CONSTRAINT "FK_AULA";
ALTER TABLE turma
  DROP CONSTRAINT "FK_DISCIPLINA";
ALTER TABLE turma
  DROP CONSTRAINT "FK_PROFESSOR";
ALTER TABLE turma
  DROP CONSTRAINT "FK_TURNO";
ALTER TABLE turma
  ADD CONSTRAINT "FK_AULA" FOREIGN KEY (id_aula)
      REFERENCES aula (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE turma
  ADD CONSTRAINT "FK_DISCIPLINA" FOREIGN KEY (id_disciplina)
      REFERENCES disciplina (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE turma
  ADD CONSTRAINT "FK_PROFESSOR" FOREIGN KEY (id_professor)
      REFERENCES professor (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE turma
  ADD CONSTRAINT "FK_TURNO" FOREIGN KEY (id_turno)
      REFERENCES turno (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE turma
  ADD CONSTRAINT "FK_ANO_LETIVO" FOREIGN KEY (id_ano_letivo) REFERENCES ano_letivo (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

  
  
  CREATE TABLE public.periodo
(
   id serial NOT NULL, 
   nome character varying(50) NOT NULL, 
   CONSTRAINT "PK_PERIODO" PRIMARY KEY (id), 
   CONSTRAINT "AK_PERIODO" UNIQUE (nome)
) 
WITH (
  OIDS = FALSE
);

                                                
INSERT INTO periodo(id, nome) VALUES (1, '1º Período');
INSERT INTO periodo(id, nome) VALUES (2, '2º Período');
INSERT INTO periodo(id, nome) VALUES (3, '3º Período');
INSERT INTO periodo(id, nome) VALUES (4, '4º Período');
INSERT INTO periodo(id, nome) VALUES (5, '5º Período');

ALTER TABLE disciplina
  ADD COLUMN id_periodo serial NOT NULL;
ALTER TABLE disciplina
  ADD CONSTRAINT "FK_PERIODO" FOREIGN KEY (id_periodo) REFERENCES periodo (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

  
--Atualizando a vers�o do BD
update configuracao set valor_configuracao = '1.2';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------


--1.3----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
INSERT INTO turno(id, nome) VALUES (1, 'Manhã');
INSERT INTO turno(id, nome) VALUES (2, 'Noite');

--Atualizando a versão do BD
update configuracao set valor_configuracao = '1.3';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

--1.4----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
ALTER TABLE turma
  ADD COLUMN horario character varying;
  
--Atualizando a versão do BD
update configuracao set valor_configuracao = '1.4';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------


--1.5----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

ALTER TABLE turma
  ADD COLUMN nome character varying NOT NULL;
      
--Atualizando a versão do BD
update configuracao set valor_configuracao = '1.5';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

--1.6----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

ALTER TABLE ano_letivo
  ADD COLUMN data_inicio date;
ALTER TABLE ano_letivo
  ADD COLUMN data_fim date;

--Atualizando a versão do BD
update configuracao set valor_configuracao = '1.6';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------


--1.7----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

ALTER TABLE aula
  ADD COLUMN quantidade numeric NOT NULL;
  
--Atualizando a versão do BD
update configuracao set valor_configuracao = '1.7';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------


--1.8----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

DELETE FROM turno;

ALTER TABLE turma DROP CONSTRAINT "FK_TURNO";

ALTER TABLE turma DROP COLUMN id_turno;

ALTER TABLE turma ADD COLUMN id_turno serial NOT NULL;

ALTER TABLE turno DROP CONSTRAINT "PK_TURNO";

ALTER TABLE turno DROP COLUMN id;

ALTER TABLE turno ADD COLUMN id serial NOT NULL;

ALTER TABLE turno ADD CONSTRAINT "PK_TURNO" PRIMARY KEY (id);

ALTER TABLE turma ADD CONSTRAINT "FK_TURNO" FOREIGN KEY (id_turno) REFERENCES turno (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO turno(nome, id) VALUES ('Manhã', 1);
INSERT INTO turno(nome, id) VALUES ('Noite', 2);


  
--Atualizando a versão do BD
update configuracao set valor_configuracao = '1.8';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------


--1.9----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

ALTER TABLE turma DROP CONSTRAINT "FK_AULA";
  
ALTER TABLE turma DROP COLUMN id_aula;
--Atualizando a versão do BD
update configuracao set valor_configuracao = '1.9';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------


--2.0----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

ALTER TABLE aula
  ADD COLUMN id_turma serial NOT NULL;
ALTER TABLE aula
  ADD CONSTRAINT "FK_TURMA" FOREIGN KEY (id_turma) REFERENCES turma (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

  --Atualizando a versão do BD
update configuracao set valor_configuracao = '2.0';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------


--2.1----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
ALTER TABLE aluno_avaliacao
  DROP COLUMN nota;
	  
ALTER TABLE aluno_avaliacao
  ADD COLUMN nota double precision;
  
   --Atualizando a versão do BD
update configuracao set valor_configuracao = '2.1';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

--2.2----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
ALTER TABLE aluno_aula
  DROP CONSTRAINT "AK_ALUNO_AULA";
  
 --Atualizando a versão do BD
update configuracao set valor_configuracao = '2.2';

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

--2.3----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

ALTER TABLE avaliacao
  DROP CONSTRAINT "AK_AVALIACAO";
  
--Atualizando a versão do BD
update configuracao set valor_configuracao = '2.3';


---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

--2.4----------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

ALTER TABLE aluno RENAME matricula  TO matricula_antiga;

ALTER TABLE aluno DROP CONSTRAINT "AK_ALUNO_MATRICULA";

ALTER TABLE aluno ADD COLUMN matricula character varying NOT NULL DEFAULT 0;

UPDATE aluno SET matricula = matricula_antiga;

ALTER TABLE aluno DROP COLUMN matricula_antiga;

ALTER TABLE aluno ADD CONSTRAINT "AK_ALUNO_MATRICULA" UNIQUE (matricula);

ALTER TABLE professor RENAME matricula  TO matricula_antiga;

ALTER TABLE professor DROP CONSTRAINT "AK_PROFESSOR_MATRICULA";

ALTER TABLE professor ADD COLUMN matricula character varying NOT NULL DEFAULT 0;

UPDATE professor SET matricula = matricula_antiga;

ALTER TABLE professor DROP COLUMN matricula_antiga;

ALTER TABLE professor ADD CONSTRAINT "AK_PROFESSOR_MATRICULA" UNIQUE (matricula);



--Atualizando a versão do BD
update configuracao set valor_configuracao = '2.4';


