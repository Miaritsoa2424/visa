<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page">
    <div class="header">
        <h1>Modifier demande</h1>
        <p>Meme formulaire que la creation, avec les champs pre remplis.</p>
        <div class="badge">Type de demande selectionne: ${typeDemande.libelle}</div>
    </div>

    <div class="form-card">
        <form action="/demande/modifier" method="post">
            <input type="hidden" name="demandeId" value="${demande.id}">
            <div class="form-grid">
                <details class="section" open>
                    <summary>
                        <span>Etat civil</span>
                        <span class="summary-note">Identite et informations personnelles</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="nom">Nom <span class="required-mark">*</span></label>
                                <input id="nom" name="nom" type="text" value="${personne.nom}" required>
                            </div>
                            <div class="field col-6">
                                <label for="prenom">Prenom</label>
                                <input id="prenom" name="prenom" type="text" value="${personne.prenom}">
                            </div>
                            <div class="field col-6">
                                <label for="nomJeuneFille">Nom de jeune fille</label>
                                <input id="nomJeuneFille" name="nomJeuneFille" type="text" value="${personne.nomJeuneFille}">
                            </div>
                            <div class="field col-6">
                                <label for="email">Email</label>
                                <input id="email" name="email" type="email" value="${personne.email}">
                            </div>
                            <div class="field col-4">
                                <label for="dateNaissance">Date de naissance <span class="required-mark">*</span></label>
                                <input id="dateNaissance" name="dateNaissance" type="date" value="${personne.dateNaissance}" required>
                            </div>
                            <div class="field col-4">
                                <label for="lieuNaissance">Lieu de naissance</label>
                                <input id="lieuNaissance" name="lieuNaissance" type="text" value="${personne.lieuNaissance}">
                            </div>
                            <div class="field col-4">
                                <label for="telephone">Telephone <span class="required-mark">*</span></label>
                                <input id="telephone" name="telephone" type="tel" value="${personne.telephone}" required>
                            </div>
                            <div class="field full-width">
                                <label for="adresse">Adresse <span class="required-mark">*</span></label>
                                <textarea id="adresse" name="adresse" required>${personne.adresse}</textarea>
                            </div>
                            <div class="field col-6">
                                <label for="nationalite">Nationalite <span class="required-mark">*</span></label>
                                <select id="nationalite" name="nationalite" required>
                                    <option value="">Selectionner une nationalite</option>
                                    <c:forEach items="${nationalites}" var="nationalite">
                                        <option value="${nationalite.id}" <c:if test="${personne.nationalite != null and personne.nationalite.id == nationalite.id}">selected</c:if>>${nationalite.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="field col-6">
                                <label for="situationFamiliale">Situation familiale <span class="required-mark">*</span></label>
                                <select id="situationFamiliale" name="situationFamiliale" required>
                                    <option value="">Selectionner une situation</option>
                                    <c:forEach items="${situationsFamiliales}" var="situation">
                                        <option value="${situation.id}" <c:if test="${personne.situationFamiliale != null and personne.situationFamiliale.id == situation.id}">selected</c:if>>${situation.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </details>

                <details class="section" open>
                    <summary>
                        <span>Passeport</span>
                        <span class="summary-note">Identifiant de voyage</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="numeroPasseport">Numero passeport <span class="required-mark">*</span></label>
                                <input id="numeroPasseport" name="numeroPasseport" type="text" value="${passeport.numero}" required>
                            </div>
                            <div class="field col-6">
                                <label for="dateExpirationPasseport">Date d'expiration <span class="required-mark">*</span></label>
                                <input id="dateExpirationPasseport" name="dateExpirationPasseport" type="date" value="${passeport.dateExpiration}" required>
                            </div>
                        </div>
                    </div>
                </details>

                <details class="section" open>
                    <summary>
                        <span>Visa transformable</span>
                        <span class="summary-note">Visa en cours de transformation</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-hint">Tous les champs de cette section sont obligatoires.</div>
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="numeroVisaTransformable">Numero visa transformable <span class="required-mark">*</span></label>
                                <input id="numeroVisaTransformable" name="numeroVisaTransformable" type="text" value="${visaTransformable.numero}" required>
                            </div>
                            <div class="field col-6">
                                <label for="dateArrivee">Date d'arrivee <span class="required-mark">*</span></label>
                                <input id="dateArrivee" name="dateArrivee" type="date" value="${visaTransformable.dateArrivee}" required>
                            </div>
                            <div class="field col-6">
                                <label for="dateExpirationVisaTransformable">Date d'expiration <span class="required-mark">*</span></label>
                                <input id="dateExpirationVisaTransformable" name="dateExpirationVisaTransformable" type="date" value="${visaTransformable.dateExpiration}" required>
                            </div>
                        </div>
                    </div>
                </details>

                <details class="section" open>
                    <summary>
                        <span>Visa demande</span>
                        <span class="summary-note">Demande principale</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-4">
                                <label for="dateDemande">Date demande <span class="required-mark">*</span></label>
                                <input id="dateDemande" name="dateDemande" type="date" value="${demande.dateDemande}" required>
                            </div>
                            <div class="field col-4">
                                <label for="typeVisa">Type visa <span class="required-mark">*</span></label>
                                <select id="typeVisa" name="typeVisa" required>
                                    <option value="">Selectionner un type visa</option>
                                    <c:forEach items="${typesVisa}" var="typeVisa">
                                        <option value="${typeVisa.id}" <c:if test="${selectedTypeVisaId != null and selectedTypeVisaId == typeVisa.id}">selected</c:if>>${typeVisa.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="field col-4">
                                <input id="typeDemandeId" name="typeDemandeId" type="hidden" value="${typeDemandeId}">
                            </div>

                            <div class="dynamic-champs">
                                <h4>Champs a fournir (selon le type de visa)</h4>
                                <div id="champsFournirContainer" class="checkbox-list">
                                    <p class="empty-dynamic">Selectionnez un type de visa pour afficher les champs.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </details>
            </div>

            <div class="form-actions">
                <a class="btn btn-secondary" href="/demandes">Annuler</a>
                <c:choose>
                    <c:when test="${isScanTermine}">
                        <span>Fichier deja uploade</span>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-secondary" type="button">Scanner les fichiers</button>
                    </c:otherwise>
                </c:choose>
                <button class="btn btn-primary" type="submit">Enregistrer les changements</button>
            </div>
        </form>
    </div>
</div>
<script>
    (function () {
        const typeVisaSelect = document.getElementById("typeVisa");
        const container = document.getElementById("champsFournirContainer");
        const selectedChampIds = new Set([
            <c:forEach items="${selectedChampFournirIds}" var="selectedChampId">${selectedChampId},</c:forEach>
        ]);

        async function loadChampsFournir(typeVisaId) {
            if (!typeVisaId) {
                container.innerHTML = '<p class="empty-dynamic">Selectionnez un type de visa pour afficher les champs.</p>';
                return;
            }

            try {
                const url = "/demande/champs-fournir?typeVisaId=" + encodeURIComponent(typeVisaId);
                const response = await fetch(url);
                if (!response.ok) {
                    throw new Error("Erreur de chargement des champs a fournir");
                }

                const champs = await response.json();
                if (!Array.isArray(champs) || champs.length === 0) {
                    container.innerHTML = '<p class="empty-dynamic">Aucun champ requis pour ce type de visa.</p>';
                    return;
                }

                const html = champs
                    .map(function (champ) {
                        const checked = selectedChampIds.has(champ.id) ? ' checked' : '';
                        return '<label class="checkbox-item" for="champ-' + champ.id + '">' +
                            '<input id="champ-' + champ.id + '" type="checkbox" name="champFournirIds" value="' + champ.id + '"' + checked + '>' +
                            '<span>' + champ.libelle + '</span>' +
                            '</label>';
                    })
                    .join("");

                container.innerHTML = html;
            } catch (error) {
                container.innerHTML = '<p class="empty-dynamic">Impossible de charger les champs. Reessayez.</p>';
            }
        }

        typeVisaSelect.addEventListener("change", function (event) {
            loadChampsFournir(event.target.value);
        });

        loadChampsFournir(typeVisaSelect.value);
    })();
</script>
