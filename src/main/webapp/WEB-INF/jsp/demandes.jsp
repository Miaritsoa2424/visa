<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des demandes</title>
</head>
<body>
<div class="container">
    <h1>Liste des demandes</h1>

    <c:choose>
        <c:when test="${not empty demandes}">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Date demande</th>
                    <th>Demandeur</th>
                    <th>Numéro passeport</th>
                    <th>Type visa</th>
                    <th>Type demande</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${demandes}" var="demande">
                    <tr>
                        <td>${demande.id}</td>
                        <td>${demande.dateDemande}</td>
                        <td>${demande.passeport.personne.prenom} ${demande.passeport.personne.nom}</td>
                        <td>${demande.passeport.numero}</td>
                        <td>${demande.typeVisa.libelle}</td>
                        <td>${demande.typeDemande.libelle}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="empty">Aucune demande trouvée.</div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>