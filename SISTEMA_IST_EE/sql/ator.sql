-- Table: siss.ator

-- DROP TABLE ator;

CREATE TABLE ator
(
  id integer NOT NULL,
  login character varying NOT NULL,
  senha character varying NOT NULL,
  nome character varying,
  perfil character(2),
  codigo_alteracao_senha character varying,
  data_validade_codigo timestamp with time zone,
  CONSTRAINT id PRIMARY KEY (id)
  
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ator
  OWNER TO postgres;
