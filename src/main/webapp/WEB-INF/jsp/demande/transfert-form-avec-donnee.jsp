<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page">
    <div class="header">
        <h1>Formulaire de transfert avec donnees</h1>
        <p>${description}</p>
        <div class="badge">Type de demande selectionne: ${typeDemande.libelle}</div>
    </div>

    <div class="form-card">
        <p>Le formulaire complet de transfert avec donnees sera ajoute dans une prochaine etape.</p>
        <div class="form-actions">
            <a class="btn btn-secondary" href="/home">Retour a l'accueil</a>
        </div>
    </div>
</div>