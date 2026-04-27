<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page">
    <div class="header">
        <h1>Verification du numero de visa</h1>
        <p>Entrez le numero du visa source pour savoir si la demande de transfert doit reprendre des donnees existantes.</p>
        <div class="badge">Type de demande selectionne: ${typeDemande.libelle}</div>
    </div>

    <div class="form-card">
        <form action="/demande/check-numero-visa" method="post">
            <input type="hidden" name="typeDemandeId" value="${typeDemandeId}">

            <div class="form-grid">
                <div class="field full-width">
                    <label for="numeroVisa">Numero visa</label>
                    <input id="numeroVisa" name="numeroVisa" type="text" placeholder="Numero du visa source" value="${numeroVisa}">
                </div>
            </div>

            <div class="form-actions">
                <a class="btn btn-secondary" href="/home">Retour</a>
                <button class="btn btn-primary" type="submit">Verifier</button>
            </div>
        </form>
    </div>

    <c:if test="${visaVerificationDone}">
        <div class="form-card" style="margin-top: 20px;">
            <div class="badge">
                <c:choose>
                    <c:when test="${visaExiste}">Visa trouve</c:when>
                    <c:otherwise>Visa introuvable</c:otherwise>
                </c:choose>
            </div>
            <p>${verificationMessage}</p>

            <div class="form-actions">
                <a class="btn btn-primary" href="${redirectUrl}">Remplir le formulaire</a>
            </div>
        </div>
    </c:if>
</div>