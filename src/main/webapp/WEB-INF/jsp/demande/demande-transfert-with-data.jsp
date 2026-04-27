<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page">
    <div class="header">
        <h1>Demande de transfert de visa</h1>
        <%-- <p>Formulaire statique organise par sections pour preparer une demande de visa avec les champs issus des tables du modele.</p> --%>
        <div class="badge">Type de demande selectionne: ${typeDemande.libelle}</div>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">
            <strong>Erreur:</strong> ${errorMessage}
        </div>
    </c:if>

    <c:if test="${not empty succesMessage}">
        <div class="alert alert-success">
            <strong>Succes:</strong> ${succesMessage}
        </div>
    </c:if>

    <div class="form-card">
        <form action="/transfert/execute" method="post">
            <div class="form-grid">
                <details class="section">
                    <summary>
                        <span>Etat civil</span>
                        <span class="summary-note">Identite et informations personnelles</span>
                    </summary>
                    <div class="section-body">
                        <c:choose>
                            <c:when test="${not empty personne}">
                                <div class="civil-card">
                                    <div class="civil-grid">
                                        <div class="civil-item">
                                            <span class="civil-label">Nom</span>
                                            <span class="civil-value">${personne.nom}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Prenom</span>
                                            <span class="civil-value">${personne.prenom}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Nom de jeune fille</span>
                                            <span class="civil-value">${empty personne.nomJeuneFille ? '-' : personne.nomJeuneFille}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Email</span>
                                            <span class="civil-value">${empty personne.email ? '-' : personne.email}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Date de naissance</span>
                                            <span class="civil-value">${personne.dateNaissance}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Lieu de naissance</span>
                                            <span class="civil-value">${empty personne.lieuNaissance ? '-' : personne.lieuNaissance}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Telephone</span>
                                            <span class="civil-value">${personne.telephone}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Nationalite</span>
                                            <span class="civil-value">${personne.nationalite.libelle}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Situation familiale</span>
                                            <span class="civil-value">${personne.situationFamiliale.libelle}</span>
                                        </div>
                                        <div class="civil-item civil-item-full">
                                            <span class="civil-label">Adresse</span>
                                            <span class="civil-value">${personne.adresse}</span>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="civil-empty">
                                    Aucune information d'etat civil n'a ete trouvee pour la personne concernee.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </details>

                <details class="section" open>
                    <summary>
                        <span>Details du visa</span>
                        <span class="summary-note">Informations extraites de la table Visa</span>
                    </summary>
                    <div class="section-body">
                        <c:choose>
                            <c:when test="${not empty visa}">
                                <div class="civil-card">
                                    <div class="civil-grid">
                                        <div class="civil-item">
                                            <span class="civil-label">Numero visa</span>
                                            <span class="civil-value">${visa.numero}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Type visa</span>
                                            <span class="civil-value">${empty visa.typeVisa ? '-' : visa.typeVisa.libelle}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Pays d'entree</span>
                                            <span class="civil-value">${empty visa.paysEntree ? '-' : visa.paysEntree.libelle}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Date d'entree</span>
                                            <span class="civil-value">${empty visa.dateEntree ? '-' : visa.dateEntree}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Date de delivrance</span>
                                            <span class="civil-value">${empty visa.dateDelivrance ? '-' : visa.dateDelivrance}</span>
                                        </div>
                                        <div class="civil-item">
                                            <span class="civil-label">Date d'expiration</span>
                                            <span class="civil-value">${empty visa.dateExpiration ? '-' : visa.dateExpiration}</span>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="civil-empty">
                                    Aucun visa n'a ete trouve pour la personne concernee.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </details>

                <input type="hidden" name="typeDemandeId" value="${typeDemande.id}">
                <input type="hidden" name="personneId" value="${personne.id}">
                <input type="hidden" name="numeroVisa" value="${visa.numero}">

                <details class="section">
                    <summary>
                        <span>Nouveaux Passeport</span>
                        <span class="summary-note">Identifiant de voyage</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="numeroPasseport">Numero passeport <span class="required-mark">*</span></label>
                                <input id="numeroPasseport" name="numeroPasseport" type="text" placeholder="Numero du passeport" required>
                            </div>
                            <div class="field col-6">
                                <label for="dateExpirationPasseport">Date d'expiration <span class="required-mark">*</span></label>
                                <input id="dateExpirationPasseport" name="dateExpirationPasseport" type="date" required>
                            </div>
                        </div>
                    </div>
                </details>

            </div>

            <div class="form-actions">
                <a class="btn btn-secondary" href="/demandes">Annuler</a>
                <button class="btn btn-primary" type="submit">Previsualiser la demande</button>
            </div>
        </form>
    </div>
</div>
</main>
