-- =====================
-- TABLES DE REFERENCE
-- =====================

CREATE TABLE type_visa(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE situation_familiale(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE nationalite(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE pays(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE type_demande(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE type_statut_visa(
   id VARCHAR(30) PRIMARY KEY,
   libelle VARCHAR(50) NOT NULL,
   rang INTEGER NOT NULL
);

CREATE TABLE type_statut_demande(
   id VARCHAR(30) PRIMARY KEY,
   libelle VARCHAR(50) NOT NULL
);

-- =====================
-- STRUCTURE METIER
-- =====================

CREATE TABLE personne(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   nom VARCHAR(50),
   prenom VARCHAR(50),
   nom_jeune_fille VARCHAR(50),
   email VARCHAR(100),
   date_naissance DATE NOT NULL,
   lieu_naissance VARCHAR(100),
   adresse TEXT,
   telephone VARCHAR(20),
   nationalite_id INTEGER NOT NULL,
   situation_familiale_id INTEGER NOT NULL,
   FOREIGN KEY(nationalite_id) REFERENCES nationalite(id),
   FOREIGN KEY(situation_familiale_id) REFERENCES situation_familiale(id)
);

CREATE TABLE passeport(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   numero VARCHAR(50) NOT NULL UNIQUE,
   date_expiration DATE,
   personne_id INTEGER NOT NULL,
   FOREIGN KEY(personne_id) REFERENCES personne(id)
);

CREATE TABLE visa_transformable(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   numero VARCHAR(50) NOT NULL UNIQUE,
   date_arrivee DATE NOT NULL,
   date_expiration DATE,
   personne_id INTEGER NOT NULL,
   FOREIGN KEY(personne_id) REFERENCES personne(id)
);

CREATE TABLE demande(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   date_demande DATE NOT NULL,
   passeport_id INTEGER NOT NULL,
   type_visa_id INTEGER NOT NULL,
   type_demande_id INTEGER NOT NULL,
   FOREIGN KEY(passeport_id) REFERENCES passeport(id),
   FOREIGN KEY(type_visa_id) REFERENCES type_visa(id),
   FOREIGN KEY(type_demande_id) REFERENCES type_demande(id)
);

CREATE TABLE statut_demande(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   date_statut DATE NOT NULL,
   type_statut_demande_id VARCHAR(30) NOT NULL,
   demande_id INTEGER NOT NULL,
   FOREIGN KEY(type_statut_demande_id) REFERENCES type_statut_demande(id),
   FOREIGN KEY(demande_id) REFERENCES demande(id)
);

CREATE TABLE champ_fournir(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   libelle VARCHAR(50),
   type_donnee VARCHAR(30),
   type_visa_id INTEGER NOT NULL,
   FOREIGN KEY(type_visa_id) REFERENCES type_visa(id)
);

CREATE TABLE dossier_professionnel(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   valeur TEXT,
   demande_id INTEGER NOT NULL,
   champ_fournir_id INTEGER NOT NULL,
   FOREIGN KEY(demande_id) REFERENCES demande(id),
   FOREIGN KEY(champ_fournir_id) REFERENCES champ_fournir(id)
);

CREATE TABLE fichier_uploade(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   valeur TEXT,
   dossier_professionnel_id INTEGER NOT NULL,
   FOREIGN KEY(dossier_professionnel_id) REFERENCES dossier_professionnel(id)
);

CREATE TABLE visa(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   numero VARCHAR(50) NOT NULL UNIQUE,
   date_entree DATE,
   date_expiration DATE,
   date_delivrance DATE,
   demande_id INTEGER NOT NULL,
   pays_entree_id INTEGER NOT NULL,
   personne_id INTEGER NOT NULL,
   type_visa_id INTEGER NOT NULL,
   FOREIGN KEY(demande_id) REFERENCES demande(id),
   FOREIGN KEY(pays_entree_id) REFERENCES pays(id),
   FOREIGN KEY(personne_id) REFERENCES personne(id),
   FOREIGN KEY(type_visa_id) REFERENCES type_visa(id)
);

CREATE TABLE statut_visa(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   date_statut DATE NOT NULL,
   visa_id INTEGER NOT NULL,
   type_statut_visa_id VARCHAR(30) NOT NULL,
   FOREIGN KEY(visa_id) REFERENCES visa(id),
   FOREIGN KEY(type_statut_visa_id) REFERENCES type_statut_visa(id)
);

CREATE TABLE carte_resident(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   numero VARCHAR(50) UNIQUE,
   visa_id INTEGER NOT NULL,
   FOREIGN KEY(visa_id) REFERENCES visa(id)
);

CREATE TABLE historique_passeport_visa(
   id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   date_historique DATE NOT NULL,
   visa_id INTEGER NOT NULL,
   passeport_id INTEGER NOT NULL,
   FOREIGN KEY(visa_id) REFERENCES visa(id),
   FOREIGN KEY(passeport_id) REFERENCES passeport(id)
);