<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page">
    <div class="hero">
        <h1>Demande enregistrée avec succès</h1>
        <p>La demande a été insérée dans la base de données. Un statut initial <strong>${statutInitial}</strong> a également été créé automatiquement.</p>
        <div class="badge">Insertion confirmée</div>
    </div>

    <div class="grid">
        <div class="card">
            <h2>Informations de la demande</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">ID demande</span>
                    <span class="value">${demande.id}</span>
                </div>
                <div class="item">
                    <span class="label">Date demande</span>
                    <span class="value">${demande.dateDemande}</span>
                </div>
                <div class="item">
                    <span class="label">Type visa</span>
                    <span class="value">${demande.typeVisa.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Type demande</span>
                    <span class="value">${demande.typeDemande.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Statut initial</span>
                    <span class="value">${statutInitial}</span>
                </div>
                <div class="item">
                    <span class="label">Champs de dossier enregistrés</span>
                    <span class="value">${champFournirCount}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>État civil</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Nom</span>
                    <span class="value">${dto.nom}</span>
                </div>
                <div class="item">
                    <span class="label">Prénom</span>
                    <span class="value">${dto.prenom}</span>
                </div>
                <div class="item">
                    <span class="label">Date de naissance</span>
                    <span class="value">${dto.dateNaissance}</span>
                </div>
                <div class="item">
                    <span class="label">Téléphone</span>
                    <span class="value">${dto.telephone}</span>
                </div>
                <div class="item">
                    <span class="label">Nationalité</span>
                    <span class="value">${demande.passeport.personne.nationalite.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Situation familiale</span>
                    <span class="value">${demande.passeport.personne.situationFamiliale.libelle}</span>
                </div>
                <div class="item item-span-2">
                    <span class="label">Adresse</span>
                    <span class="value">${dto.adresse}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Passeport</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Numéro passeport</span>
                    <span class="value">${dto.numeroPasseport}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'expiration</span>
                    <span class="value">${dto.dateExpirationPasseport}</span>
                </div>
                <div class="item">
                    <span class="label">Demandeur</span>
                    <span class="value">${demande.passeport.personne.prenom} ${demande.passeport.personne.nom}</span>
                </div>
                <div class="item">
                    <span class="label">Email</span>
                    <span class="value">${dto.email}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Visa transformable</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Numéro</span>
                    <span class="value">${dto.numeroVisaTransformable}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'entrée</span>
                    <span class="value">${dto.dateArrivee}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'expiration</span>
                    <span class="value">${dto.dateExpirationVisaTransformable}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Action</h2>
            <div class="actions">
                <a class="btn btn-primary" href="/demandes">Voir la liste des demandes</a>
                <a class="btn btn-secondary" href="/demande/nouvelle?typeDemandeId=${demande.typeDemande.id}">Créer une autre demande</a>
            </div>
        </div>
    </div>
</div>
