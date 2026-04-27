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



 
 
 