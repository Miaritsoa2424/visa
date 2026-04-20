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


 
 
 