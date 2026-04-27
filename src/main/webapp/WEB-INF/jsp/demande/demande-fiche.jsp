<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page">
    <div class="hero">
        <h1>Fiche de la demande #${demande.id}</h1>
        <p>Vue detaillee de la demande selectionnee.</p>
        <div class="badge">Consultation</div>
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
                    <span class="label">Modifiable</span>
                    <span class="value"><c:choose><c:when test="${canEdit}">Oui</c:when><c:otherwise>Non</c:otherwise></c:choose></span>
                </div>
                <div class="item">
                    <span class="label">Champs de dossier</span>
                    <span class="value">${selectedChampFournirIds.size()}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Etat civil</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Nom</span>
                    <span class="value">${personne.nom}</span>
                </div>
                <div class="item">
                    <span class="label">Prenom</span>
                    <span class="value">${personne.prenom}</span>
                </div>
                <div class="item">
                    <span class="label">Date de naissance</span>
                    <span class="value">${personne.dateNaissance}</span>
                </div>
                <div class="item">
                    <span class="label">Telephone</span>
                    <span class="value">${personne.telephone}</span>
                </div>
                <div class="item">
                    <span class="label">Nationalite</span>
                    <span class="value">${personne.nationalite.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Situation familiale</span>
                    <span class="value">${personne.situationFamiliale.libelle}</span>
                </div>
                <div class="item item-span-2">
                    <span class="label">Adresse</span>
                    <span class="value">${personne.adresse}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Passeport</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Numero passeport</span>
                    <span class="value">${passeport.numero}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'expiration</span>
                    <span class="value">${passeport.dateExpiration}</span>
                </div>
                <div class="item">
                    <span class="label">Email</span>
                    <span class="value">${personne.email}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Visa transformable</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Numero</span>
                    <span class="value">${visaTransformable.numero}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'arrivee</span>
                    <span class="value">${visaTransformable.dateArrivee}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'expiration</span>
                    <span class="value">${visaTransformable.dateExpiration}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Actions</h2>
            <div class="actions">
                <a class="btn btn-primary" href="/demandes">Retour a la liste</a>
                <c:if test="${canEdit}">
                    <a class="btn btn-secondary" href="/demande/modifier?id=${demande.id}">Modifier cette demande</a>
                </c:if>
            </div>
        </div>
    </div>
</div>
