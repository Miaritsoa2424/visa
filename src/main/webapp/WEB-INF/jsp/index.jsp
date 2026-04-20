<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visa - Bienvenue</title>
</head>
<body>
<h1>Bienvenue</h1>
<p>Choisissez le type de demande que vous voulez faire.</p>

<p>
    <a href="/demandes">Voir la liste des demandes</a> |
</p>

<h2>Types de demande</h2>
<c:choose>
    <c:when test="${not empty typesDemande}">
        <ul>
            <c:forEach items="${typesDemande}" var="typeDemande">
                <li>
                    <a href="/demande/nouvelle?typeDemandeId=${typeDemande.id}">${typeDemande.libelle}</a>
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <p>Aucun type de demande n'est disponible pour le moment.</p>
        <p>Verifiez que la table type_demande contient des donnees et rechargez /home.</p>
    </c:otherwise>
</c:choose>
</body>
</html>
