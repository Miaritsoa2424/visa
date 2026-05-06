# Sprint 1

### Description :
  Insertion de demande de visa dans la base de donnees avec toutes les sections d'information
### Repartition : 
#### Ny ando
1. Branche : sprint1/1/InitProject
2. Taches:
	- [ ] Cloner le repo : https://github.com/Miaritsoa2424/visa
	- [ ] Creer application.properties a partir de application.properties.example
	- [ ] Verifier connection base
	- [ ] Creer les entites
	- [ ] Tester a partir des donnes de test et une page pour lister les demandes
	- [ ] Creation de donnees de test conforma a ce que Naina a dit
	- [ ] Creer une page de bienvenue pour faire choisir quelle type de demande le client veut faire[Ilay css an'ilay projet taloha ampiasaina]
3. DeadLine : 20/04/2026 19:00

#### Elyance (Tsy manomboka raha tsy vita ny an'i Ny Ando)
1. Branche : sprint1/1/CreateDemande
2. Taches:
	- [ ] Cloner le repo : https://github.com/Miaritsoa2424/visa
	- [ ]  Creer application.properties a partir de application.properties.example
	- [ ] Verifier connection base
	- [ ] Creer une page pour le formulaire de demande
		- [ ] Formulaire long mais avec des sections[Etat civil,Passeport,Visa transformable,Visa demande] Drop down pour chaque section
			
		- [ ] Dans la section Visa demande,ca doit etre dynamique a partir du type de visa et lister a partir de champ_fournir
		- [ ] Lire les tables pour voir les champs a ajouter
	- [ ] Creer fonction nouvelle demande dans le demandeService
		- [ ] Creer des fonction d'insertion dans les services de (Passeport,VisaTransformable,Personne,Demande,DossierPro)
		- [ ] Dans les insertions, pour les champs obligatoires,ajouter un catch avec message clair si ca n'existe pas
		- [ ] Demande nouvelleDemandeTitre(Passeport,VisaTransformable,Personne,Demande,List DossierPro)
		- [ ] WorkFlow d'insertion dans la base de donnees:
			Personne -> Passeport -> Visa_transformable -> Demande -> Statut_demande
	- [ ] Creer le controller pour la creation de demande[request Param dynamique angamba no mety satria ilay champ zao lasa dynamique dia tsy aiko hoe ahoana ny hanovanao anazy]
	- [ ] Page succes si l'insertion est valide
3. Deadline : 21/04/2026 12:00 


# Sprint 2

### Description :
  Transfert/Duplicata de visa sans donnees anterieures disponibles.
  Cas exceptionnel: on simule d'abord une personne source (Personne 1) avec creation complete + visa deja valide,
  puis on permet a une personne cible (Personne 2) de faire la demande de transfert.

### Regles metier visees :
1. Personne 1 remplit un formulaire similaire a Premiere demande directement valide et visa cree
2. Personne 2 fait une demande de transfert/duplicata liee au visa de Personne 1.

### Repartition :
#### Elyance
1. Branche : sprint2/1/source-visa
2. Taches:
	- [ ] Ajouter les types et references necessaires pour Sprint 2 (type_demande/type_statuts si besoin)
	- [ ] Ajouter page formulaire (meme structure que Premiere demande)
		- [ ] Creation automatique statut_demande = "Visa validee"
		- [ ] Creation automatique visa + statut_visa = "Actif"
	- [ ] Ajouter validations metier pour creation directe (champs obligatoires + coherence dates)

#### Miaritsoa 
1. Branche : sprint2/2/transfert-visa
2. Taches:
	- [ ] Definir le workflow transfert Personne 2 :
		- [ ] Selection/recherche du visa source (numero visa ou numero passeport source)
		- [ ] Verification visa source existant et pas expire, annule, ...
		- [ ] nouveau passeport
		- [ ] Lien entre visa source et nouvelle demande de transfert
		
	- [ ] Creer DTO + controller pour la demande de transfert
	- [ ] Creer service transfertVisa dans DemandeService (ou service dedie)
	- [ ] insertion nouveau ligne visa
	- [ ] Inserer historique de transfert
	- [ ] Ajouter donnees de test pour scenario complet Personne 1 -> Personne 2
	- [ ] Tester les cas limites:
		- [ ] visa source introuvable
		- [ ] visa source non transferables (expire, annule, etc.)

# Sprint 3

### Description :
Scan / Upload des fichiers pour les dossiers professionnels (workflow de scan et marquage)

### Repartition :

#### Ny Ando (Backend)
1. Branche : sprint3/1/scan-upload-backend
2. Taches:
	- [ ] Ajouter une table `statut_dossier_pro` (id, libelle) dans les scripts SQL
	- [ ] Ajouter une table `historique_statut_dossier_pro` (id, id_statut_dossier_pro FK, id_dossier_pro FK)
	- [ ] Donnees initiales dans `statut_dossier_pro`: Non coché, Coché, Scanné
	- [ ] Creer la table `fichier_uploade` (id, valeur, dossier_professionnel_id FK) dans les scripts SQL
	- [ ] Ajouter l'entite JPA `FichierUploade` et le `FichierUploadeRepository`
	- [ ] Ajouter l'entite JPA `StatutDossierPro` et le `StatutDossierProRepository`
	- [ ] Ajouter l'entite JPA `HistoriqueStatutDossierPro` et le `HistoriqueStatutDossierProRepository`
	- [ ] Mettre a jour les donnees de test (data.sql) avec les nouveaux statuts

#### Miaritsoa (Frontend / Controller)
1. Branche : sprint3/2/scan-upload-frontend
2. Taches:
	- [ ] Afficher les champs a fournir dans la fiche demande
	- [ ] Ajouter un statut "Scan termine" dans `type_statut_demande` et mettre a jour data.sql
	- [ ] Dans la page Modifier, afficher:
		- [ ] Un bouton "Scanner les fichiers" si statut != "Scan termine"
		- [ ] Le texte "Fichier deja uploade" si statut == "Scan termine"
	- [ ] Creer la page `upload-pieces-justificatif.jsp` avec champs d'upload dynamiques
	- [ ] La page affiche les champs d'upload en fonction du nombre de `dossier_professionnel`
	- [ ] Validation: on peut uploader seulement si le statut du dossier est "Coché"
	- [ ] Implementer un service pour sauvegarder les metadonnees d'upload:
		- [ ] Copier le fichier dans `assets/`
		- [ ] Inserer dans la table `fichier_uploades`
		- [ ] Mettre a jour le statut du dossier professionnel
	- [ ] Creer l'endpoint d'upload (multipart) et tester
	- [ ] A reussite, rediriger et afficher le statut mis a jour
	- [ ] Validations: types de fichiers autorises, taille max, verification du lien dossier_professionnel

3. Deadline : A definir


# Sprint 4

### Description :
Generation de QR Code et integration de l'application Vue pour consultation des demandes

### Repartition :

#### Elyance (Backend API)
1. Branche : sprint4/1/generation-qrcode
2. Taches:
	- [x] Generer un QRCode a la creation de la demande
	- [x] Afficher le QRCode dans la page demande-confirmation
	- [x] Afficher le QRCode dans la page demande-fiche
	- [ ] Creer l'API `/api/getDemandes?ref=...`:
		- [ ] Fonction `getByNumPasseportOrNumDemande(string reference)` retourne `List<Demande>`
		- [ ] Parametres: numero passeport OU numero de demande
		- [ ] Resultat: liste des demandes associees
	- [ ] Creer l'API `/api/getObjectById?ref=...`:
		- [ ] Fonction `getPasseportOrDemandeById(string reference)`
		- [ ] Retourne: `objet: passeport/demande, data: json de l'objet`
		- [ ] Determiner si c'est un passeport ou une demande
	- [ ] A la lecture du QRCode: rediriger vers l'application Vue avec les donnees

3. Deadline : A definir

#### Ny Ando (Frontend Vue)
1. Partie I - Setup du repository Vue
	- [ ] Creer un nouveau repository GitHub: `visa-vue`
	- [ ] Initialiser le projet Vue (branche: master)

2. Branche : sprint4/2/recherche-demande
3. Taches:
	- [ ] Creer une page de recherche avec:
		- [ ] Label: "Entrez votre numero de passeport ou votre numero de demande"
		- [ ] Champ de texte pour l'input
		- [ ] Bouton Submit
	- [ ] Creer une page de resultats qui affiche:
		- [ ] Apres recherche par demande: la demande concernee (mise en avant) + liste des autres demandes du meme demandeur
		- [ ] Apres recherche par passeport: liste des demandes du proprietaire de ce passeport
		- [ ] Afficher l'historique et les statuts des demandes
	- [ ] Creer un component `ListeDemandes` reutilisable pour afficher la liste
	- [ ] Integrer les appels aux APIs du backend Java
	- [ ] Tester la lecture de QR Code et la redirection

4. Deadline : A definir
