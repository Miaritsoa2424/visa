-- Reinitialisation complete des donnees
-- PostgreSQL: les tables liees par FK doivent etre tronquees dans la meme commande

TRUNCATE TABLE
	historique_passeport_visa,
	carte_resident,
	statut_visa,
	visa,
	fichier_uploade,
	dossier_professionnel,
	statut_demande,
	demande,
	visa_transformable,
	passeport,
	personne,
	champ_fournir,
	type_statut_visa,
	type_statut_demande,
	type_demande,
	pays,
	nationalite,
	situation_familiale,
	type_visa
RESTART IDENTITY CASCADE;
