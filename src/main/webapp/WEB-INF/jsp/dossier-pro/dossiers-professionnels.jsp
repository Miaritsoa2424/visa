<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page dossier-pro-page">
    <div class="header">
        <h1>Dossiers professionnels</h1>
        <p>Consultez les dossiers professionnels relies a la demande ainsi que leur statut le plus recent.</p>
        <div class="badge">Demande selectionnee: #${demandeId}</div>
    </div>

    <c:if test="${not empty infoMessage}">
        <div class="alert alert-success">
            <strong>Information:</strong> ${infoMessage}
        </div>
    </c:if>

    <div class="form-card">
        <div class="form-actions top-actions">
            <a class="btn btn-secondary" href="/demandes">Retour aux demandes</a>
        </div>

        <c:choose>
            <c:when test="${not empty dossiersProfessionnels}">
                <table class="dossier-pro-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Champ a fournir</th>
                        <th>Valeur</th>
                        <th>Statut le plus recent</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${dossiersProfessionnels}" var="dossier">
                        <tr>
                            <td>${dossier.dossierProfessionnelId}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty dossier.champFournirLibelle}">
                                        ${dossier.champFournirLibelle}
                                    </c:when>
                                    <c:otherwise>
                                        Non renseigne
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty dossier.valeur}">
                                        ${dossier.valeur}
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty dossier.dernierStatutLibelle}">
                                        <span class="status-pill">${dossier.dernierStatutLibelle}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pill status-unknown">Aucun statut</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a class="btn btn-primary btn-scanner" href="/dossier-pro/scanner?id=${dossier.dossierProfessionnelId}">Scanner</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-message">Aucun dossier professionnel trouve pour cette demande.</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
