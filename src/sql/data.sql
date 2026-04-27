-- Initialisation des references pour les tests
INSERT INTO type_visa (libelle)
VALUES
	('Etudiant'),
	('Professionnel');
INSERT INTO situation_familiale (libelle)
VALUES
	('Celibataire'),
	('Marie(e)'),
	('Divorce(e)');
INSERT INTO nationalite (libelle)
VALUES
	('Francaise'),
	('Canadienne'),
	('Senegalaise'),
	('Italienne'),
	('Chinoise');
INSERT INTO pays (libelle)
VALUES
	('Madagascar'),
	('France'),
	('Senegal'),
	('Canada');
INSERT INTO type_demande (libelle)
VALUES
	('Nouveau titre'),
	('Renouvellement'),
	('Duplicata');
INSERT INTO type_statut_demande (id, libelle)
VALUES
	('1', 'Cree'),
	('2', 'Scanee'),
	('3', 'Visa validee');
INSERT INTO type_statut_visa (id, libelle, rang)
VALUES
	('1', 'Actif', 1),
	('2', 'Expire', 2);
INSERT INTO champ_fournir (libelle, type_donnee, type_visa_id)
VALUES
	('Certificat Scolaire', 'PDF', 1),
	('Justificatif Ressources', 'TEXT', 1),
	('Contrat Travail', 'PDF', 2),
	('SIRET Employeur', 'TEXT', 2);

-- 1 & 2. Personnes avec visa etudiant deja valide
INSERT INTO personne (nom, prenom, email, date_naissance, lieu_naissance, adresse, telephone, nationalite_id, situation_familiale_id)
VALUES
	('DUVAL', 'Alice', 'a.duval@mail.com', '2004-05-12', 'Paris', 'Ankatso II, Tana', '0341100001', 1, 1),
	('SOW', 'Omar', 'o.sow@mail.sn', '2003-11-20', 'Dakar', 'Ivato, Tana', '0341100002', 3, 1);
INSERT INTO passeport (numero, date_expiration, personne_id)
VALUES
	('PAS-FR-01', '2030-01-01', 1),
	('PAS-SN-02', '2029-06-15', 2);
INSERT INTO demande (date_demande, passeport_id, type_visa_id, type_demande_id)
VALUES
	('2026-01-10', 1, 1, 1),
	('2026-01-15', 2, 1, 1);
INSERT INTO dossier_professionnel (valeur, demande_id, champ_fournir_id)
VALUES
	('insc_sorbonne.pdf', 1, 1),
	('bourse_france.txt', 1, 2),
	('insc_cntemad.pdf', 2, 1),
	('virement_parent.txt', 2, 2);
INSERT INTO visa (numero, date_entree, date_expiration, date_delivrance, demande_id, pays_entree_id, personne_id, type_visa_id)
VALUES
	('V-ETU-001', '2026-01-01', '2027-01-01', '2026-01-20', 1, 1, 1, 1),
	('V-ETU-002', '2026-01-05', '2027-01-05', '2026-01-25', 2, 1, 2, 1);
INSERT INTO statut_visa (date_statut, visa_id, type_statut_visa_id)
VALUES
	('2026-01-20', 1, '1'),
	('2026-01-25', 2, '1');

-- 3, 4 & 5. Personnes avec visa transformable
INSERT INTO personne (nom, prenom, email, date_naissance, lieu_naissance, adresse, telephone, nationalite_id, situation_familiale_id)
VALUES
	('SMITH', 'John', 'j.smith@mail.ca', '1995-03-10', 'Quebec', 'Ivandry, Tana', '0341100003', 2, 2),
	('ROSSI', 'Mario', 'm.rossi@mail.it', '1988-07-22', 'Rome', 'Ambohibao, Tana', '0341100004', 4, 2),
	('LI', 'Wei', 'w.li@mail.cn', '2002-09-30', 'Pekin', 'Behoririka, Tana', '0341100005', 5, 1);
INSERT INTO passeport (numero, date_expiration, personne_id)
VALUES
	('PAS-CA-03', '2031-12-01', 3),
	('PAS-IT-04', '2028-04-10', 4),
	('PAS-CN-05', '2030-08-20', 5);
INSERT INTO visa_transformable (numero, date_arrivee, date_expiration, personne_id)
VALUES
	('VT-CA-101', '2026-03-01', '2026-06-01', 3),
	('VT-IT-102', '2026-03-15', '2026-06-15', 4),
	('VT-CN-103', '2026-04-01', '2026-07-01', 5);
-- John et Mario demandent un visa professionnel, Wei demande un visa etudiant
INSERT INTO demande (date_demande, passeport_id, type_visa_id, type_demande_id)
VALUES
	('2026-03-10', 3, 2, 1),
	('2026-03-20', 4, 2, 1),
	('2026-04-05', 5, 1, 1);
INSERT INTO dossier_professionnel (valeur, demande_id, champ_fournir_id)
VALUES
	('contrat_mines.pdf', 3, 3),
	('SIRET_999888', 3, 4),
	('contrat_ong.pdf', 4, 3),
	('SIRET_777666', 4, 4),
	('insc_u_tana.pdf', 5, 1),
	('releve_banque.txt', 5, 2);
INSERT INTO statut_demande (date_statut, type_statut_demande_id, demande_id)
VALUES
	('2026-03-10', '1', 3),
	('2026-03-20', '2', 4),
	('2026-04-05', '1', 5);