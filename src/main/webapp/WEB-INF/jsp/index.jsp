<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
    <h1>Bienvenue</h1>
    <p>Choisissez le type de demande que vous voulez faire.</p>

    <a class="btn btn-primary" href="/demandes">Voir la liste des demandes</a>

    <h2 class="list-title">Types de demande</h2>
    <c:choose>
        <c:when test="${not empty typesDemande}">
            <ul class="type-list">
                <c:forEach items="${typesDemande}" var="typeDemande">
                    <li>
                        <a class="type-link" href="/demande/nouvelle?typeDemandeId=${typeDemande.id}">${typeDemande.libelle}</a>
                    </li>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <div class="empty-message">Aucun type de demande n'est disponible pour le moment.</div>
            <p class="hint">Verifiez que la table type_demande contient des donnees, puis rechargez /home.</p>
        </c:otherwise>
    </c:choose>
</div>
